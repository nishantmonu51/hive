/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.metastore.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hive.common.StatsSetupConst;
import org.apache.hadoop.hive.metastore.Warehouse;
import org.apache.hadoop.hive.metastore.annotation.MetastoreUnitTest;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.client.builder.DatabaseBuilder;
import org.apache.hadoop.hive.metastore.client.builder.PartitionBuilder;
import org.hamcrest.core.IsNot;

import org.apache.hadoop.hive.metastore.client.builder.TableBuilder;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.hadoop.hive.common.StatsSetupConst.COLUMN_STATS_ACCURATE;
import static org.apache.hadoop.hive.common.StatsSetupConst.NUM_FILES;
import static org.apache.hadoop.hive.common.StatsSetupConst.NUM_ERASURE_CODED_FILES;
import static org.apache.hadoop.hive.common.StatsSetupConst.STATS_GENERATED;
import static org.apache.hadoop.hive.common.StatsSetupConst.TOTAL_SIZE;
import static org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.updateTableStatsSlow;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;


@Category(MetastoreUnitTest.class)
public class TestMetaStoreUtils {

  private static final String DB_NAME = "db1";
  private static final String TABLE_NAME = "tbl1";

  private final Map<String, String> paramsWithStats = ImmutableMap.of(
      NUM_FILES, "1",
      TOTAL_SIZE, "2",
      NUM_ERASURE_CODED_FILES, "0"
  );

  private Database db;

  public TestMetaStoreUtils() {
    try {
      db = new DatabaseBuilder().setName(DB_NAME).build(null);
    } catch (TException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTrimMapNullsXform() throws Exception {
    Map<String,String> m = new HashMap<>();
    m.put("akey","aval");
    m.put("blank","");
    m.put("null",null);

    Map<String, String> expected = ImmutableMap.of("akey", "aval",
        "blank", "", "null", "");

    Map<String,String> xformed = MetaStoreUtils.trimMapNulls(m,true);
    assertThat(xformed, is(expected));
  }

  @Test
  public void testTrimMapNullsPrune() throws Exception {
    Map<String,String> m = new HashMap<>();
    m.put("akey","aval");
    m.put("blank","");
    m.put("null",null);
    Map<String, String> expected = ImmutableMap.of("akey", "aval", "blank", "");

    Map<String,String> pruned = MetaStoreUtils.trimMapNulls(m,false);
    assertThat(pruned, is(expected));
  }

  @Test
  public void testcolumnsIncludedByNameType() {
    FieldSchema col1 = new FieldSchema("col1", "string", "col1 comment");
    FieldSchema col1a = new FieldSchema("col1", "string", "col1 but with a different comment");
    FieldSchema col2 = new FieldSchema("col2", "string", "col2 comment");
    FieldSchema col3 = new FieldSchema("col3", "string", "col3 comment");
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1), Arrays.asList(col1)));
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1), Arrays.asList(col1a)));
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1, col2), Arrays.asList(col1, col2)));
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1, col2), Arrays.asList(col2, col1)));
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1, col2), Arrays.asList(col1, col2, col3)));
    Assert.assertTrue(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1, col2), Arrays.asList(col3, col2, col1)));
    Assert.assertFalse(org.apache.hadoop.hive.metastore.utils.MetaStoreUtils.columnsIncludedByNameType(Arrays.asList(col1, col2), Arrays.asList(col1)));
  }

  /**
   * Verify that updateTableStatsSlow really updates table statistics.
   * The test does the following:
   * <ol>
   *   <li>Create database</li>
   *   <li>Create unpartitioned table</li>
   *   <li>Create unpartitioned table which has params</li>
   *   <li>Call updateTableStatsSlow with arguments which should cause stats calculation</li>
   *   <li>Verify table statistics using mocked warehouse</li>
   *   <li>Create table which already have stats</li>
   *   <li>Call updateTableStatsSlow forcing stats recompute</li>
   *   <li>Verify table statistics using mocked warehouse</li>
   *   <li>Verifies behavior when STATS_GENERATED is set in environment context</li>
   * </ol>
   */
  @Test
  public void testUpdateTableStatsSlow_statsUpdated() throws TException {
    long fileLength = 5;

    // Create database and table
    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .build(null);


    // Set up mock warehouse
    FileStatus fs1 = getFileStatus(1, true, 2, 3, 4, "/tmp/0", false);
    FileStatus fs2 = getFileStatus(fileLength, false, 3, 4, 5, "/tmp/1", true);
    FileStatus fs3 = getFileStatus(fileLength, false, 3, 4, 5, "/tmp/1", false);
    List<FileStatus> fileStatus = Arrays.asList(fs1, fs2, fs3);
    Warehouse wh = mock(Warehouse.class);
    when(wh.getFileStatusesForUnpartitionedTable(db, tbl)).thenReturn(fileStatus);

    Map<String, String> expected = ImmutableMap.of(NUM_FILES, "2",
        TOTAL_SIZE, String.valueOf(2 * fileLength),
        NUM_ERASURE_CODED_FILES, "1"
    );
    updateTableStatsSlow(db, tbl, wh, false, false, null);
    assertThat(tbl.getParameters(), is(expected));

    // Verify that when stats are already present and forceRecompute is specified they are recomputed
    Table tbl1 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .addTableParam(NUM_FILES, "0")
        .addTableParam(TOTAL_SIZE, "0")
        .build(null);
    when(wh.getFileStatusesForUnpartitionedTable(db, tbl1)).thenReturn(fileStatus);
    updateTableStatsSlow(db, tbl1, wh, false, true, null);
    assertThat(tbl1.getParameters(), is(expected));

    // Verify that COLUMN_STATS_ACCURATE is removed from params
    Table tbl2 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .addTableParam(COLUMN_STATS_ACCURATE, "true")
        .build(null);
    when(wh.getFileStatusesForUnpartitionedTable(db, tbl2)).thenReturn(fileStatus);
    updateTableStatsSlow(db, tbl2, wh, false, true, null);
    assertThat(tbl2.getParameters(), is(expected));

    EnvironmentContext context = new EnvironmentContext(ImmutableMap.of(STATS_GENERATED,
        StatsSetupConst.TASK));

    // Verify that if environment context has STATS_GENERATED set to task,
    // COLUMN_STATS_ACCURATE in params is set to correct value
    Table tbl3 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .addTableParam(COLUMN_STATS_ACCURATE, "foo") // The value doesn't matter
        .build(null);
    when(wh.getFileStatusesForUnpartitionedTable(db, tbl3)).thenReturn(fileStatus);
    updateTableStatsSlow(db, tbl3, wh, false, true, context);

    Map<String, String> expected1 = ImmutableMap.of(NUM_FILES, "2",
        TOTAL_SIZE, String.valueOf(2 * fileLength),
        NUM_ERASURE_CODED_FILES, "1",
        COLUMN_STATS_ACCURATE, "{\"BASIC_STATS\":\"true\"}");
    assertThat(tbl3.getParameters(), is(expected1));
  }

  /**
   * Verify that the call to updateTableStatsSlow() removes DO_NOT_UPDATE_STATS from table params.
   */
  @Test
  public void testUpdateTableStatsSlow_removesDoNotUpdateStats() throws TException {
    // Create database and table
    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .addTableParam(StatsSetupConst.DO_NOT_UPDATE_STATS, "true")
        .build(null);
    Table tbl1 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .addTableParam(StatsSetupConst.DO_NOT_UPDATE_STATS, "false")
        .build(null);
    Warehouse wh = mock(Warehouse.class);
    updateTableStatsSlow(db, tbl, wh, false, true, null);
    assertThat(tbl.getParameters(), is(Collections.emptyMap()));
    verify(wh, never()).getFileStatusesForUnpartitionedTable(db, tbl);
    updateTableStatsSlow(db, tbl1, wh, true, false, null);
    assertThat(tbl.getParameters(), is(Collections.emptyMap()));
    verify(wh, never()).getFileStatusesForUnpartitionedTable(db, tbl1);
  }

  /**
   * Verify that updateTableStatsSlow() does not calculate table statistics when
   * <ol>
   *   <li>newDir is true</li>
   *   <li>Table is partitioned</li>
   *   <li>Stats are already present and forceRecompute isn't set</li>
   * </ol>
   */
  @Test
  public void testUpdateTableStatsSlow_doesNotUpdateStats() throws TException {
    // Create database and table
    FieldSchema fs = new FieldSchema("date", "string", "date column");
    List<FieldSchema> cols = Collections.singletonList(fs);

    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .build(null);
    Warehouse wh = mock(Warehouse.class);
    // newDir(true) => stats not updated
    updateTableStatsSlow(db, tbl, wh, true, false, null);
    verify(wh, never()).getFileStatusesForUnpartitionedTable(db, tbl);

    // partitioned table => stats not updated
    Table tbl1 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setPartCols(cols)
        .build(null);
    updateTableStatsSlow(db, tbl1, wh, false, false, null);
    verify(wh, never()).getFileStatusesForUnpartitionedTable(db, tbl1);

    // Already contains stats => stats not updated when forceRecompute isn't set
    Table tbl2 = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setTableParams(paramsWithStats)
        .build(null);
    updateTableStatsSlow(db, tbl2, wh, false, false, null);
    verify(wh, never()).getFileStatusesForUnpartitionedTable(db, tbl2);
  }

  /**
   * Build a FileStatus object.
   */
  private static FileStatus getFileStatus(long fileLength, boolean isdir, int blockReplication,
      int blockSize, int modificationTime, String pathString, boolean isErasureCoded) {
    return new FileStatus(fileLength, isdir, blockReplication, blockSize, modificationTime,
        0L, (FsPermission)null, (String)null, (String)null, null,
        new Path(pathString), false, false, isErasureCoded);
  }


  @Test
  public void testAnonymizeConnectionURL() {
    String connectionURL = null;
    String expectedConnectionURL = null;
    String result = MetaStoreUtils.anonymizeConnectionURL(connectionURL);
    assertEquals(expectedConnectionURL, result);

    connectionURL = "jdbc:mysql://localhost:1111/db?user=user&password=password";
    expectedConnectionURL = "jdbc:mysql://localhost:1111/db?user=****&password=****";
    result = MetaStoreUtils.anonymizeConnectionURL(connectionURL);
    assertEquals(expectedConnectionURL, result);

    connectionURL = "jdbc:derby:sample;user=jill;password=toFetchAPail";
    expectedConnectionURL = "jdbc:derby:sample;user=****;password=****";
    result = MetaStoreUtils.anonymizeConnectionURL(connectionURL);
    assertEquals(expectedConnectionURL, result);

    connectionURL = "jdbc:mysql://[(host=myhost1,port=1111,user=sandy,password=secret)," +
                        "(host=myhost2,port=2222,user=finn,password=secret)]/db";
    expectedConnectionURL = "jdbc:mysql://[(host=myhost1,port=1111,user=****,password=****)," +
                                "(host=myhost2,port=2222,user=****,password=****)]/db";
    result = MetaStoreUtils.anonymizeConnectionURL(connectionURL);
    assertEquals(expectedConnectionURL, result);

    connectionURL = "jdbc:derby:memory:${test.tmp.dir}/junit_metastore_db;create=true";
    result = MetaStoreUtils.anonymizeConnectionURL(connectionURL);
    assertEquals(connectionURL, result);
  }

  /**
   * Two empty StorageDescriptorKey should be equal.
   */
  @Test
  public void testCompareNullSdKey() {
    assertThat(MetaStoreUtils.StorageDescriptorKey.UNSET_KEY,
        is(new MetaStoreUtils.StorageDescriptorKey()));
  }

  /**
   * Two StorageDescriptorKey objects with null storage descriptors should be
   * equal iff the base location is equal.
   */
  @Test
  public void testCompareNullSd()
  {
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", null),
        is(new MetaStoreUtils.StorageDescriptorKey("a", null)));
    // Different locations produce different objects
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", null),
        IsNot.not(equalTo(new MetaStoreUtils.StorageDescriptorKey("b", null))));
  }

  /**
   * Two StorageDescriptorKey objects with the same base location but different
   * SD location should be equal
   */
  @Test
  public void testCompareWithSdSamePrefixDifferentLocation() throws MetaException {
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l1")
        .addCol("a", "int")
        .addValue("val1")
        .build(null);
    Partition p2 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("a", "int")
        .addValue("val1")
        .build(null);
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        is(new MetaStoreUtils.StorageDescriptorKey("a", p2.getSd())));
  }

  /**
   * Two StorageDescriptorKey objects with the same base location
   * should be equal iff their columns are equal
   */
  @Test
  public void testCompareWithSdSamePrefixDifferentCols() throws MetaException {
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l1")
        .addCol("a", "int")
        .addValue("val1")
        .build(null);
    Partition p2 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("b", "int")
        .addValue("val1")
        .build(null);
    Partition p3 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("a", "int")
        .addValue("val1")
        .build(null);
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        IsNot.not(new MetaStoreUtils.StorageDescriptorKey("a", p2.getSd())));
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        is(new MetaStoreUtils.StorageDescriptorKey("a", p3.getSd())));
  }

  /**
   * Two StorageDescriptorKey objects with the same base location
   * should be equal iff their output formats are equal
   */
  @Test
  public void testCompareWithSdSamePrefixDifferentOutputFormat() throws MetaException {
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l1")
        .addCol("a", "int")
        .addValue("val1")
        .setOutputFormat("foo")
        .build(null);
    Partition p2 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("a", "int")
        .setOutputFormat("bar")
        .addValue("val1")
        .build(null);
    Partition p3 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("a", "int")
        .setOutputFormat("foo")
        .addValue("val1")
        .build(null);
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        IsNot.not(new MetaStoreUtils.StorageDescriptorKey("a", p2.getSd())));
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        is(new MetaStoreUtils.StorageDescriptorKey("a", p3.getSd())));
  }

  /**
   * Two StorageDescriptorKey objects with the same base location
   * should be equal iff their input formats are equal
   */
  @Test
  public void testCompareWithSdSamePrefixDifferentInputFormat() throws MetaException {
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l1")
        .addCol("a", "int")
        .addValue("val1")
        .setInputFormat("foo")
        .build(null);
    Partition p2 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l2")
        .addCol("a", "int")
        .setInputFormat("bar")
        .addValue("val1")
        .build(null);
    Partition p3 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("l1")
        .addCol("a", "int")
        .addValue("val1")
        .setInputFormat("foo")
        .build(null);
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        IsNot.not(new MetaStoreUtils.StorageDescriptorKey("a", p2.getSd())));
    assertThat(new MetaStoreUtils.StorageDescriptorKey("a", p1.getSd()),
        is(new MetaStoreUtils.StorageDescriptorKey("a", p3.getSd())));
  }

  /**
   * Test getPartitionspecsGroupedByStorageDescriptor() for partitions with null SDs.
   */
  @Test
  public void testGetPartitionspecsGroupedBySDNullSD() throws MetaException {
    // Create database and table
    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setLocation("/foo")
        .build(null);
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .addCol("a", "int")
        .addValue("val1")
        .setInputFormat("foo")
        .build(null);
    // Set SD to null
    p1.unsetSd();
    assertThat(p1.getSd(), is((StorageDescriptor)null));
    List<PartitionSpec> result =
        MetaStoreUtils.getPartitionspecsGroupedByStorageDescriptor(tbl, Collections.singleton(p1));
    assertThat(result.size(), is(1));
    PartitionSpec ps = result.get(0);
    assertThat(ps.getRootPath(), is((String)null));
    List<PartitionWithoutSD> partitions = ps.getSharedSDPartitionSpec().getPartitions();
    assertThat(partitions.size(), is(1));
    PartitionWithoutSD partition = partitions.get(0);
    assertThat(partition.getRelativePath(), is((String)null));
    assertThat(partition.getValues(), is(Collections.singletonList("val1")));
  }

  /**
   * Test getPartitionspecsGroupedByStorageDescriptor() for partitions with a single
   * partition which is located under table location.
   */
  @Test
  public void testGetPartitionspecsGroupedBySDOnePartitionInTable() throws MetaException {
    // Create database and table
    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setLocation("/foo")
        .build(null);
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("/foo/bar")
        .addCol("a", "int")
        .addValue("val1")
        .setInputFormat("foo")
        .build(null);
    List<PartitionSpec> result =
        MetaStoreUtils.getPartitionspecsGroupedByStorageDescriptor(tbl, Collections.singleton(p1));
    assertThat(result.size(), is(1));
    PartitionSpec ps = result.get(0);
    assertThat(ps.getRootPath(), is(tbl.getSd().getLocation()));
    List<PartitionWithoutSD> partitions = ps.getSharedSDPartitionSpec().getPartitions();
    assertThat(partitions.size(), is(1));
    PartitionWithoutSD partition = partitions.get(0);
    assertThat(partition.getRelativePath(), is("/bar"));
    assertThat(partition.getValues(), is(Collections.singletonList("val1")));
  }

  /**
   * Test getPartitionspecsGroupedByStorageDescriptor() for partitions with a single
   * partition which is located outside table location.
   */
  @Test
  public void testGetPartitionspecsGroupedBySDonePartitionExternal() throws MetaException {
    // Create database and table
    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setLocation("/foo")
        .build(null);
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("/a/b")
        .addCol("a", "int")
        .addValue("val1")
        .setInputFormat("foo")
        .build(null);
    List<PartitionSpec> result =
        MetaStoreUtils.getPartitionspecsGroupedByStorageDescriptor(tbl, Collections.singleton(p1));
    assertThat(result.size(), is(1));
    PartitionSpec ps = result.get(0);
    assertThat(ps.getRootPath(), is((String)null));
    List<Partition>partitions = ps.getPartitionList().getPartitions();
    assertThat(partitions.size(), is(1));
    Partition partition = partitions.get(0);
    assertThat(partition.getSd().getLocation(), is("/a/b"));
    assertThat(partition.getValues(), is(Collections.singletonList("val1")));
  }

  /**
   * Test getPartitionspecsGroupedByStorageDescriptor() multiple partitions:
   * <ul>
   *   <li>Partition with null SD</li>
   *   <li>Two partitions under the table location</li>
   *   <li>One partition outside of table location</li>
   * </ul>
   */
  @Test
  public void testGetPartitionspecsGroupedBySDonePartitionCombined() throws MetaException {
    // Create database and table
    String sharedInputFormat = "foo1";

    Table tbl = new TableBuilder()
        .setDbName(DB_NAME)
        .setTableName(TABLE_NAME)
        .addCol("id", "int")
        .setLocation("/foo")
        .build(null);
    Partition p1 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("/foo/bar")
        .addCol("a1", "int")
        .addValue("val1")
        .setInputFormat(sharedInputFormat)
        .build(null);
    Partition p2 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .setLocation("/a/b")
        .addCol("a2", "int")
        .addValue("val2")
        .setInputFormat("foo2")
        .build(null);
    Partition p3 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName(TABLE_NAME)
        .addCol("a3", "int")
        .addValue("val3")
        .setInputFormat("foo3")
        .build(null);
    Partition p4 = new PartitionBuilder()
        .setDbName("DB_NAME")
        .setTableName("TABLE_NAME")
        .setLocation("/foo/baz")
        .addCol("a1", "int")
        .addValue("val4")
        .setInputFormat(sharedInputFormat)
        .build(null);
    p3.unsetSd();
    List<PartitionSpec> result =
        MetaStoreUtils.getPartitionspecsGroupedByStorageDescriptor(tbl,
            Arrays.asList(p1, p2, p3, p4));
    assertThat(result.size(), is(3));
    PartitionSpec ps1 = result.get(0);
    assertThat(ps1.getRootPath(), is((String)null));
    assertThat(ps1.getPartitionList(), is((List<Partition>)null));
    PartitionSpecWithSharedSD partSpec = ps1.getSharedSDPartitionSpec();
    List<PartitionWithoutSD> partitions1 = partSpec.getPartitions();
    assertThat(partitions1.size(), is(1));
    PartitionWithoutSD partition1 = partitions1.get(0);
    assertThat(partition1.getRelativePath(), is((String)null));
    assertThat(partition1.getValues(), is(Collections.singletonList("val3")));

    PartitionSpec ps2 = result.get(1);
    assertThat(ps2.getRootPath(), is(tbl.getSd().getLocation()));
    assertThat(ps2.getPartitionList(), is((List<Partition>)null));
    List<PartitionWithoutSD> partitions2 = ps2.getSharedSDPartitionSpec().getPartitions();
    assertThat(partitions2.size(), is(2));
    PartitionWithoutSD partition2_1 = partitions2.get(0);
    PartitionWithoutSD partition2_2 = partitions2.get(1);
    if (partition2_1.getRelativePath().equals("baz")) {
      // Swap p2_1 and p2_2
      PartitionWithoutSD tmp = partition2_1;
      partition2_1 = partition2_2;
      partition2_2 = tmp;
    }
    assertThat(partition2_1.getRelativePath(), is("/bar"));
    assertThat(partition2_1.getValues(), is(Collections.singletonList("val1")));
    assertThat(partition2_2.getRelativePath(), is("/baz"));
    assertThat(partition2_2.getValues(), is(Collections.singletonList("val4")));

    PartitionSpec ps4 = result.get(2);
    assertThat(ps4.getRootPath(), is((String)null));
    assertThat(ps4.getSharedSDPartitionSpec(), is((PartitionSpecWithSharedSD)null));
    List<Partition>partitions = ps4.getPartitionList().getPartitions();
    assertThat(partitions.size(), is(1));
    Partition partition = partitions.get(0);
    assertThat(partition.getSd().getLocation(), is("/a/b"));
    assertThat(partition.getValues(), is(Collections.singletonList("val2")));
  }
}


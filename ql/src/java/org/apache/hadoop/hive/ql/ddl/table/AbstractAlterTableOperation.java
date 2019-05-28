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

package org.apache.hadoop.hive.ql.ddl.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.common.StatsSetupConst;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaHook;
import org.apache.hadoop.hive.metastore.Warehouse;
import org.apache.hadoop.hive.metastore.api.EnvironmentContext;
import org.apache.hadoop.hive.metastore.api.InvalidOperationException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.ql.ErrorMsg;
import org.apache.hadoop.hive.ql.ddl.DDLOperation;
import org.apache.hadoop.hive.ql.ddl.DDLOperationContext;
import org.apache.hadoop.hive.ql.ddl.DDLUtils;
import org.apache.hadoop.hive.ql.ddl.table.constaint.AlterTableAddConstraintOperation;
import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.hooks.WriteEntity;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.parse.DDLSemanticAnalyzer;
import org.apache.hadoop.hive.ql.plan.AlterTableDesc.AlterTableTypes;
import org.apache.hadoop.hive.ql.session.SessionState;

/**
 * Operation process of running some alter table command that requires write id.
 */
public abstract class AbstractAlterTableOperation extends DDLOperation {
  private final AbstractAlterTableDesc desc;

  public AbstractAlterTableOperation(DDLOperationContext context, AbstractAlterTableDesc desc) {
    super(context);
    this.desc = desc;
  }

  @Override
  public int execute() throws HiveException {
    if (!AlterTableUtils.allowOperationInReplicationScope(context.getDb(), desc.getTableName(), null,
        desc.getReplicationSpec())) {
      // no alter, the table is missing either due to drop/rename which follows the alter.
      // or the existing table is newer than our update.
      LOG.debug("DDLTask: Alter Table is skipped as table {} is newer than update", desc.getTableName());
      return 0;
    }

    Table oldTable = context.getDb().getTable(desc.getTableName());
    List<Partition> partitions = getPartitions(oldTable, desc.getPartitionSpec(), context);

    // Don't change the table object returned by the metastore, as we'll mess with it's caches.
    Table table = oldTable.copy();

    EnvironmentContext environmentContext = initializeEnvironmentContext(null);

    if (partitions == null) {
      doAlteration(table, null);
    } else {
      for (Partition partition : partitions) {
        doAlteration(table, partition);
      }
    }

    finalizeAlterTableWithWriteIdOp(table, oldTable, partitions, context, environmentContext, desc);
    return 0;
  }

  private List<Partition> getPartitions(Table tbl, Map<String, String> partSpec, DDLOperationContext context)
      throws HiveException {
    List<Partition> partitions = null;
    if (partSpec != null) {
      if (DDLSemanticAnalyzer.isFullSpec(tbl, partSpec)) {
        partitions = new ArrayList<Partition>();
        Partition part = context.getDb().getPartition(tbl, partSpec, false);
        if (part == null) {
          // User provided a fully specified partition spec but it doesn't exist, fail.
          throw new HiveException(ErrorMsg.INVALID_PARTITION,
                StringUtils.join(partSpec.keySet(), ',') + " for table " + tbl.getTableName());

        }
        partitions.add(part);
      } else {
        // DDLSemanticAnalyzer has already checked if partial partition specs are allowed,
        // thus we should not need to check it here.
        partitions = context.getDb().getPartitions(tbl, partSpec);
      }
    }

    return partitions;
  }

  private EnvironmentContext initializeEnvironmentContext(EnvironmentContext environmentContext) {
    EnvironmentContext result = environmentContext == null ? new EnvironmentContext() : environmentContext;
    // do not need update stats in alter table/partition operations
    if (result.getProperties() == null ||
        result.getProperties().get(StatsSetupConst.DO_NOT_UPDATE_STATS) == null) {
      result.putToProperties(StatsSetupConst.DO_NOT_UPDATE_STATS, StatsSetupConst.TRUE);
    }
    return result;
  }

  protected abstract void doAlteration(Table table, Partition partition) throws HiveException;

  protected StorageDescriptor getStorageDescriptor(Table tbl, Partition part) {
    return (part == null ? tbl.getTTable().getSd() : part.getTPartition().getSd());
  }

  public void finalizeAlterTableWithWriteIdOp(Table table, Table oldTable, List<Partition> partitions,
      DDLOperationContext context, EnvironmentContext environmentContext, AbstractAlterTableDesc alterTable)
      throws HiveException {
    if (partitions == null) {
      updateModifiedParameters(table.getTTable().getParameters(), context.getConf());
      table.checkValidity(context.getConf());
    } else {
      for (Partition partition : partitions) {
        updateModifiedParameters(partition.getParameters(), context.getConf());
      }
    }

    try {
      environmentContext.putToProperties(HiveMetaHook.ALTER_TABLE_OPERATION_TYPE, alterTable.getType().name());
      if (partitions == null) {
          context.getDb().alterTable(desc.getTableName(), table, desc.isCascade(), environmentContext, true);
      } else {
        // Note: this is necessary for UPDATE_STATISTICS command, that operates via ADDPROPS (why?).
        //       For any other updates, we don't want to do txn check on partitions when altering table.
        boolean isTxn = desc.getPartitionSpec() != null && desc.getType() == AlterTableTypes.ADDPROPS;
        context.getDb().alterPartitions(Warehouse.getQualifiedName(table.getTTable()), partitions, environmentContext,
            isTxn);
      }
      // Add constraints if necessary
      if (alterTable instanceof AbstractAlterTableWithConstraintsDesc) {
        AlterTableAddConstraintOperation.addConstraints((AbstractAlterTableWithConstraintsDesc)alterTable,
            context.getDb());
      }
    } catch (InvalidOperationException e) {
      LOG.error("alter table: ", e);
      throw new HiveException(e, ErrorMsg.GENERIC_ERROR);
    }

    // This is kind of hacky - the read entity contains the old table, whereas the write entity contains the new
    // table. This is needed for rename - both the old and the new table names are passed
    // Don't acquire locks for any of these, we have already asked for them in DDLSemanticAnalyzer.
    if (partitions != null) {
      for (Partition partition : partitions) {
        context.getWork().getInputs().add(new ReadEntity(partition));
        DDLUtils.addIfAbsentByName(new WriteEntity(partition, WriteEntity.WriteType.DDL_NO_LOCK), context);
      }
    } else {
      context.getWork().getInputs().add(new ReadEntity(oldTable));
      DDLUtils.addIfAbsentByName(new WriteEntity(table, WriteEntity.WriteType.DDL_NO_LOCK), context);
    }
  }

  private static void updateModifiedParameters(Map<String, String> params, HiveConf conf) throws HiveException {
    String user = SessionState.getUserFromAuthenticator();
    params.put("last_modified_by", user);
    params.put("last_modified_time", Long.toString(System.currentTimeMillis() / 1000));
  }
}

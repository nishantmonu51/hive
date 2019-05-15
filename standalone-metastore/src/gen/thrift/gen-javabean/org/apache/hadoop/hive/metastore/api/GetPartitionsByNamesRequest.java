/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.hadoop.hive.metastore.api;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)")
@org.apache.hadoop.classification.InterfaceAudience.Public @org.apache.hadoop.classification.InterfaceStability.Stable public class GetPartitionsByNamesRequest implements org.apache.thrift.TBase<GetPartitionsByNamesRequest, GetPartitionsByNamesRequest._Fields>, java.io.Serializable, Cloneable, Comparable<GetPartitionsByNamesRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetPartitionsByNamesRequest");

  private static final org.apache.thrift.protocol.TField DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("db_name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField TBL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("tbl_name", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("names", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField GET_COL_STATS_FIELD_DESC = new org.apache.thrift.protocol.TField("get_col_stats", org.apache.thrift.protocol.TType.BOOL, (short)4);
  private static final org.apache.thrift.protocol.TField PROCESSOR_CAPABILITIES_FIELD_DESC = new org.apache.thrift.protocol.TField("processorCapabilities", org.apache.thrift.protocol.TType.LIST, (short)5);
  private static final org.apache.thrift.protocol.TField PROCESSOR_IDENTIFIER_FIELD_DESC = new org.apache.thrift.protocol.TField("processorIdentifier", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetPartitionsByNamesRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetPartitionsByNamesRequestTupleSchemeFactory());
  }

  private String db_name; // required
  private String tbl_name; // required
  private List<String> names; // optional
  private boolean get_col_stats; // optional
  private List<String> processorCapabilities; // optional
  private String processorIdentifier; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DB_NAME((short)1, "db_name"),
    TBL_NAME((short)2, "tbl_name"),
    NAMES((short)3, "names"),
    GET_COL_STATS((short)4, "get_col_stats"),
    PROCESSOR_CAPABILITIES((short)5, "processorCapabilities"),
    PROCESSOR_IDENTIFIER((short)6, "processorIdentifier");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // DB_NAME
          return DB_NAME;
        case 2: // TBL_NAME
          return TBL_NAME;
        case 3: // NAMES
          return NAMES;
        case 4: // GET_COL_STATS
          return GET_COL_STATS;
        case 5: // PROCESSOR_CAPABILITIES
          return PROCESSOR_CAPABILITIES;
        case 6: // PROCESSOR_IDENTIFIER
          return PROCESSOR_IDENTIFIER;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __GET_COL_STATS_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.NAMES,_Fields.GET_COL_STATS,_Fields.PROCESSOR_CAPABILITIES,_Fields.PROCESSOR_IDENTIFIER};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("db_name", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TBL_NAME, new org.apache.thrift.meta_data.FieldMetaData("tbl_name", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NAMES, new org.apache.thrift.meta_data.FieldMetaData("names", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.GET_COL_STATS, new org.apache.thrift.meta_data.FieldMetaData("get_col_stats", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.PROCESSOR_CAPABILITIES, new org.apache.thrift.meta_data.FieldMetaData("processorCapabilities", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.PROCESSOR_IDENTIFIER, new org.apache.thrift.meta_data.FieldMetaData("processorIdentifier", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetPartitionsByNamesRequest.class, metaDataMap);
  }

  public GetPartitionsByNamesRequest() {
  }

  public GetPartitionsByNamesRequest(
    String db_name,
    String tbl_name)
  {
    this();
    this.db_name = db_name;
    this.tbl_name = tbl_name;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetPartitionsByNamesRequest(GetPartitionsByNamesRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetDb_name()) {
      this.db_name = other.db_name;
    }
    if (other.isSetTbl_name()) {
      this.tbl_name = other.tbl_name;
    }
    if (other.isSetNames()) {
      List<String> __this__names = new ArrayList<String>(other.names);
      this.names = __this__names;
    }
    this.get_col_stats = other.get_col_stats;
    if (other.isSetProcessorCapabilities()) {
      List<String> __this__processorCapabilities = new ArrayList<String>(other.processorCapabilities);
      this.processorCapabilities = __this__processorCapabilities;
    }
    if (other.isSetProcessorIdentifier()) {
      this.processorIdentifier = other.processorIdentifier;
    }
  }

  public GetPartitionsByNamesRequest deepCopy() {
    return new GetPartitionsByNamesRequest(this);
  }

  @Override
  public void clear() {
    this.db_name = null;
    this.tbl_name = null;
    this.names = null;
    setGet_col_statsIsSet(false);
    this.get_col_stats = false;
    this.processorCapabilities = null;
    this.processorIdentifier = null;
  }

  public String getDb_name() {
    return this.db_name;
  }

  public void setDb_name(String db_name) {
    this.db_name = db_name;
  }

  public void unsetDb_name() {
    this.db_name = null;
  }

  /** Returns true if field db_name is set (has been assigned a value) and false otherwise */
  public boolean isSetDb_name() {
    return this.db_name != null;
  }

  public void setDb_nameIsSet(boolean value) {
    if (!value) {
      this.db_name = null;
    }
  }

  public String getTbl_name() {
    return this.tbl_name;
  }

  public void setTbl_name(String tbl_name) {
    this.tbl_name = tbl_name;
  }

  public void unsetTbl_name() {
    this.tbl_name = null;
  }

  /** Returns true if field tbl_name is set (has been assigned a value) and false otherwise */
  public boolean isSetTbl_name() {
    return this.tbl_name != null;
  }

  public void setTbl_nameIsSet(boolean value) {
    if (!value) {
      this.tbl_name = null;
    }
  }

  public int getNamesSize() {
    return (this.names == null) ? 0 : this.names.size();
  }

  public java.util.Iterator<String> getNamesIterator() {
    return (this.names == null) ? null : this.names.iterator();
  }

  public void addToNames(String elem) {
    if (this.names == null) {
      this.names = new ArrayList<String>();
    }
    this.names.add(elem);
  }

  public List<String> getNames() {
    return this.names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  public void unsetNames() {
    this.names = null;
  }

  /** Returns true if field names is set (has been assigned a value) and false otherwise */
  public boolean isSetNames() {
    return this.names != null;
  }

  public void setNamesIsSet(boolean value) {
    if (!value) {
      this.names = null;
    }
  }

  public boolean isGet_col_stats() {
    return this.get_col_stats;
  }

  public void setGet_col_stats(boolean get_col_stats) {
    this.get_col_stats = get_col_stats;
    setGet_col_statsIsSet(true);
  }

  public void unsetGet_col_stats() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __GET_COL_STATS_ISSET_ID);
  }

  /** Returns true if field get_col_stats is set (has been assigned a value) and false otherwise */
  public boolean isSetGet_col_stats() {
    return EncodingUtils.testBit(__isset_bitfield, __GET_COL_STATS_ISSET_ID);
  }

  public void setGet_col_statsIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __GET_COL_STATS_ISSET_ID, value);
  }

  public int getProcessorCapabilitiesSize() {
    return (this.processorCapabilities == null) ? 0 : this.processorCapabilities.size();
  }

  public java.util.Iterator<String> getProcessorCapabilitiesIterator() {
    return (this.processorCapabilities == null) ? null : this.processorCapabilities.iterator();
  }

  public void addToProcessorCapabilities(String elem) {
    if (this.processorCapabilities == null) {
      this.processorCapabilities = new ArrayList<String>();
    }
    this.processorCapabilities.add(elem);
  }

  public List<String> getProcessorCapabilities() {
    return this.processorCapabilities;
  }

  public void setProcessorCapabilities(List<String> processorCapabilities) {
    this.processorCapabilities = processorCapabilities;
  }

  public void unsetProcessorCapabilities() {
    this.processorCapabilities = null;
  }

  /** Returns true if field processorCapabilities is set (has been assigned a value) and false otherwise */
  public boolean isSetProcessorCapabilities() {
    return this.processorCapabilities != null;
  }

  public void setProcessorCapabilitiesIsSet(boolean value) {
    if (!value) {
      this.processorCapabilities = null;
    }
  }

  public String getProcessorIdentifier() {
    return this.processorIdentifier;
  }

  public void setProcessorIdentifier(String processorIdentifier) {
    this.processorIdentifier = processorIdentifier;
  }

  public void unsetProcessorIdentifier() {
    this.processorIdentifier = null;
  }

  /** Returns true if field processorIdentifier is set (has been assigned a value) and false otherwise */
  public boolean isSetProcessorIdentifier() {
    return this.processorIdentifier != null;
  }

  public void setProcessorIdentifierIsSet(boolean value) {
    if (!value) {
      this.processorIdentifier = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DB_NAME:
      if (value == null) {
        unsetDb_name();
      } else {
        setDb_name((String)value);
      }
      break;

    case TBL_NAME:
      if (value == null) {
        unsetTbl_name();
      } else {
        setTbl_name((String)value);
      }
      break;

    case NAMES:
      if (value == null) {
        unsetNames();
      } else {
        setNames((List<String>)value);
      }
      break;

    case GET_COL_STATS:
      if (value == null) {
        unsetGet_col_stats();
      } else {
        setGet_col_stats((Boolean)value);
      }
      break;

    case PROCESSOR_CAPABILITIES:
      if (value == null) {
        unsetProcessorCapabilities();
      } else {
        setProcessorCapabilities((List<String>)value);
      }
      break;

    case PROCESSOR_IDENTIFIER:
      if (value == null) {
        unsetProcessorIdentifier();
      } else {
        setProcessorIdentifier((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DB_NAME:
      return getDb_name();

    case TBL_NAME:
      return getTbl_name();

    case NAMES:
      return getNames();

    case GET_COL_STATS:
      return isGet_col_stats();

    case PROCESSOR_CAPABILITIES:
      return getProcessorCapabilities();

    case PROCESSOR_IDENTIFIER:
      return getProcessorIdentifier();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DB_NAME:
      return isSetDb_name();
    case TBL_NAME:
      return isSetTbl_name();
    case NAMES:
      return isSetNames();
    case GET_COL_STATS:
      return isSetGet_col_stats();
    case PROCESSOR_CAPABILITIES:
      return isSetProcessorCapabilities();
    case PROCESSOR_IDENTIFIER:
      return isSetProcessorIdentifier();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetPartitionsByNamesRequest)
      return this.equals((GetPartitionsByNamesRequest)that);
    return false;
  }

  public boolean equals(GetPartitionsByNamesRequest that) {
    if (that == null)
      return false;

    boolean this_present_db_name = true && this.isSetDb_name();
    boolean that_present_db_name = true && that.isSetDb_name();
    if (this_present_db_name || that_present_db_name) {
      if (!(this_present_db_name && that_present_db_name))
        return false;
      if (!this.db_name.equals(that.db_name))
        return false;
    }

    boolean this_present_tbl_name = true && this.isSetTbl_name();
    boolean that_present_tbl_name = true && that.isSetTbl_name();
    if (this_present_tbl_name || that_present_tbl_name) {
      if (!(this_present_tbl_name && that_present_tbl_name))
        return false;
      if (!this.tbl_name.equals(that.tbl_name))
        return false;
    }

    boolean this_present_names = true && this.isSetNames();
    boolean that_present_names = true && that.isSetNames();
    if (this_present_names || that_present_names) {
      if (!(this_present_names && that_present_names))
        return false;
      if (!this.names.equals(that.names))
        return false;
    }

    boolean this_present_get_col_stats = true && this.isSetGet_col_stats();
    boolean that_present_get_col_stats = true && that.isSetGet_col_stats();
    if (this_present_get_col_stats || that_present_get_col_stats) {
      if (!(this_present_get_col_stats && that_present_get_col_stats))
        return false;
      if (this.get_col_stats != that.get_col_stats)
        return false;
    }

    boolean this_present_processorCapabilities = true && this.isSetProcessorCapabilities();
    boolean that_present_processorCapabilities = true && that.isSetProcessorCapabilities();
    if (this_present_processorCapabilities || that_present_processorCapabilities) {
      if (!(this_present_processorCapabilities && that_present_processorCapabilities))
        return false;
      if (!this.processorCapabilities.equals(that.processorCapabilities))
        return false;
    }

    boolean this_present_processorIdentifier = true && this.isSetProcessorIdentifier();
    boolean that_present_processorIdentifier = true && that.isSetProcessorIdentifier();
    if (this_present_processorIdentifier || that_present_processorIdentifier) {
      if (!(this_present_processorIdentifier && that_present_processorIdentifier))
        return false;
      if (!this.processorIdentifier.equals(that.processorIdentifier))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_db_name = true && (isSetDb_name());
    list.add(present_db_name);
    if (present_db_name)
      list.add(db_name);

    boolean present_tbl_name = true && (isSetTbl_name());
    list.add(present_tbl_name);
    if (present_tbl_name)
      list.add(tbl_name);

    boolean present_names = true && (isSetNames());
    list.add(present_names);
    if (present_names)
      list.add(names);

    boolean present_get_col_stats = true && (isSetGet_col_stats());
    list.add(present_get_col_stats);
    if (present_get_col_stats)
      list.add(get_col_stats);

    boolean present_processorCapabilities = true && (isSetProcessorCapabilities());
    list.add(present_processorCapabilities);
    if (present_processorCapabilities)
      list.add(processorCapabilities);

    boolean present_processorIdentifier = true && (isSetProcessorIdentifier());
    list.add(present_processorIdentifier);
    if (present_processorIdentifier)
      list.add(processorIdentifier);

    return list.hashCode();
  }

  @Override
  public int compareTo(GetPartitionsByNamesRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDb_name()).compareTo(other.isSetDb_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDb_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.db_name, other.db_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTbl_name()).compareTo(other.isSetTbl_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTbl_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tbl_name, other.tbl_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNames()).compareTo(other.isSetNames());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNames()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.names, other.names);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGet_col_stats()).compareTo(other.isSetGet_col_stats());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGet_col_stats()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.get_col_stats, other.get_col_stats);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetProcessorCapabilities()).compareTo(other.isSetProcessorCapabilities());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProcessorCapabilities()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.processorCapabilities, other.processorCapabilities);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetProcessorIdentifier()).compareTo(other.isSetProcessorIdentifier());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProcessorIdentifier()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.processorIdentifier, other.processorIdentifier);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("GetPartitionsByNamesRequest(");
    boolean first = true;

    sb.append("db_name:");
    if (this.db_name == null) {
      sb.append("null");
    } else {
      sb.append(this.db_name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("tbl_name:");
    if (this.tbl_name == null) {
      sb.append("null");
    } else {
      sb.append(this.tbl_name);
    }
    first = false;
    if (isSetNames()) {
      if (!first) sb.append(", ");
      sb.append("names:");
      if (this.names == null) {
        sb.append("null");
      } else {
        sb.append(this.names);
      }
      first = false;
    }
    if (isSetGet_col_stats()) {
      if (!first) sb.append(", ");
      sb.append("get_col_stats:");
      sb.append(this.get_col_stats);
      first = false;
    }
    if (isSetProcessorCapabilities()) {
      if (!first) sb.append(", ");
      sb.append("processorCapabilities:");
      if (this.processorCapabilities == null) {
        sb.append("null");
      } else {
        sb.append(this.processorCapabilities);
      }
      first = false;
    }
    if (isSetProcessorIdentifier()) {
      if (!first) sb.append(", ");
      sb.append("processorIdentifier:");
      if (this.processorIdentifier == null) {
        sb.append("null");
      } else {
        sb.append(this.processorIdentifier);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetDb_name()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'db_name' is unset! Struct:" + toString());
    }

    if (!isSetTbl_name()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tbl_name' is unset! Struct:" + toString());
    }

    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetPartitionsByNamesRequestStandardSchemeFactory implements SchemeFactory {
    public GetPartitionsByNamesRequestStandardScheme getScheme() {
      return new GetPartitionsByNamesRequestStandardScheme();
    }
  }

  private static class GetPartitionsByNamesRequestStandardScheme extends StandardScheme<GetPartitionsByNamesRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetPartitionsByNamesRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DB_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.db_name = iprot.readString();
              struct.setDb_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TBL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.tbl_name = iprot.readString();
              struct.setTbl_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list562 = iprot.readListBegin();
                struct.names = new ArrayList<String>(_list562.size);
                String _elem563;
                for (int _i564 = 0; _i564 < _list562.size; ++_i564)
                {
                  _elem563 = iprot.readString();
                  struct.names.add(_elem563);
                }
                iprot.readListEnd();
              }
              struct.setNamesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // GET_COL_STATS
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.get_col_stats = iprot.readBool();
              struct.setGet_col_statsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // PROCESSOR_CAPABILITIES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list565 = iprot.readListBegin();
                struct.processorCapabilities = new ArrayList<String>(_list565.size);
                String _elem566;
                for (int _i567 = 0; _i567 < _list565.size; ++_i567)
                {
                  _elem566 = iprot.readString();
                  struct.processorCapabilities.add(_elem566);
                }
                iprot.readListEnd();
              }
              struct.setProcessorCapabilitiesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // PROCESSOR_IDENTIFIER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.processorIdentifier = iprot.readString();
              struct.setProcessorIdentifierIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetPartitionsByNamesRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.db_name != null) {
        oprot.writeFieldBegin(DB_NAME_FIELD_DESC);
        oprot.writeString(struct.db_name);
        oprot.writeFieldEnd();
      }
      if (struct.tbl_name != null) {
        oprot.writeFieldBegin(TBL_NAME_FIELD_DESC);
        oprot.writeString(struct.tbl_name);
        oprot.writeFieldEnd();
      }
      if (struct.names != null) {
        if (struct.isSetNames()) {
          oprot.writeFieldBegin(NAMES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.names.size()));
            for (String _iter568 : struct.names)
            {
              oprot.writeString(_iter568);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetGet_col_stats()) {
        oprot.writeFieldBegin(GET_COL_STATS_FIELD_DESC);
        oprot.writeBool(struct.get_col_stats);
        oprot.writeFieldEnd();
      }
      if (struct.processorCapabilities != null) {
        if (struct.isSetProcessorCapabilities()) {
          oprot.writeFieldBegin(PROCESSOR_CAPABILITIES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.processorCapabilities.size()));
            for (String _iter569 : struct.processorCapabilities)
            {
              oprot.writeString(_iter569);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.processorIdentifier != null) {
        if (struct.isSetProcessorIdentifier()) {
          oprot.writeFieldBegin(PROCESSOR_IDENTIFIER_FIELD_DESC);
          oprot.writeString(struct.processorIdentifier);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetPartitionsByNamesRequestTupleSchemeFactory implements SchemeFactory {
    public GetPartitionsByNamesRequestTupleScheme getScheme() {
      return new GetPartitionsByNamesRequestTupleScheme();
    }
  }

  private static class GetPartitionsByNamesRequestTupleScheme extends TupleScheme<GetPartitionsByNamesRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetPartitionsByNamesRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.db_name);
      oprot.writeString(struct.tbl_name);
      BitSet optionals = new BitSet();
      if (struct.isSetNames()) {
        optionals.set(0);
      }
      if (struct.isSetGet_col_stats()) {
        optionals.set(1);
      }
      if (struct.isSetProcessorCapabilities()) {
        optionals.set(2);
      }
      if (struct.isSetProcessorIdentifier()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetNames()) {
        {
          oprot.writeI32(struct.names.size());
          for (String _iter570 : struct.names)
          {
            oprot.writeString(_iter570);
          }
        }
      }
      if (struct.isSetGet_col_stats()) {
        oprot.writeBool(struct.get_col_stats);
      }
      if (struct.isSetProcessorCapabilities()) {
        {
          oprot.writeI32(struct.processorCapabilities.size());
          for (String _iter571 : struct.processorCapabilities)
          {
            oprot.writeString(_iter571);
          }
        }
      }
      if (struct.isSetProcessorIdentifier()) {
        oprot.writeString(struct.processorIdentifier);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetPartitionsByNamesRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.db_name = iprot.readString();
      struct.setDb_nameIsSet(true);
      struct.tbl_name = iprot.readString();
      struct.setTbl_nameIsSet(true);
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list572 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.names = new ArrayList<String>(_list572.size);
          String _elem573;
          for (int _i574 = 0; _i574 < _list572.size; ++_i574)
          {
            _elem573 = iprot.readString();
            struct.names.add(_elem573);
          }
        }
        struct.setNamesIsSet(true);
      }
      if (incoming.get(1)) {
        struct.get_col_stats = iprot.readBool();
        struct.setGet_col_statsIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list575 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.processorCapabilities = new ArrayList<String>(_list575.size);
          String _elem576;
          for (int _i577 = 0; _i577 < _list575.size; ++_i577)
          {
            _elem576 = iprot.readString();
            struct.processorCapabilities.add(_elem576);
          }
        }
        struct.setProcessorCapabilitiesIsSet(true);
      }
      if (incoming.get(3)) {
        struct.processorIdentifier = iprot.readString();
        struct.setProcessorIdentifierIsSet(true);
      }
    }
  }

}


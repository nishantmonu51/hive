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
@org.apache.hadoop.classification.InterfaceAudience.Public @org.apache.hadoop.classification.InterfaceStability.Stable public class MaxAllocatedTableWriteIdResponse implements org.apache.thrift.TBase<MaxAllocatedTableWriteIdResponse, MaxAllocatedTableWriteIdResponse._Fields>, java.io.Serializable, Cloneable, Comparable<MaxAllocatedTableWriteIdResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MaxAllocatedTableWriteIdResponse");

  private static final org.apache.thrift.protocol.TField MAX_WRITE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("maxWriteId", org.apache.thrift.protocol.TType.I64, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MaxAllocatedTableWriteIdResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MaxAllocatedTableWriteIdResponseTupleSchemeFactory());
  }

  private long maxWriteId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    MAX_WRITE_ID((short)1, "maxWriteId");

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
        case 1: // MAX_WRITE_ID
          return MAX_WRITE_ID;
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
  private static final int __MAXWRITEID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.MAX_WRITE_ID, new org.apache.thrift.meta_data.FieldMetaData("maxWriteId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MaxAllocatedTableWriteIdResponse.class, metaDataMap);
  }

  public MaxAllocatedTableWriteIdResponse() {
  }

  public MaxAllocatedTableWriteIdResponse(
    long maxWriteId)
  {
    this();
    this.maxWriteId = maxWriteId;
    setMaxWriteIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MaxAllocatedTableWriteIdResponse(MaxAllocatedTableWriteIdResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    this.maxWriteId = other.maxWriteId;
  }

  public MaxAllocatedTableWriteIdResponse deepCopy() {
    return new MaxAllocatedTableWriteIdResponse(this);
  }

  @Override
  public void clear() {
    setMaxWriteIdIsSet(false);
    this.maxWriteId = 0;
  }

  public long getMaxWriteId() {
    return this.maxWriteId;
  }

  public void setMaxWriteId(long maxWriteId) {
    this.maxWriteId = maxWriteId;
    setMaxWriteIdIsSet(true);
  }

  public void unsetMaxWriteId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MAXWRITEID_ISSET_ID);
  }

  /** Returns true if field maxWriteId is set (has been assigned a value) and false otherwise */
  public boolean isSetMaxWriteId() {
    return EncodingUtils.testBit(__isset_bitfield, __MAXWRITEID_ISSET_ID);
  }

  public void setMaxWriteIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MAXWRITEID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case MAX_WRITE_ID:
      if (value == null) {
        unsetMaxWriteId();
      } else {
        setMaxWriteId((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case MAX_WRITE_ID:
      return getMaxWriteId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case MAX_WRITE_ID:
      return isSetMaxWriteId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MaxAllocatedTableWriteIdResponse)
      return this.equals((MaxAllocatedTableWriteIdResponse)that);
    return false;
  }

  public boolean equals(MaxAllocatedTableWriteIdResponse that) {
    if (that == null)
      return false;

    boolean this_present_maxWriteId = true;
    boolean that_present_maxWriteId = true;
    if (this_present_maxWriteId || that_present_maxWriteId) {
      if (!(this_present_maxWriteId && that_present_maxWriteId))
        return false;
      if (this.maxWriteId != that.maxWriteId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_maxWriteId = true;
    list.add(present_maxWriteId);
    if (present_maxWriteId)
      list.add(maxWriteId);

    return list.hashCode();
  }

  @Override
  public int compareTo(MaxAllocatedTableWriteIdResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetMaxWriteId()).compareTo(other.isSetMaxWriteId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaxWriteId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maxWriteId, other.maxWriteId);
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
    StringBuilder sb = new StringBuilder("MaxAllocatedTableWriteIdResponse(");
    boolean first = true;

    sb.append("maxWriteId:");
    sb.append(this.maxWriteId);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetMaxWriteId()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'maxWriteId' is unset! Struct:" + toString());
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

  private static class MaxAllocatedTableWriteIdResponseStandardSchemeFactory implements SchemeFactory {
    public MaxAllocatedTableWriteIdResponseStandardScheme getScheme() {
      return new MaxAllocatedTableWriteIdResponseStandardScheme();
    }
  }

  private static class MaxAllocatedTableWriteIdResponseStandardScheme extends StandardScheme<MaxAllocatedTableWriteIdResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MaxAllocatedTableWriteIdResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // MAX_WRITE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.maxWriteId = iprot.readI64();
              struct.setMaxWriteIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, MaxAllocatedTableWriteIdResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(MAX_WRITE_ID_FIELD_DESC);
      oprot.writeI64(struct.maxWriteId);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MaxAllocatedTableWriteIdResponseTupleSchemeFactory implements SchemeFactory {
    public MaxAllocatedTableWriteIdResponseTupleScheme getScheme() {
      return new MaxAllocatedTableWriteIdResponseTupleScheme();
    }
  }

  private static class MaxAllocatedTableWriteIdResponseTupleScheme extends TupleScheme<MaxAllocatedTableWriteIdResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MaxAllocatedTableWriteIdResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.maxWriteId);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MaxAllocatedTableWriteIdResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.maxWriteId = iprot.readI64();
      struct.setMaxWriteIdIsSet(true);
    }
  }

}


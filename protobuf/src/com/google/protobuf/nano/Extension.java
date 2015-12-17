package com.google.protobuf.nano;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Extension<M extends ExtendableMessageNano<M>, T> {
	public static final int TYPE_DOUBLE = 1;
	public static final int TYPE_FLOAT = 2;
	public static final int TYPE_INT64 = 3;
	public static final int TYPE_UINT64 = 4;
	public static final int TYPE_INT32 = 5;
	public static final int TYPE_FIXED64 = 6;
	public static final int TYPE_FIXED32 = 7;
	public static final int TYPE_BOOL = 8;
	public static final int TYPE_STRING = 9;
	public static final int TYPE_GROUP = 10;
	public static final int TYPE_MESSAGE = 11;
	public static final int TYPE_BYTES = 12;
	public static final int TYPE_UINT32 = 13;
	public static final int TYPE_ENUM = 14;
	public static final int TYPE_SFIXED32 = 15;
	public static final int TYPE_SFIXED64 = 16;
	public static final int TYPE_SINT32 = 17;
	public static final int TYPE_SINT64 = 18;
	protected final int type;
	protected final Class<T> clazz;
	public final int tag;
	protected final boolean repeated;

	@Deprecated
	public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(
			int type, Class<T> clazz, int tag) {
		return new Extension(type, clazz, tag, false);
	}

	public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(
			int type, Class<T> clazz, long tag) {
		return new Extension(type, clazz, (int) tag, false);
	}

	public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(
			int type, Class<T[]> clazz, long tag) {
		return new Extension(type, clazz, (int) tag, true);
	}

	public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(
			int type, Class<T> clazz, long tag) {
		return new PrimitiveExtension(type, clazz, (int) tag, false, 0, 0);
	}

	public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(
			int type, Class<T> clazz, long tag, long nonPackedTag,
			long packedTag) {
		return new PrimitiveExtension(type, clazz, (int) tag, true,
				(int) nonPackedTag, (int) packedTag);
	}

	private Extension(int type, Class<T> clazz, int tag, boolean repeated) {
		this.type = type;
		this.clazz = clazz;
		this.tag = tag;
		this.repeated = repeated;
	}

	final T getValueFrom(List<UnknownFieldData> unknownFields) {
		if (unknownFields == null) {
			return null;
		}
		return this.repeated ? getRepeatedValueFrom(unknownFields)
				: getSingularValueFrom(unknownFields);
	}

	private T getRepeatedValueFrom(List<UnknownFieldData> unknownFields) {
		List resultList = new ArrayList();
		for (int i = 0; i < unknownFields.size(); i++) {
			UnknownFieldData data = (UnknownFieldData) unknownFields.get(i);
			if (data.bytes.length != 0) {
				readDataInto(data, resultList);
			}
		}

		int resultSize = resultList.size();
		if (resultSize == 0) {
			return null;
		}
		Object result = this.clazz.cast(Array.newInstance(
				this.clazz.getComponentType(), resultSize));
		for (int i = 0; i < resultSize; i++) {
			Array.set(result, i, resultList.get(i));
		}
		return result;
	}

	private T getSingularValueFrom(List<UnknownFieldData> unknownFields) {
		if (unknownFields.isEmpty()) {
			return null;
		}
		UnknownFieldData lastData = (UnknownFieldData) unknownFields
				.get(unknownFields.size() - 1);
		return this.clazz.cast(readData(CodedInputByteBufferNano
				.newInstance(lastData.bytes)));
	}

	protected Object readData(CodedInputByteBufferNano input) {
		Class messageType = this.repeated ? this.clazz.getComponentType()
				: this.clazz;
		try {
			switch (this.type) {
			case 10:
				MessageNano group = (MessageNano) messageType.newInstance();
				input.readGroup(group,
						WireFormatNano.getTagFieldNumber(this.tag));
				return group;
			case 11:
				MessageNano message = (MessageNano) messageType.newInstance();
				input.readMessage(message);
				return message;
			}
			throw new IllegalArgumentException("Unknown type " + this.type);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(
					"Error creating instance of class " + messageType, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(
					"Error creating instance of class " + messageType, e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error reading extension field",
					e);
		}
	}

	protected void readDataInto(UnknownFieldData data, List<Object> resultList) {
		resultList.add(readData(CodedInputByteBufferNano
				.newInstance(data.bytes)));
	}

	void writeTo(Object value, CodedOutputByteBufferNano output)
			throws IOException {
		if (this.repeated)
			writeRepeatedData(value, output);
		else
			writeSingularData(value, output);
	}

	protected void writeSingularData(Object value, CodedOutputByteBufferNano out) {
		try {
			out.writeRawVarint32(this.tag);
			switch (this.type) {
			case 10:
				MessageNano groupValue = (MessageNano) value;
				int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
				out.writeGroupNoTag(groupValue);

				out.writeTag(fieldNumber, 4);
				break;
			case 11:
				MessageNano messageValue = (MessageNano) value;
				out.writeMessageNoTag(messageValue);
				break;
			default:
				throw new IllegalArgumentException("Unknown type " + this.type);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void writeRepeatedData(Object array,
			CodedOutputByteBufferNano output) {
		int arrayLength = Array.getLength(array);
		for (int i = 0; i < arrayLength; i++) {
			Object element = Array.get(array, i);
			if (element != null)
				writeSingularData(element, output);
		}
	}

	int computeSerializedSize(Object value) {
		if (this.repeated) {
			return computeRepeatedSerializedSize(value);
		}
		return computeSingularSerializedSize(value);
	}

	protected int computeRepeatedSerializedSize(Object array) {
		int size = 0;
		int arrayLength = Array.getLength(array);
		for (int i = 0; i < arrayLength; i++) {
			Object element = Array.get(array, i);
			if (element != null) {
				size += computeSingularSerializedSize(Array.get(array, i));
			}
		}
		return size;
	}

	protected int computeSingularSerializedSize(Object value) {
		int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
		switch (this.type) {
		case 10:
			MessageNano groupValue = (MessageNano) value;
			return CodedOutputByteBufferNano.computeGroupSize(fieldNumber,
					groupValue);
		case 11:
			MessageNano messageValue = (MessageNano) value;
			return CodedOutputByteBufferNano.computeMessageSize(fieldNumber,
					messageValue);
		}
		throw new IllegalArgumentException("Unknown type " + this.type);
	}

	private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T>
			extends Extension<M, T> {
		private final int nonPackedTag;
		private final int packedTag;

		public PrimitiveExtension(int type, Class<T> clazz, int tag,
				boolean repeated, int nonPackedTag, int packedTag) {
			super(clazz, tag, repeated, null);
			this.nonPackedTag = nonPackedTag;
			this.packedTag = packedTag;
		}

		protected Object readData(CodedInputByteBufferNano input) {
			try {
				switch (this.type) {
				case 1:
					return Double.valueOf(input.readDouble());
				case 2:
					return Float.valueOf(input.readFloat());
				case 3:
					return Long.valueOf(input.readInt64());
				case 4:
					return Long.valueOf(input.readUInt64());
				case 5:
					return Integer.valueOf(input.readInt32());
				case 6:
					return Long.valueOf(input.readFixed64());
				case 7:
					return Integer.valueOf(input.readFixed32());
				case 8:
					return Boolean.valueOf(input.readBool());
				case 9:
					return input.readString();
				case 12:
					return input.readBytes();
				case 13:
					return Integer.valueOf(input.readUInt32());
				case 14:
					return Integer.valueOf(input.readEnum());
				case 15:
					return Integer.valueOf(input.readSFixed32());
				case 16:
					return Long.valueOf(input.readSFixed64());
				case 17:
					return Integer.valueOf(input.readSInt32());
				case 18:
					return Long.valueOf(input.readSInt64());
				case 10:
				case 11:
				}
				throw new IllegalArgumentException("Unknown type " + this.type);
			} catch (IOException e) {
				throw new IllegalArgumentException(
						"Error reading extension field", e);
			}
		}

		protected void readDataInto(UnknownFieldData data,
				List<Object> resultList) {
			if (data.tag == this.nonPackedTag) {
				resultList.add(readData(CodedInputByteBufferNano
						.newInstance(data.bytes)));
			} else {
				CodedInputByteBufferNano buffer = CodedInputByteBufferNano
						.newInstance(data.bytes);
				try {
					buffer.pushLimit(buffer.readRawVarint32());
				} catch (IOException e) {
					throw new IllegalArgumentException(
							"Error reading extension field", e);
				}
				while (!buffer.isAtEnd())
					resultList.add(readData(buffer));
			}
		}

		protected final void writeSingularData(Object value,
				CodedOutputByteBufferNano output) {
			try {
				output.writeRawVarint32(this.tag);
				switch (this.type) {
				case 1:
					Double doubleValue = (Double) value;
					output.writeDoubleNoTag(doubleValue.doubleValue());
					break;
				case 2:
					Float floatValue = (Float) value;
					output.writeFloatNoTag(floatValue.floatValue());
					break;
				case 3:
					Long int64Value = (Long) value;
					output.writeInt64NoTag(int64Value.longValue());
					break;
				case 4:
					Long uint64Value = (Long) value;
					output.writeUInt64NoTag(uint64Value.longValue());
					break;
				case 5:
					Integer int32Value = (Integer) value;
					output.writeInt32NoTag(int32Value.intValue());
					break;
				case 6:
					Long fixed64Value = (Long) value;
					output.writeFixed64NoTag(fixed64Value.longValue());
					break;
				case 7:
					Integer fixed32Value = (Integer) value;
					output.writeFixed32NoTag(fixed32Value.intValue());
					break;
				case 8:
					Boolean boolValue = (Boolean) value;
					output.writeBoolNoTag(boolValue.booleanValue());
					break;
				case 9:
					String stringValue = (String) value;
					output.writeStringNoTag(stringValue);
					break;
				case 12:
					byte[] bytesValue = (byte[]) value;
					output.writeBytesNoTag(bytesValue);
					break;
				case 13:
					Integer uint32Value = (Integer) value;
					output.writeUInt32NoTag(uint32Value.intValue());
					break;
				case 14:
					Integer enumValue = (Integer) value;
					output.writeEnumNoTag(enumValue.intValue());
					break;
				case 15:
					Integer sfixed32Value = (Integer) value;
					output.writeSFixed32NoTag(sfixed32Value.intValue());
					break;
				case 16:
					Long sfixed64Value = (Long) value;
					output.writeSFixed64NoTag(sfixed64Value.longValue());
					break;
				case 17:
					Integer sint32Value = (Integer) value;
					output.writeSInt32NoTag(sint32Value.intValue());
					break;
				case 18:
					Long sint64Value = (Long) value;
					output.writeSInt64NoTag(sint64Value.longValue());
					break;
				case 10:
				case 11:
				default:
					throw new IllegalArgumentException("Unknown type "
							+ this.type);
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		protected void writeRepeatedData(Object array,
				CodedOutputByteBufferNano output) {
			if (this.tag == this.nonPackedTag) {
				super.writeRepeatedData(array, output);
			} else if (this.tag == this.packedTag) {
				int arrayLength = Array.getLength(array);
				int dataSize = computePackedDataSize(array);
				try {
					output.writeRawVarint32(this.tag);
					output.writeRawVarint32(dataSize);
					switch (this.type) {
					case 8:
						for (int i = 0; i < arrayLength; i++) {
							output.writeBoolNoTag(Array.getBoolean(array, i));
						}
						break;
					case 7:
						for (int i = 0; i < arrayLength; i++) {
							output.writeFixed32NoTag(Array.getInt(array, i));
						}
						break;
					case 15:
						for (int i = 0; i < arrayLength; i++) {
							output.writeSFixed32NoTag(Array.getInt(array, i));
						}
						break;
					case 2:
						for (int i = 0; i < arrayLength; i++) {
							output.writeFloatNoTag(Array.getFloat(array, i));
						}
						break;
					case 6:
						for (int i = 0; i < arrayLength; i++) {
							output.writeFixed64NoTag(Array.getLong(array, i));
						}
						break;
					case 16:
						for (int i = 0; i < arrayLength; i++) {
							output.writeSFixed64NoTag(Array.getLong(array, i));
						}
						break;
					case 1:
						for (int i = 0; i < arrayLength; i++) {
							output.writeDoubleNoTag(Array.getDouble(array, i));
						}
						break;
					case 5:
						for (int i = 0; i < arrayLength; i++) {
							output.writeInt32NoTag(Array.getInt(array, i));
						}
						break;
					case 17:
						for (int i = 0; i < arrayLength; i++) {
							output.writeSInt32NoTag(Array.getInt(array, i));
						}
						break;
					case 13:
						for (int i = 0; i < arrayLength; i++) {
							output.writeUInt32NoTag(Array.getInt(array, i));
						}
						break;
					case 3:
						for (int i = 0; i < arrayLength; i++) {
							output.writeInt64NoTag(Array.getLong(array, i));
						}
						break;
					case 18:
						for (int i = 0; i < arrayLength; i++) {
							output.writeSInt64NoTag(Array.getLong(array, i));
						}
						break;
					case 4:
						for (int i = 0; i < arrayLength; i++) {
							output.writeUInt64NoTag(Array.getLong(array, i));
						}
						break;
					case 14:
						for (int i = 0; i < arrayLength; i++) {
							output.writeEnumNoTag(Array.getInt(array, i));
						}
						break;
					case 9:
					case 10:
					case 11:
					case 12:
					default:
						throw new IllegalArgumentException("Unpackable type "
								+ this.type);
					}
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			} else {
				throw new IllegalArgumentException(
						"Unexpected repeated extension tag " + this.tag
								+ ", unequal to both non-packed variant "
								+ this.nonPackedTag + " and packed variant "
								+ this.packedTag);
			}
		}

		private int computePackedDataSize(Object array) {
			int dataSize = 0;
			int arrayLength = Array.getLength(array);
			switch (this.type) {
			case 8:
				dataSize = arrayLength;
				break;
			case 2:
			case 7:
			case 15:
				dataSize = arrayLength * 4;
				break;
			case 1:
			case 6:
			case 16:
				dataSize = arrayLength * 8;
				break;
			case 5:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeInt32SizeNoTag(Array.getInt(array, i));
				}

				break;
			case 17:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeSInt32SizeNoTag(Array.getInt(array, i));
				}

				break;
			case 13:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeUInt32SizeNoTag(Array.getInt(array, i));
				}

				break;
			case 3:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeInt64SizeNoTag(Array.getLong(array, i));
				}

				break;
			case 18:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeSInt64SizeNoTag(Array.getLong(array, i));
				}

				break;
			case 4:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeUInt64SizeNoTag(Array.getLong(array, i));
				}

				break;
			case 14:
				for (int i = 0; i < arrayLength; i++) {
					dataSize += CodedOutputByteBufferNano
							.computeEnumSizeNoTag(Array.getInt(array, i));
				}

				break;
			case 9:
			case 10:
			case 11:
			case 12:
			default:
				throw new IllegalArgumentException(
						"Unexpected non-packable type " + this.type);
			}
			return dataSize;
		}

		protected int computeRepeatedSerializedSize(Object array) {
			if (this.tag == this.nonPackedTag) {
				return super.computeRepeatedSerializedSize(array);
			}
			if (this.tag == this.packedTag) {
				int dataSize = computePackedDataSize(array);
				int payloadSize = dataSize
						+ CodedOutputByteBufferNano
								.computeRawVarint32Size(dataSize);

				return payloadSize
						+ CodedOutputByteBufferNano
								.computeRawVarint32Size(this.tag);
			}
			throw new IllegalArgumentException(
					"Unexpected repeated extension tag " + this.tag
							+ ", unequal to both non-packed variant "
							+ this.nonPackedTag + " and packed variant "
							+ this.packedTag);
		}

		protected final int computeSingularSerializedSize(Object value) {
			int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
			switch (this.type) {
			case 1:
				Double doubleValue = (Double) value;
				return CodedOutputByteBufferNano.computeDoubleSize(fieldNumber,
						doubleValue.doubleValue());
			case 2:
				Float floatValue = (Float) value;
				return CodedOutputByteBufferNano.computeFloatSize(fieldNumber,
						floatValue.floatValue());
			case 3:
				Long int64Value = (Long) value;
				return CodedOutputByteBufferNano.computeInt64Size(fieldNumber,
						int64Value.longValue());
			case 4:
				Long uint64Value = (Long) value;
				return CodedOutputByteBufferNano.computeUInt64Size(fieldNumber,
						uint64Value.longValue());
			case 5:
				Integer int32Value = (Integer) value;
				return CodedOutputByteBufferNano.computeInt32Size(fieldNumber,
						int32Value.intValue());
			case 6:
				Long fixed64Value = (Long) value;
				return CodedOutputByteBufferNano.computeFixed64Size(
						fieldNumber, fixed64Value.longValue());
			case 7:
				Integer fixed32Value = (Integer) value;
				return CodedOutputByteBufferNano.computeFixed32Size(
						fieldNumber, fixed32Value.intValue());
			case 8:
				Boolean boolValue = (Boolean) value;
				return CodedOutputByteBufferNano.computeBoolSize(fieldNumber,
						boolValue.booleanValue());
			case 9:
				String stringValue = (String) value;
				return CodedOutputByteBufferNano.computeStringSize(fieldNumber,
						stringValue);
			case 12:
				byte[] bytesValue = (byte[]) value;
				return CodedOutputByteBufferNano.computeBytesSize(fieldNumber,
						bytesValue);
			case 13:
				Integer uint32Value = (Integer) value;
				return CodedOutputByteBufferNano.computeUInt32Size(fieldNumber,
						uint32Value.intValue());
			case 14:
				Integer enumValue = (Integer) value;
				return CodedOutputByteBufferNano.computeEnumSize(fieldNumber,
						enumValue.intValue());
			case 15:
				Integer sfixed32Value = (Integer) value;
				return CodedOutputByteBufferNano.computeSFixed32Size(
						fieldNumber, sfixed32Value.intValue());
			case 16:
				Long sfixed64Value = (Long) value;
				return CodedOutputByteBufferNano.computeSFixed64Size(
						fieldNumber, sfixed64Value.longValue());
			case 17:
				Integer sint32Value = (Integer) value;
				return CodedOutputByteBufferNano.computeSInt32Size(fieldNumber,
						sint32Value.intValue());
			case 18:
				Long sint64Value = (Long) value;
				return CodedOutputByteBufferNano.computeSInt64Size(fieldNumber,
						sint64Value.longValue());
			case 10:
			case 11:
			}
			throw new IllegalArgumentException("Unknown type " + this.type);
		}
	}
}
package com.google.protobuf.nano;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

public final class CodedOutputByteBufferNano {
	private static final int MAX_UTF8_EXPANSION = 3;
	private final ByteBuffer buffer;
	public static final int LITTLE_ENDIAN_32_SIZE = 4;
	public static final int LITTLE_ENDIAN_64_SIZE = 8;

	private CodedOutputByteBufferNano(byte[] buffer, int offset, int length) {
		this(ByteBuffer.wrap(buffer, offset, length));
	}

	private CodedOutputByteBufferNano(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public static CodedOutputByteBufferNano newInstance(byte[] flatArray) {
		return newInstance(flatArray, 0, flatArray.length);
	}

	public static CodedOutputByteBufferNano newInstance(byte[] flatArray,
			int offset, int length) {
		return new CodedOutputByteBufferNano(flatArray, offset, length);
	}

	public void writeDouble(int fieldNumber, double value) throws IOException {
		writeTag(fieldNumber, 1);
		writeDoubleNoTag(value);
	}

	public void writeFloat(int fieldNumber, float value) throws IOException {
		writeTag(fieldNumber, 5);
		writeFloatNoTag(value);
	}

	public void writeUInt64(int fieldNumber, long value) throws IOException {
		writeTag(fieldNumber, 0);
		writeUInt64NoTag(value);
	}

	public void writeInt64(int fieldNumber, long value) throws IOException {
		writeTag(fieldNumber, 0);
		writeInt64NoTag(value);
	}

	public void writeInt32(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 0);
		writeInt32NoTag(value);
	}

	public void writeFixed64(int fieldNumber, long value) throws IOException {
		writeTag(fieldNumber, 1);
		writeFixed64NoTag(value);
	}

	public void writeFixed32(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 5);
		writeFixed32NoTag(value);
	}

	public void writeBool(int fieldNumber, boolean value) throws IOException {
		writeTag(fieldNumber, 0);
		writeBoolNoTag(value);
	}

	public void writeString(int fieldNumber, String value) throws IOException {
		writeTag(fieldNumber, 2);
		writeStringNoTag(value);
	}

	public void writeGroup(int fieldNumber, MessageNano value)
			throws IOException {
		writeTag(fieldNumber, 3);
		writeGroupNoTag(value);
		writeTag(fieldNumber, 4);
	}

	public void writeMessage(int fieldNumber, MessageNano value)
			throws IOException {
		writeTag(fieldNumber, 2);
		writeMessageNoTag(value);
	}

	public void writeBytes(int fieldNumber, byte[] value) throws IOException {
		writeTag(fieldNumber, 2);
		writeBytesNoTag(value);
	}

	public void writeUInt32(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 0);
		writeUInt32NoTag(value);
	}

	public void writeEnum(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 0);
		writeEnumNoTag(value);
	}

	public void writeSFixed32(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 5);
		writeSFixed32NoTag(value);
	}

	public void writeSFixed64(int fieldNumber, long value) throws IOException {
		writeTag(fieldNumber, 1);
		writeSFixed64NoTag(value);
	}

	public void writeSInt32(int fieldNumber, int value) throws IOException {
		writeTag(fieldNumber, 0);
		writeSInt32NoTag(value);
	}

	public void writeSInt64(int fieldNumber, long value) throws IOException {
		writeTag(fieldNumber, 0);
		writeSInt64NoTag(value);
	}

	public void writeDoubleNoTag(double value) throws IOException {
		writeRawLittleEndian64(Double.doubleToLongBits(value));
	}

	public void writeFloatNoTag(float value) throws IOException {
		writeRawLittleEndian32(Float.floatToIntBits(value));
	}

	public void writeUInt64NoTag(long value) throws IOException {
		writeRawVarint64(value);
	}

	public void writeInt64NoTag(long value) throws IOException {
		writeRawVarint64(value);
	}

	public void writeInt32NoTag(int value) throws IOException {
		if (value >= 0) {
			writeRawVarint32(value);
		} else
			writeRawVarint64(value);
	}

	public void writeFixed64NoTag(long value) throws IOException {
		writeRawLittleEndian64(value);
	}

	public void writeFixed32NoTag(int value) throws IOException {
		writeRawLittleEndian32(value);
	}

	public void writeBoolNoTag(boolean value) throws IOException {
		writeRawByte(value ? 1 : 0);
	}

	public void writeStringNoTag(String value) throws IOException {
		try {
			int minLengthVarIntSize = computeRawVarint32Size(value.length());
			int maxLengthVarIntSize = computeRawVarint32Size(value.length() * 3);
			if (minLengthVarIntSize == maxLengthVarIntSize) {
				int oldPosition = this.buffer.position();
				this.buffer.position(oldPosition + minLengthVarIntSize);
				encode(value, this.buffer);
				int newPosition = this.buffer.position();
				this.buffer.position(oldPosition);
				writeRawVarint32(newPosition - oldPosition
						- minLengthVarIntSize);
				this.buffer.position(newPosition);
			} else {
				writeRawVarint32(encodedLength(value));
				encode(value, this.buffer);
			}
		} catch (BufferOverflowException e) {
			throw new OutOfSpaceException(this.buffer.position(),
					this.buffer.limit());
		}
	}

	private static int encodedLength(CharSequence sequence) {
		int utf16Length = sequence.length();
		int utf8Length = utf16Length;
		int i = 0;

		while ((i < utf16Length) && (sequence.charAt(i) < '')) {
			i++;
		}

		for (; i < utf16Length; i++) {
			char c = sequence.charAt(i);
			if (c < 'ࠀ') {
				utf8Length += ('' - c >>> 31);
			} else {
				utf8Length += encodedLengthGeneral(sequence, i);
				break;
			}
		}

		if (utf8Length < utf16Length) {
			throw new IllegalArgumentException(
					"UTF-8 length does not fit in int: "
							+ (utf8Length + 4294967296L));
		}

		return utf8Length;
	}

	private static int encodedLengthGeneral(CharSequence sequence, int start) {
		int utf16Length = sequence.length();
		int utf8Length = 0;
		for (int i = start; i < utf16Length; i++) {
			char c = sequence.charAt(i);
			if (c < 'ࠀ') {
				utf8Length += ('' - c >>> 31);
			} else {
				utf8Length += 2;

				if ((55296 <= c) && (c <= 57343)) {
					int cp = Character.codePointAt(sequence, i);
					if (cp < 65536) {
						throw new IllegalArgumentException(
								"Unpaired surrogate at index " + i);
					}
					i++;
				}
			}
		}
		return utf8Length;
	}

	private static void encode(CharSequence sequence, ByteBuffer byteBuffer) {
		if (byteBuffer.isReadOnly())
			throw new ReadOnlyBufferException();
		if (byteBuffer.hasArray())
			try {
				int encoded = encode(sequence, byteBuffer.array(),
						byteBuffer.arrayOffset() + byteBuffer.position(),
						byteBuffer.remaining());

				byteBuffer.position(encoded - byteBuffer.arrayOffset());
			} catch (ArrayIndexOutOfBoundsException e) {
				BufferOverflowException boe = new BufferOverflowException();
				boe.initCause(e);
				throw boe;
			}
		else
			encodeDirect(sequence, byteBuffer);
	}

	private static void encodeDirect(CharSequence sequence,
			ByteBuffer byteBuffer) {
		int utf16Length = sequence.length();
		for (int i = 0; i < utf16Length; i++) {
			char c = sequence.charAt(i);
			if (c < '') {
				byteBuffer.put((byte) c);
			} else if (c < 'ࠀ') {
				byteBuffer.put((byte) (0x3C0 | c >>> '\006'));
				byteBuffer.put((byte) (0x80 | 0x3F & c));
			} else if ((c < 55296) || (57343 < c)) {
				byteBuffer.put((byte) (0x1E0 | c >>> '\f'));
				byteBuffer.put((byte) (0x80 | 0x3F & c >>> '\006'));
				byteBuffer.put((byte) (0x80 | 0x3F & c));
			} else {
				char low;
				if ((i + 1 == sequence.length())
						|| (!Character.isSurrogatePair(c,
								low = sequence.charAt(++i)))) {
					throw new IllegalArgumentException(
							"Unpaired surrogate at index " + (i - 1));
				}
				char low;
				int codePoint = Character.toCodePoint(c, low);
				byteBuffer.put((byte) (0xF0 | codePoint >>> 18));
				byteBuffer.put((byte) (0x80 | 0x3F & codePoint >>> 12));
				byteBuffer.put((byte) (0x80 | 0x3F & codePoint >>> 6));
				byteBuffer.put((byte) (0x80 | 0x3F & codePoint));
			}
		}
	}

	private static int encode(CharSequence sequence, byte[] bytes, int offset,
			int length) {
		int utf16Length = sequence.length();
		int j = offset;
		int i = 0;
		int limit = offset + length;
		char c;
		for (; (i < utf16Length) && (i + j < limit)
				&& ((c = sequence.charAt(i)) < ''); i++) {
			bytes[(j + i)] = ((byte) c);
		}
		if (i == utf16Length) {
			return j + utf16Length;
		}
		j += i;
		for (; i < utf16Length; i++) {
			char c = sequence.charAt(i);
			if ((c < '') && (j < limit)) {
				bytes[(j++)] = ((byte) c);
			} else if ((c < 'ࠀ') && (j <= limit - 2)) {
				bytes[(j++)] = ((byte) (0x3C0 | c >>> '\006'));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & c));
			} else if (((c < 55296) || (57343 < c)) && (j <= limit - 3)) {
				bytes[(j++)] = ((byte) (0x1E0 | c >>> '\f'));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & c >>> '\006'));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & c));
			} else if (j <= limit - 4) {
				char low;
				if ((i + 1 == sequence.length())
						|| (!Character.isSurrogatePair(c,
								low = sequence.charAt(++i)))) {
					throw new IllegalArgumentException(
							"Unpaired surrogate at index " + (i - 1));
				}
				char low;
				int codePoint = Character.toCodePoint(c, low);
				bytes[(j++)] = ((byte) (0xF0 | codePoint >>> 18));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & codePoint >>> 12));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & codePoint >>> 6));
				bytes[(j++)] = ((byte) (0x80 | 0x3F & codePoint));
			} else {
				throw new ArrayIndexOutOfBoundsException("Failed writing " + c
						+ " at index " + j);
			}
		}
		return j;
	}

	public void writeGroupNoTag(MessageNano value) throws IOException {
		value.writeTo(this);
	}

	public void writeMessageNoTag(MessageNano value) throws IOException {
		writeRawVarint32(value.getCachedSize());
		value.writeTo(this);
	}

	public void writeBytesNoTag(byte[] value) throws IOException {
		writeRawVarint32(value.length);
		writeRawBytes(value);
	}

	public void writeUInt32NoTag(int value) throws IOException {
		writeRawVarint32(value);
	}

	public void writeEnumNoTag(int value) throws IOException {
		writeRawVarint32(value);
	}

	public void writeSFixed32NoTag(int value) throws IOException {
		writeRawLittleEndian32(value);
	}

	public void writeSFixed64NoTag(long value) throws IOException {
		writeRawLittleEndian64(value);
	}

	public void writeSInt32NoTag(int value) throws IOException {
		writeRawVarint32(encodeZigZag32(value));
	}

	public void writeSInt64NoTag(long value) throws IOException {
		writeRawVarint64(encodeZigZag64(value));
	}

	public static int computeDoubleSize(int fieldNumber, double value) {
		return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
	}

	public static int computeFloatSize(int fieldNumber, float value) {
		return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
	}

	public static int computeUInt64Size(int fieldNumber, long value) {
		return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
	}

	public static int computeInt64Size(int fieldNumber, long value) {
		return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
	}

	public static int computeInt32Size(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
	}

	public static int computeFixed64Size(int fieldNumber, long value) {
		return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
	}

	public static int computeFixed32Size(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
	}

	public static int computeBoolSize(int fieldNumber, boolean value) {
		return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
	}

	public static int computeStringSize(int fieldNumber, String value) {
		return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
	}

	public static int computeGroupSize(int fieldNumber, MessageNano value) {
		return computeTagSize(fieldNumber) * 2 + computeGroupSizeNoTag(value);
	}

	public static int computeMessageSize(int fieldNumber, MessageNano value) {
		return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
	}

	public static int computeBytesSize(int fieldNumber, byte[] value) {
		return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
	}

	public static int computeUInt32Size(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
	}

	public static int computeEnumSize(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
	}

	public static int computeSFixed32Size(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
	}

	public static int computeSFixed64Size(int fieldNumber, long value) {
		return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
	}

	public static int computeSInt32Size(int fieldNumber, int value) {
		return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
	}

	public static int computeSInt64Size(int fieldNumber, long value) {
		return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
	}

	public static int computeDoubleSizeNoTag(double value) {
		return 8;
	}

	public static int computeFloatSizeNoTag(float value) {
		return 4;
	}

	public static int computeUInt64SizeNoTag(long value) {
		return computeRawVarint64Size(value);
	}

	public static int computeInt64SizeNoTag(long value) {
		return computeRawVarint64Size(value);
	}

	public static int computeInt32SizeNoTag(int value) {
		if (value >= 0) {
			return computeRawVarint32Size(value);
		}

		return 10;
	}

	public static int computeFixed64SizeNoTag(long value) {
		return 8;
	}

	public static int computeFixed32SizeNoTag(int value) {
		return 4;
	}

	public static int computeBoolSizeNoTag(boolean value) {
		return 1;
	}

	public static int computeStringSizeNoTag(String value) {
		int length = encodedLength(value);
		return computeRawVarint32Size(length) + length;
	}

	public static int computeGroupSizeNoTag(MessageNano value) {
		return value.getSerializedSize();
	}

	public static int computeMessageSizeNoTag(MessageNano value) {
		int size = value.getSerializedSize();
		return computeRawVarint32Size(size) + size;
	}

	public static int computeBytesSizeNoTag(byte[] value) {
		return computeRawVarint32Size(value.length) + value.length;
	}

	public static int computeUInt32SizeNoTag(int value) {
		return computeRawVarint32Size(value);
	}

	public static int computeEnumSizeNoTag(int value) {
		return computeRawVarint32Size(value);
	}

	public static int computeSFixed32SizeNoTag(int value) {
		return 4;
	}

	public static int computeSFixed64SizeNoTag(long value) {
		return 8;
	}

	public static int computeSInt32SizeNoTag(int value) {
		return computeRawVarint32Size(encodeZigZag32(value));
	}

	public static int computeSInt64SizeNoTag(long value) {
		return computeRawVarint64Size(encodeZigZag64(value));
	}

	public int spaceLeft() {
		return this.buffer.remaining();
	}

	public void checkNoSpaceLeft() {
		if (spaceLeft() != 0)
			throw new IllegalStateException(
					"Did not write as much data as expected.");
	}

	public int position() {
		return this.buffer.position();
	}

	public void reset() {
		this.buffer.clear();
	}

	public void writeRawByte(byte value) throws IOException {
		if (!this.buffer.hasRemaining()) {
			throw new OutOfSpaceException(this.buffer.position(),
					this.buffer.limit());
		}

		this.buffer.put(value);
	}

	public void writeRawByte(int value) throws IOException {
		writeRawByte((byte) value);
	}

	public void writeRawBytes(byte[] value) throws IOException {
		writeRawBytes(value, 0, value.length);
	}

	public void writeRawBytes(byte[] value, int offset, int length)
			throws IOException {
		if (this.buffer.remaining() >= length) {
			this.buffer.put(value, offset, length);
		} else
			throw new OutOfSpaceException(this.buffer.position(),
					this.buffer.limit());
	}

	public void writeTag(int fieldNumber, int wireType) throws IOException {
		writeRawVarint32(WireFormatNano.makeTag(fieldNumber, wireType));
	}

	public static int computeTagSize(int fieldNumber) {
		return computeRawVarint32Size(WireFormatNano.makeTag(fieldNumber, 0));
	}

	public void writeRawVarint32(int value) throws IOException {
		while (true) {
			if ((value & 0xFFFFFF80) == 0) {
				writeRawByte(value);
				return;
			}
			writeRawByte(value & 0x7F | 0x80);
			value >>>= 7;
		}
	}

	public static int computeRawVarint32Size(int value) {
		if ((value & 0xFFFFFF80) == 0)
			return 1;
		if ((value & 0xFFFFC000) == 0)
			return 2;
		if ((value & 0xFFE00000) == 0)
			return 3;
		if ((value & 0xF0000000) == 0)
			return 4;
		return 5;
	}

	public void writeRawVarint64(long value) throws IOException {
		while (true) {
			if ((value & 0xFFFFFF80) == 0L) {
				writeRawByte((int) value);
				return;
			}
			writeRawByte((int) value & 0x7F | 0x80);
			value >>>= 7;
		}
	}

	public static int computeRawVarint64Size(long value) {
		if ((value & 0xFFFFFF80) == 0L)
			return 1;
		if ((value & 0xFFFFC000) == 0L)
			return 2;
		if ((value & 0xFFE00000) == 0L)
			return 3;
		if ((value & 0xF0000000) == 0L)
			return 4;
		if ((value & 0x0) == 0L)
			return 5;
		if ((value & 0x0) == 0L)
			return 6;
		if ((value & 0x0) == 0L)
			return 7;
		if ((value & 0x0) == 0L)
			return 8;
		if ((value & 0x0) == 0L)
			return 9;
		return 10;
	}

	public void writeRawLittleEndian32(int value) throws IOException {
		writeRawByte(value & 0xFF);
		writeRawByte(value >> 8 & 0xFF);
		writeRawByte(value >> 16 & 0xFF);
		writeRawByte(value >> 24 & 0xFF);
	}

	public void writeRawLittleEndian64(long value) throws IOException {
		writeRawByte((int) value & 0xFF);
		writeRawByte((int) (value >> 8) & 0xFF);
		writeRawByte((int) (value >> 16) & 0xFF);
		writeRawByte((int) (value >> 24) & 0xFF);
		writeRawByte((int) (value >> 32) & 0xFF);
		writeRawByte((int) (value >> 40) & 0xFF);
		writeRawByte((int) (value >> 48) & 0xFF);
		writeRawByte((int) (value >> 56) & 0xFF);
	}

	public static int encodeZigZag32(int n) {
		return n << 1 ^ n >> 31;
	}

	public static long encodeZigZag64(long n) {
		return n << 1 ^ n >> 63;
	}

	public static class OutOfSpaceException extends IOException {
		private static final long serialVersionUID = -6947486886997889499L;

		OutOfSpaceException(int position, int limit) {
			super();
		}
	}
}
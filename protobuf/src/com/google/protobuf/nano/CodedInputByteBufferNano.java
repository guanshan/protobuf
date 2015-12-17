package com.google.protobuf.nano;

import java.io.IOException;

public final class CodedInputByteBufferNano {
	private final byte[] buffer;
	private int bufferStart;
	private int bufferSize;
	private int bufferSizeAfterLimit;
	private int bufferPos;
	private int lastTag;
	private int currentLimit = 2147483647;
	private int recursionDepth;
	private int recursionLimit = 64;

	private int sizeLimit = 67108864;
	private static final int DEFAULT_RECURSION_LIMIT = 64;
	private static final int DEFAULT_SIZE_LIMIT = 67108864;

	public static CodedInputByteBufferNano newInstance(byte[] buf) {
		return newInstance(buf, 0, buf.length);
	}

	public static CodedInputByteBufferNano newInstance(byte[] buf, int off,
			int len) {
		return new CodedInputByteBufferNano(buf, off, len);
	}

	public int readTag() throws IOException {
		if (isAtEnd()) {
			this.lastTag = 0;
			return 0;
		}

		this.lastTag = readRawVarint32();
		if (this.lastTag == 0) {
			throw InvalidProtocolBufferNanoException.invalidTag();
		}
		return this.lastTag;
	}

	public void checkLastTagWas(int value)
			throws InvalidProtocolBufferNanoException {
		if (this.lastTag != value)
			throw InvalidProtocolBufferNanoException.invalidEndTag();
	}

	public boolean skipField(int tag) throws IOException {
		switch (WireFormatNano.getTagWireType(tag)) {
		case 0:
			readInt32();
			return true;
		case 1:
			readRawLittleEndian64();
			return true;
		case 2:
			skipRawBytes(readRawVarint32());
			return true;
		case 3:
			skipMessage();
			checkLastTagWas(WireFormatNano.makeTag(
					WireFormatNano.getTagFieldNumber(tag), 4));

			return true;
		case 4:
			return false;
		case 5:
			readRawLittleEndian32();
			return true;
		}
		throw InvalidProtocolBufferNanoException.invalidWireType();
	}

	public void skipMessage() throws IOException {
		while (true) {
			int tag = readTag();
			if ((tag == 0) || (!skipField(tag)))
				return;
		}
	}

	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readRawLittleEndian64());
	}

	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readRawLittleEndian32());
	}

	public long readUInt64() throws IOException {
		return readRawVarint64();
	}

	public long readInt64() throws IOException {
		return readRawVarint64();
	}

	public int readInt32() throws IOException {
		return readRawVarint32();
	}

	public long readFixed64() throws IOException {
		return readRawLittleEndian64();
	}

	public int readFixed32() throws IOException {
		return readRawLittleEndian32();
	}

	public boolean readBool() throws IOException {
		return readRawVarint32() != 0;
	}

	public String readString() throws IOException {
		int size = readRawVarint32();
		if ((size <= this.bufferSize - this.bufferPos) && (size > 0)) {
			String result = new String(this.buffer, this.bufferPos, size,
					"UTF-8");
			this.bufferPos += size;
			return result;
		}

		return new String(readRawBytes(size), "UTF-8");
	}

	public void readGroup(MessageNano msg, int fieldNumber) throws IOException {
		if (this.recursionDepth >= this.recursionLimit) {
			throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
		}
		this.recursionDepth += 1;
		msg.mergeFrom(this);
		checkLastTagWas(WireFormatNano.makeTag(fieldNumber, 4));

		this.recursionDepth -= 1;
	}

	public void readMessage(MessageNano msg) throws IOException {
		int length = readRawVarint32();
		if (this.recursionDepth >= this.recursionLimit) {
			throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
		}
		int oldLimit = pushLimit(length);
		this.recursionDepth += 1;
		msg.mergeFrom(this);
		checkLastTagWas(0);
		this.recursionDepth -= 1;
		popLimit(oldLimit);
	}

	public byte[] readBytes() throws IOException {
		int size = readRawVarint32();
		if ((size <= this.bufferSize - this.bufferPos) && (size > 0)) {
			byte[] result = new byte[size];
			System.arraycopy(this.buffer, this.bufferPos, result, 0, size);
			this.bufferPos += size;
			return result;
		}

		return readRawBytes(size);
	}

	public int readUInt32() throws IOException {
		return readRawVarint32();
	}

	public int readEnum() throws IOException {
		return readRawVarint32();
	}

	public int readSFixed32() throws IOException {
		return readRawLittleEndian32();
	}

	public long readSFixed64() throws IOException {
		return readRawLittleEndian64();
	}

	public int readSInt32() throws IOException {
		return decodeZigZag32(readRawVarint32());
	}

	public long readSInt64() throws IOException {
		return decodeZigZag64(readRawVarint64());
	}

	public int readRawVarint32() throws IOException {
		byte tmp = readRawByte();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7F;
		if ((tmp = readRawByte()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7F) << 7;
			if ((tmp = readRawByte()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7F) << 14;
				if ((tmp = readRawByte()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7F) << 21;
					result |= (tmp = readRawByte()) << 28;
					if (tmp < 0) {
						for (int i = 0; i < 5; i++) {
							if (readRawByte() >= 0) {
								return result;
							}
						}
						throw InvalidProtocolBufferNanoException
								.malformedVarint();
					}
				}
			}
		}
		return result;
	}

	public long readRawVarint64() throws IOException {
		int shift = 0;
		long result = 0L;
		while (shift < 64) {
			byte b = readRawByte();
			result |= (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				return result;
			}
			shift += 7;
		}
		throw InvalidProtocolBufferNanoException.malformedVarint();
	}

	public int readRawLittleEndian32() throws IOException {
		byte b1 = readRawByte();
		byte b2 = readRawByte();
		byte b3 = readRawByte();
		byte b4 = readRawByte();
		return b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16
				| (b4 & 0xFF) << 24;
	}

	public long readRawLittleEndian64() throws IOException {
		byte b1 = readRawByte();
		byte b2 = readRawByte();
		byte b3 = readRawByte();
		byte b4 = readRawByte();
		byte b5 = readRawByte();
		byte b6 = readRawByte();
		byte b7 = readRawByte();
		byte b8 = readRawByte();
		return b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16
				| (b4 & 0xFF) << 24 | (b5 & 0xFF) << 32 | (b6 & 0xFF) << 40
				| (b7 & 0xFF) << 48 | (b8 & 0xFF) << 56;
	}

	public static int decodeZigZag32(int n) {
		return n >>> 1 ^ -(n & 0x1);
	}

	public static long decodeZigZag64(long n) {
		return n >>> 1 ^ -(n & 1L);
	}

	private CodedInputByteBufferNano(byte[] buffer, int off, int len) {
		this.buffer = buffer;
		this.bufferStart = off;
		this.bufferSize = (off + len);
		this.bufferPos = off;
	}

	public int setRecursionLimit(int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException(
					"Recursion limit cannot be negative: " + limit);
		}

		int oldLimit = this.recursionLimit;
		this.recursionLimit = limit;
		return oldLimit;
	}

	public int setSizeLimit(int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException(
					"Size limit cannot be negative: " + limit);
		}

		int oldLimit = this.sizeLimit;
		this.sizeLimit = limit;
		return oldLimit;
	}

	public void resetSizeCounter() {
	}

	public int pushLimit(int byteLimit)
			throws InvalidProtocolBufferNanoException {
		if (byteLimit < 0) {
			throw InvalidProtocolBufferNanoException.negativeSize();
		}
		byteLimit += this.bufferPos;
		int oldLimit = this.currentLimit;
		if (byteLimit > oldLimit) {
			throw InvalidProtocolBufferNanoException.truncatedMessage();
		}
		this.currentLimit = byteLimit;

		recomputeBufferSizeAfterLimit();

		return oldLimit;
	}

	private void recomputeBufferSizeAfterLimit() {
		this.bufferSize += this.bufferSizeAfterLimit;
		int bufferEnd = this.bufferSize;
		if (bufferEnd > this.currentLimit) {
			this.bufferSizeAfterLimit = (bufferEnd - this.currentLimit);
			this.bufferSize -= this.bufferSizeAfterLimit;
		} else {
			this.bufferSizeAfterLimit = 0;
		}
	}

	public void popLimit(int oldLimit) {
		this.currentLimit = oldLimit;
		recomputeBufferSizeAfterLimit();
	}

	public int getBytesUntilLimit() {
		if (this.currentLimit == 2147483647) {
			return -1;
		}

		int currentAbsolutePosition = this.bufferPos;
		return this.currentLimit - currentAbsolutePosition;
	}

	public boolean isAtEnd() {
		return this.bufferPos == this.bufferSize;
	}

	public int getPosition() {
		return this.bufferPos - this.bufferStart;
	}

	public byte[] getData(int offset, int length) {
		if (length == 0) {
			return WireFormatNano.EMPTY_BYTES;
		}
		byte[] copy = new byte[length];
		int start = this.bufferStart + offset;
		System.arraycopy(this.buffer, start, copy, 0, length);
		return copy;
	}

	public void rewindToPosition(int position) {
		if (position > this.bufferPos - this.bufferStart) {
			throw new IllegalArgumentException("Position " + position
					+ " is beyond current "
					+ (this.bufferPos - this.bufferStart));
		}

		if (position < 0) {
			throw new IllegalArgumentException("Bad position " + position);
		}
		this.bufferPos = (this.bufferStart + position);
	}

	public byte readRawByte() throws IOException {
		if (this.bufferPos == this.bufferSize) {
			throw InvalidProtocolBufferNanoException.truncatedMessage();
		}
		return this.buffer[(this.bufferPos++)];
	}

	public byte[] readRawBytes(int size) throws IOException {
		if (size < 0) {
			throw InvalidProtocolBufferNanoException.negativeSize();
		}

		if (this.bufferPos + size > this.currentLimit) {
			skipRawBytes(this.currentLimit - this.bufferPos);

			throw InvalidProtocolBufferNanoException.truncatedMessage();
		}

		if (size <= this.bufferSize - this.bufferPos) {
			byte[] bytes = new byte[size];
			System.arraycopy(this.buffer, this.bufferPos, bytes, 0, size);
			this.bufferPos += size;
			return bytes;
		}
		throw InvalidProtocolBufferNanoException.truncatedMessage();
	}

	public void skipRawBytes(int size) throws IOException {
		if (size < 0) {
			throw InvalidProtocolBufferNanoException.negativeSize();
		}

		if (this.bufferPos + size > this.currentLimit) {
			skipRawBytes(this.currentLimit - this.bufferPos);

			throw InvalidProtocolBufferNanoException.truncatedMessage();
		}

		if (size <= this.bufferSize - this.bufferPos) {
			this.bufferPos += size;
		} else
			throw InvalidProtocolBufferNanoException.truncatedMessage();
	}
}
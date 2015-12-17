package com.google.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

final class UnknownFieldData {
	final int tag;
	final byte[] bytes;

	UnknownFieldData(int tag, byte[] bytes) {
		this.tag = tag;
		this.bytes = bytes;
	}

	int computeSerializedSize() {
		int size = 0;
		size += CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
		size += this.bytes.length;
		return size;
	}

	void writeTo(CodedOutputByteBufferNano output) throws IOException {
		output.writeRawVarint32(this.tag);
		output.writeRawBytes(this.bytes);
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UnknownFieldData)) {
			return false;
		}

		UnknownFieldData other = (UnknownFieldData) o;
		return (this.tag == other.tag)
				&& (Arrays.equals(this.bytes, other.bytes));
	}

	public int hashCode() {
		int result = 17;
		result = 31 * result + this.tag;
		result = 31 * result + Arrays.hashCode(this.bytes);
		return result;
	}
}
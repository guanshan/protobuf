package com.google.protobuf.nano;

import java.io.IOException;

public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>>
		extends MessageNano {
	protected FieldArray unknownFieldData;

	protected int computeSerializedSize() {
		int size = 0;
		if (this.unknownFieldData != null) {
			for (int i = 0; i < this.unknownFieldData.size(); i++) {
				FieldData field = this.unknownFieldData.dataAt(i);
				size += field.computeSerializedSize();
			}
		}
		return size;
	}

	public void writeTo(CodedOutputByteBufferNano output) throws IOException {
		if (this.unknownFieldData == null) {
			return;
		}
		for (int i = 0; i < this.unknownFieldData.size(); i++) {
			FieldData field = this.unknownFieldData.dataAt(i);
			field.writeTo(output);
		}
	}

	public final boolean hasExtension(Extension<M, ?> extension) {
		if (this.unknownFieldData == null) {
			return false;
		}
		FieldData field = this.unknownFieldData.get(WireFormatNano
				.getTagFieldNumber(extension.tag));
		return field != null;
	}

	public final <T> T getExtension(Extension<M, T> extension) {
		if (this.unknownFieldData == null) {
			return null;
		}
		FieldData field = this.unknownFieldData.get(WireFormatNano
				.getTagFieldNumber(extension.tag));
		return field == null ? null : field.getValue(extension);
	}

	public final <T> M setExtension(Extension<M, T> extension, T value) {
		int fieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
		if (value == null) {
			if (this.unknownFieldData != null) {
				this.unknownFieldData.remove(fieldNumber);
				if (this.unknownFieldData.isEmpty())
					this.unknownFieldData = null;
			}
		} else {
			FieldData field = null;
			if (this.unknownFieldData == null)
				this.unknownFieldData = new FieldArray();
			else {
				field = this.unknownFieldData.get(fieldNumber);
			}
			if (field == null)
				this.unknownFieldData.put(fieldNumber, new FieldData(extension,
						value));
			else {
				field.setValue(extension, value);
			}

		}

		ExtendableMessageNano typedThis = this;
		return typedThis;
	}

	protected final boolean storeUnknownField(CodedInputByteBufferNano input,
			int tag) throws IOException {
		int startPos = input.getPosition();
		if (!input.skipField(tag)) {
			return false;
		}
		int fieldNumber = WireFormatNano.getTagFieldNumber(tag);
		int endPos = input.getPosition();
		byte[] bytes = input.getData(startPos, endPos - startPos);
		UnknownFieldData unknownField = new UnknownFieldData(tag, bytes);

		FieldData field = null;
		if (this.unknownFieldData == null)
			this.unknownFieldData = new FieldArray();
		else {
			field = this.unknownFieldData.get(fieldNumber);
		}
		if (field == null) {
			field = new FieldData();
			this.unknownFieldData.put(fieldNumber, field);
		}
		field.addUnknownField(unknownField);
		return true;
	}

	public M clone() throws CloneNotSupportedException {
		ExtendableMessageNano cloned = (ExtendableMessageNano) super.clone();
		InternalNano.cloneUnknownFieldData(this, cloned);
		return cloned;
	}
}
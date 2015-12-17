package com.google.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;

import java.lang.reflect.Array;

public final class ParcelableMessageNanoCreator<T extends MessageNano>
		implements Parcelable.Creator<T> {
	private static final String TAG = "PMNCreator";
	private final Class<T> mClazz;

	public ParcelableMessageNanoCreator(Class<T> clazz) {
		this.mClazz = clazz;
	}

	public T createFromParcel(Parcel in) {
		String className = in.readString();
		byte[] data = in.createByteArray();

		MessageNano proto = null;
		try {
			Class clazz = Class.forName(className);
			Object instance = clazz.newInstance();
			proto = (MessageNano) instance;
			MessageNano.mergeFrom(proto, data);
		} catch (ClassNotFoundException e) {
			Log.e("PMNCreator", "Exception trying to create proto from parcel",
					e);
		} catch (IllegalAccessException e) {
			Log.e("PMNCreator", "Exception trying to create proto from parcel",
					e);
		} catch (InstantiationException e) {
			Log.e("PMNCreator", "Exception trying to create proto from parcel",
					e);
		} catch (InvalidProtocolBufferNanoException e) {
			Log.e("PMNCreator", "Exception trying to create proto from parcel",
					e);
		}

		return proto;
	}

	public T[] newArray(int i) {
		return (MessageNano[]) Array.newInstance(this.mClazz, i);
	}

	static <T extends MessageNano> void writeToParcel(Class<T> clazz,
			MessageNano message, Parcel out) {
		out.writeString(clazz.getName());
		out.writeByteArray(MessageNano.toByteArray(message));
	}
}
package com.google.protobuf.nano;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public final class InternalNano {
	public static final Object LAZY_INIT_LOCK = new Object();

	public static String stringDefaultValue(String bytes) {
		try {
			return new String(bytes.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"Java VM does not support a standard character set.", e);
		}
	}

	public static byte[] bytesDefaultValue(String bytes) {
		try {
			return bytes.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"Java VM does not support a standard character set.", e);
		}
	}

	public static byte[] copyFromUtf8(String text) {
		try {
			return text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		throw new RuntimeException("UTF-8 not supported?");
	}

	public static boolean equals(int[] field1, int[] field2) {
		if ((field1 == null) || (field1.length == 0)) {
			return (field2 == null) || (field2.length == 0);
		}
		return Arrays.equals(field1, field2);
	}

	public static boolean equals(long[] field1, long[] field2) {
		if ((field1 == null) || (field1.length == 0)) {
			return (field2 == null) || (field2.length == 0);
		}
		return Arrays.equals(field1, field2);
	}

	public static boolean equals(float[] field1, float[] field2) {
		if ((field1 == null) || (field1.length == 0)) {
			return (field2 == null) || (field2.length == 0);
		}
		return Arrays.equals(field1, field2);
	}

	public static boolean equals(double[] field1, double[] field2) {
		if ((field1 == null) || (field1.length == 0)) {
			return (field2 == null) || (field2.length == 0);
		}
		return Arrays.equals(field1, field2);
	}

	public static boolean equals(boolean[] field1, boolean[] field2) {
		if ((field1 == null) || (field1.length == 0)) {
			return (field2 == null) || (field2.length == 0);
		}
		return Arrays.equals(field1, field2);
	}

	public static boolean equals(byte[][] field1, byte[][] field2) {
		int index1 = 0;
		int length1 = field1 == null ? 0 : field1.length;
		int index2 = 0;
		int length2 = field2 == null ? 0 : field2.length;
		while (true)
			if ((index1 < length1) && (field1[index1] == null)) {
				index1++;
			} else {
				while ((index2 < length2) && (field2[index2] == null)) {
					index2++;
				}
				boolean atEndOf1 = index1 >= length1;
				boolean atEndOf2 = index2 >= length2;
				if ((atEndOf1) && (atEndOf2)) {
					return true;
				}
				if (atEndOf1 != atEndOf2) {
					return false;
				}
				if (!Arrays.equals(field1[index1], field2[index2])) {
					return false;
				}
				index1++;
				index2++;
			}
	}

	public static boolean equals(Object[] field1, Object[] field2) {
		int index1 = 0;
		int length1 = field1 == null ? 0 : field1.length;
		int index2 = 0;
		int length2 = field2 == null ? 0 : field2.length;
		while (true)
			if ((index1 < length1) && (field1[index1] == null)) {
				index1++;
			} else {
				while ((index2 < length2) && (field2[index2] == null)) {
					index2++;
				}
				boolean atEndOf1 = index1 >= length1;
				boolean atEndOf2 = index2 >= length2;
				if ((atEndOf1) && (atEndOf2)) {
					return true;
				}
				if (atEndOf1 != atEndOf2) {
					return false;
				}
				if (!field1[index1].equals(field2[index2])) {
					return false;
				}
				index1++;
				index2++;
			}
	}

	public static int hashCode(int[] field) {
		return (field == null) || (field.length == 0) ? 0 : Arrays
				.hashCode(field);
	}

	public static int hashCode(long[] field) {
		return (field == null) || (field.length == 0) ? 0 : Arrays
				.hashCode(field);
	}

	public static int hashCode(float[] field) {
		return (field == null) || (field.length == 0) ? 0 : Arrays
				.hashCode(field);
	}

	public static int hashCode(double[] field) {
		return (field == null) || (field.length == 0) ? 0 : Arrays
				.hashCode(field);
	}

	public static int hashCode(boolean[] field) {
		return (field == null) || (field.length == 0) ? 0 : Arrays
				.hashCode(field);
	}

	public static int hashCode(byte[][] field) {
		int result = 0;
		int i = 0;
		for (int size = field == null ? 0 : field.length; i < size; i++) {
			byte[] element = field[i];
			if (element != null) {
				result = 31 * result + Arrays.hashCode(element);
			}
		}
		return result;
	}

	public static int hashCode(Object[] field) {
		int result = 0;
		int i = 0;
		for (int size = field == null ? 0 : field.length; i < size; i++) {
			Object element = field[i];
			if (element != null) {
				result = 31 * result + element.hashCode();
			}
		}
		return result;
	}

	public static void cloneUnknownFieldData(ExtendableMessageNano original,
			ExtendableMessageNano cloned) {
		if (original.unknownFieldData != null)
			cloned.unknownFieldData = original.unknownFieldData.clone();
	}
}
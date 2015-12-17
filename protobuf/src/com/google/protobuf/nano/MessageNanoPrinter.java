package com.google.protobuf.nano;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MessageNanoPrinter {
	private static final String INDENT = "  ";
	private static final int MAX_STRING_LEN = 200;

	public static <T extends MessageNano> String print(T message) {
		if (message == null) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		try {
			print(null, message, new StringBuffer(), buf);
		} catch (IllegalAccessException e) {
			return new StringBuilder().append("Error printing proto: ")
					.append(e.getMessage()).toString();
		} catch (InvocationTargetException e) {
			return new StringBuilder().append("Error printing proto: ")
					.append(e.getMessage()).toString();
		}
		return buf.toString();
	}

	private static void print(String identifier, Object object,
			StringBuffer indentBuf, StringBuffer buf)
			throws IllegalAccessException, InvocationTargetException {
		if (object != null) {
			if ((object instanceof MessageNano)) {
				int origIndentBufLength = indentBuf.length();
				if (identifier != null) {
					buf.append(indentBuf).append(deCamelCaseify(identifier))
							.append(" <\n");
					indentBuf.append("  ");
				}
				Class clazz = object.getClass();

				for (Field field : clazz.getFields()) {
					int modifiers = field.getModifiers();
					String fieldName = field.getName();
					if (!"cachedSize".equals(fieldName)) {
						if (((modifiers & 0x1) == 1)
								&& ((modifiers & 0x8) != 8)
								&& (!fieldName.startsWith("_"))
								&& (!fieldName.endsWith("_"))) {
							Class fieldType = field.getType();
							Object value = field.get(object);

							if (fieldType.isArray()) {
								Class arrayType = fieldType.getComponentType();

								if (arrayType == Byte.TYPE) {
									print(fieldName, value, indentBuf, buf);
								} else {
									int len = value == null ? 0 : Array
											.getLength(value);
									for (int i = 0; i < len; i++) {
										Object elem = Array.get(value, i);
										print(fieldName, elem, indentBuf, buf);
									}
								}
							} else {
								print(fieldName, value, indentBuf, buf);
							}
						}

					}

				}

				for (Method method : clazz.getMethods()) {
					String name = method.getName();

					if (name.startsWith("set")) {
						String subfieldName = name.substring(3);

						Method hazzer = null;
						try {
							hazzer = clazz.getMethod(new StringBuilder()
									.append("has").append(subfieldName)
									.toString(), new Class[0]);
						} catch (NoSuchMethodException e) {
							continue;
						}

						if (((Boolean) hazzer.invoke(object, new Object[0]))
								.booleanValue()) {
							Method getter = null;
							try {
								getter = clazz.getMethod(new StringBuilder()
										.append("get").append(subfieldName)
										.toString(), new Class[0]);
							} catch (NoSuchMethodException e) {
								continue;
							}

							print(subfieldName,
									getter.invoke(object, new Object[0]),
									indentBuf, buf);
						}
					}
				}
				if (identifier != null) {
					indentBuf.setLength(origIndentBufLength);
					buf.append(indentBuf).append(">\n");
				}
			} else {
				identifier = deCamelCaseify(identifier);
				buf.append(indentBuf).append(identifier).append(": ");
				if ((object instanceof String)) {
					String stringMessage = sanitizeString((String) object);
					buf.append("\"").append(stringMessage).append("\"");
				} else if ((object instanceof byte[])) {
					appendQuotedBytes((byte[]) object, buf);
				} else {
					buf.append(object);
				}
				buf.append("\n");
			}
		}
	}

	private static String deCamelCaseify(String identifier) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < identifier.length(); i++) {
			char currentChar = identifier.charAt(i);
			if (i == 0)
				out.append(Character.toLowerCase(currentChar));
			else if (Character.isUpperCase(currentChar))
				out.append('_').append(Character.toLowerCase(currentChar));
			else {
				out.append(currentChar);
			}
		}
		return out.toString();
	}

	private static String sanitizeString(String str) {
		if ((!str.startsWith("http")) && (str.length() > 200)) {
			str = new StringBuilder().append(str.substring(0, 200))
					.append("[...]").toString();
		}
		return escapeString(str);
	}

	private static String escapeString(String str) {
		int strLen = str.length();
		StringBuilder b = new StringBuilder(strLen);
		for (int i = 0; i < strLen; i++) {
			char original = str.charAt(i);
			if ((original >= ' ') && (original <= '~') && (original != '"')
					&& (original != '\''))
				b.append(original);
			else {
				b.append(String.format("\\u%04x",
						new Object[] { Integer.valueOf(original) }));
			}
		}
		return b.toString();
	}

	private static void appendQuotedBytes(byte[] bytes, StringBuffer builder) {
		if (bytes == null) {
			builder.append("\"\"");
			return;
		}

		builder.append('"');
		for (int i = 0; i < bytes.length; i++) {
			int ch = bytes[i] & 0xFF;
			if ((ch == 92) || (ch == 34))
				builder.append('\\').append((char) ch);
			else if ((ch >= 32) && (ch < 127))
				builder.append((char) ch);
			else {
				builder.append(String.format("\\%03o",
						new Object[] { Integer.valueOf(ch) }));
			}
		}
		builder.append('"');
	}
}
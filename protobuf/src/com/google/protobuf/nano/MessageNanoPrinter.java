/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public final class MessageNanoPrinter
/*     */ {
/*     */   private static final String INDENT = "  ";
/*     */   private static final int MAX_STRING_LEN = 200;
/*     */ 
/*     */   public static <T extends MessageNano> String print(T message)
/*     */   {
/*  61 */     if (message == null) {
/*  62 */       return "";
/*     */     }
/*     */ 
/*  65 */     StringBuffer buf = new StringBuffer();
/*     */     try {
/*  67 */       print(null, message, new StringBuffer(), buf);
/*     */     } catch (IllegalAccessException e) {
/*  69 */       return new StringBuilder().append("Error printing proto: ").append(e.getMessage()).toString();
/*     */     } catch (InvocationTargetException e) {
/*  71 */       return new StringBuilder().append("Error printing proto: ").append(e.getMessage()).toString();
/*     */     }
/*  73 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static void print(String identifier, Object object, StringBuffer indentBuf, StringBuffer buf)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/*  90 */     if (object != null)
/*     */     {
/*  96 */       if ((object instanceof MessageNano)) {
/*  97 */         int origIndentBufLength = indentBuf.length();
/*  98 */         if (identifier != null) {
/*  99 */           buf.append(indentBuf).append(deCamelCaseify(identifier)).append(" <\n");
/* 100 */           indentBuf.append("  ");
/*     */         }
/* 102 */         Class clazz = object.getClass();
/*     */ 
/* 108 */         for (Field field : clazz.getFields()) {
/* 109 */           int modifiers = field.getModifiers();
/* 110 */           String fieldName = field.getName();
/* 111 */           if (!"cachedSize".equals(fieldName))
/*     */           {
/* 116 */             if (((modifiers & 0x1) == 1) && ((modifiers & 0x8) != 8) && (!fieldName.startsWith("_")) && (!fieldName.endsWith("_")))
/*     */             {
/* 120 */               Class fieldType = field.getType();
/* 121 */               Object value = field.get(object);
/*     */ 
/* 123 */               if (fieldType.isArray()) {
/* 124 */                 Class arrayType = fieldType.getComponentType();
/*     */ 
/* 127 */                 if (arrayType == Byte.TYPE) {
/* 128 */                   print(fieldName, value, indentBuf, buf);
/*     */                 } else {
/* 130 */                   int len = value == null ? 0 : Array.getLength(value);
/* 131 */                   for (int i = 0; i < len; i++) {
/* 132 */                     Object elem = Array.get(value, i);
/* 133 */                     print(fieldName, elem, indentBuf, buf);
/*     */                   }
/*     */                 }
/*     */               } else {
/* 137 */                 print(fieldName, value, indentBuf, buf);
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 145 */         for (Method method : clazz.getMethods()) {
/* 146 */           String name = method.getName();
/*     */ 
/* 149 */           if (name.startsWith("set")) {
/* 150 */             String subfieldName = name.substring(3);
/*     */ 
/* 152 */             Method hazzer = null;
/*     */             try {
/* 154 */               hazzer = clazz.getMethod(new StringBuilder().append("has").append(subfieldName).toString(), new Class[0]);
/*     */             } catch (NoSuchMethodException e) {
/* 156 */               continue;
/*     */             }
/*     */ 
/* 159 */             if (((Boolean)hazzer.invoke(object, new Object[0])).booleanValue())
/*     */             {
/* 163 */               Method getter = null;
/*     */               try {
/* 165 */                 getter = clazz.getMethod(new StringBuilder().append("get").append(subfieldName).toString(), new Class[0]);
/*     */               } catch (NoSuchMethodException e) {
/* 167 */                 continue;
/*     */               }
/*     */ 
/* 170 */               print(subfieldName, getter.invoke(object, new Object[0]), indentBuf, buf);
/*     */             }
/*     */           }
/*     */         }
/* 173 */         if (identifier != null) {
/* 174 */           indentBuf.setLength(origIndentBufLength);
/* 175 */           buf.append(indentBuf).append(">\n");
/*     */         }
/*     */       }
/*     */       else {
/* 179 */         identifier = deCamelCaseify(identifier);
/* 180 */         buf.append(indentBuf).append(identifier).append(": ");
/* 181 */         if ((object instanceof String)) {
/* 182 */           String stringMessage = sanitizeString((String)object);
/* 183 */           buf.append("\"").append(stringMessage).append("\"");
/* 184 */         } else if ((object instanceof byte[])) {
/* 185 */           appendQuotedBytes((byte[])object, buf);
/*     */         } else {
/* 187 */           buf.append(object);
/*     */         }
/* 189 */         buf.append("\n");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String deCamelCaseify(String identifier)
/*     */   {
/* 197 */     StringBuffer out = new StringBuffer();
/* 198 */     for (int i = 0; i < identifier.length(); i++) {
/* 199 */       char currentChar = identifier.charAt(i);
/* 200 */       if (i == 0)
/* 201 */         out.append(Character.toLowerCase(currentChar));
/* 202 */       else if (Character.isUpperCase(currentChar))
/* 203 */         out.append('_').append(Character.toLowerCase(currentChar));
/*     */       else {
/* 205 */         out.append(currentChar);
/*     */       }
/*     */     }
/* 208 */     return out.toString();
/*     */   }
/*     */ 
/*     */   private static String sanitizeString(String str)
/*     */   {
/* 215 */     if ((!str.startsWith("http")) && (str.length() > 200))
/*     */     {
/* 217 */       str = new StringBuilder().append(str.substring(0, 200)).append("[...]").toString();
/*     */     }
/* 219 */     return escapeString(str);
/*     */   }
/*     */ 
/*     */   private static String escapeString(String str)
/*     */   {
/* 226 */     int strLen = str.length();
/* 227 */     StringBuilder b = new StringBuilder(strLen);
/* 228 */     for (int i = 0; i < strLen; i++) {
/* 229 */       char original = str.charAt(i);
/* 230 */       if ((original >= ' ') && (original <= '~') && (original != '"') && (original != '\''))
/* 231 */         b.append(original);
/*     */       else {
/* 233 */         b.append(String.format("\\u%04x", new Object[] { Integer.valueOf(original) }));
/*     */       }
/*     */     }
/* 236 */     return b.toString();
/*     */   }
/*     */ 
/*     */   private static void appendQuotedBytes(byte[] bytes, StringBuffer builder)
/*     */   {
/* 243 */     if (bytes == null) {
/* 244 */       builder.append("\"\"");
/* 245 */       return;
/*     */     }
/*     */ 
/* 248 */     builder.append('"');
/* 249 */     for (int i = 0; i < bytes.length; i++) {
/* 250 */       int ch = bytes[i] & 0xFF;
/* 251 */       if ((ch == 92) || (ch == 34))
/* 252 */         builder.append('\\').append((char)ch);
/* 253 */       else if ((ch >= 32) && (ch < 127))
/* 254 */         builder.append((char)ch);
/*     */       else {
/* 256 */         builder.append(String.format("\\%03o", new Object[] { Integer.valueOf(ch) }));
/*     */       }
/*     */     }
/* 259 */     builder.append('"');
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.MessageNanoPrinter
 * JD-Core Version:    0.6.2
 */
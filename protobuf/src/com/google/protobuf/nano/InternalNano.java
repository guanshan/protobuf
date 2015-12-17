/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class InternalNano
/*     */ {
/*  58 */   public static final Object LAZY_INIT_LOCK = new Object();
/*     */ 
/*     */   public static String stringDefaultValue(String bytes)
/*     */   {
/*     */     try
/*     */     {
/*  90 */       return new String(bytes.getBytes("ISO-8859-1"), "UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/*  94 */       throw new IllegalStateException("Java VM does not support a standard character set.", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] bytesDefaultValue(String bytes)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       return bytes.getBytes("ISO-8859-1");
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 113 */       throw new IllegalStateException("Java VM does not support a standard character set.", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] copyFromUtf8(String text)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       return text.getBytes("UTF-8"); } catch (UnsupportedEncodingException e) {
/*     */     }
/* 126 */     throw new RuntimeException("UTF-8 not supported?");
/*     */   }
/*     */ 
/*     */   public static boolean equals(int[] field1, int[] field2)
/*     */   {
/* 135 */     if ((field1 == null) || (field1.length == 0)) {
/* 136 */       return (field2 == null) || (field2.length == 0);
/*     */     }
/* 138 */     return Arrays.equals(field1, field2);
/*     */   }
/*     */ 
/*     */   public static boolean equals(long[] field1, long[] field2)
/*     */   {
/* 147 */     if ((field1 == null) || (field1.length == 0)) {
/* 148 */       return (field2 == null) || (field2.length == 0);
/*     */     }
/* 150 */     return Arrays.equals(field1, field2);
/*     */   }
/*     */ 
/*     */   public static boolean equals(float[] field1, float[] field2)
/*     */   {
/* 159 */     if ((field1 == null) || (field1.length == 0)) {
/* 160 */       return (field2 == null) || (field2.length == 0);
/*     */     }
/* 162 */     return Arrays.equals(field1, field2);
/*     */   }
/*     */ 
/*     */   public static boolean equals(double[] field1, double[] field2)
/*     */   {
/* 171 */     if ((field1 == null) || (field1.length == 0)) {
/* 172 */       return (field2 == null) || (field2.length == 0);
/*     */     }
/* 174 */     return Arrays.equals(field1, field2);
/*     */   }
/*     */ 
/*     */   public static boolean equals(boolean[] field1, boolean[] field2)
/*     */   {
/* 183 */     if ((field1 == null) || (field1.length == 0)) {
/* 184 */       return (field2 == null) || (field2.length == 0);
/*     */     }
/* 186 */     return Arrays.equals(field1, field2);
/*     */   }
/*     */ 
/*     */   public static boolean equals(byte[][] field1, byte[][] field2)
/*     */   {
/* 197 */     int index1 = 0;
/* 198 */     int length1 = field1 == null ? 0 : field1.length;
/* 199 */     int index2 = 0;
/* 200 */     int length2 = field2 == null ? 0 : field2.length;
/*     */     while (true)
/* 202 */       if ((index1 < length1) && (field1[index1] == null)) {
/* 203 */         index1++;
/*     */       } else {
/* 205 */         while ((index2 < length2) && (field2[index2] == null)) {
/* 206 */           index2++;
/*     */         }
/* 208 */         boolean atEndOf1 = index1 >= length1;
/* 209 */         boolean atEndOf2 = index2 >= length2;
/* 210 */         if ((atEndOf1) && (atEndOf2))
/*     */         {
/* 212 */           return true;
/* 213 */         }if (atEndOf1 != atEndOf2)
/*     */         {
/* 215 */           return false;
/* 216 */         }if (!Arrays.equals(field1[index1], field2[index2]))
/*     */         {
/* 218 */           return false;
/*     */         }
/* 220 */         index1++;
/* 221 */         index2++;
/*     */       }
/*     */   }
/*     */ 
/*     */   public static boolean equals(Object[] field1, Object[] field2)
/*     */   {
/* 232 */     int index1 = 0;
/* 233 */     int length1 = field1 == null ? 0 : field1.length;
/* 234 */     int index2 = 0;
/* 235 */     int length2 = field2 == null ? 0 : field2.length;
/*     */     while (true)
/* 237 */       if ((index1 < length1) && (field1[index1] == null)) {
/* 238 */         index1++;
/*     */       } else {
/* 240 */         while ((index2 < length2) && (field2[index2] == null)) {
/* 241 */           index2++;
/*     */         }
/* 243 */         boolean atEndOf1 = index1 >= length1;
/* 244 */         boolean atEndOf2 = index2 >= length2;
/* 245 */         if ((atEndOf1) && (atEndOf2))
/*     */         {
/* 247 */           return true;
/* 248 */         }if (atEndOf1 != atEndOf2)
/*     */         {
/* 250 */           return false;
/* 251 */         }if (!field1[index1].equals(field2[index2]))
/*     */         {
/* 253 */           return false;
/*     */         }
/* 255 */         index1++;
/* 256 */         index2++;
/*     */       }
/*     */   }
/*     */ 
/*     */   public static int hashCode(int[] field)
/*     */   {
/* 265 */     return (field == null) || (field.length == 0) ? 0 : Arrays.hashCode(field);
/*     */   }
/*     */ 
/*     */   public static int hashCode(long[] field)
/*     */   {
/* 273 */     return (field == null) || (field.length == 0) ? 0 : Arrays.hashCode(field);
/*     */   }
/*     */ 
/*     */   public static int hashCode(float[] field)
/*     */   {
/* 281 */     return (field == null) || (field.length == 0) ? 0 : Arrays.hashCode(field);
/*     */   }
/*     */ 
/*     */   public static int hashCode(double[] field)
/*     */   {
/* 289 */     return (field == null) || (field.length == 0) ? 0 : Arrays.hashCode(field);
/*     */   }
/*     */ 
/*     */   public static int hashCode(boolean[] field)
/*     */   {
/* 297 */     return (field == null) || (field.length == 0) ? 0 : Arrays.hashCode(field);
/*     */   }
/*     */ 
/*     */   public static int hashCode(byte[][] field)
/*     */   {
/* 306 */     int result = 0;
/* 307 */     int i = 0; for (int size = field == null ? 0 : field.length; i < size; i++) {
/* 308 */       byte[] element = field[i];
/* 309 */       if (element != null) {
/* 310 */         result = 31 * result + Arrays.hashCode(element);
/*     */       }
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */   public static int hashCode(Object[] field)
/*     */   {
/* 323 */     int result = 0;
/* 324 */     int i = 0; for (int size = field == null ? 0 : field.length; i < size; i++) {
/* 325 */       Object element = field[i];
/* 326 */       if (element != null) {
/* 327 */         result = 31 * result + element.hashCode();
/*     */       }
/*     */     }
/* 330 */     return result;
/*     */   }
/*     */ 
/*     */   public static void cloneUnknownFieldData(ExtendableMessageNano original, ExtendableMessageNano cloned)
/*     */   {
/* 336 */     if (original.unknownFieldData != null)
/* 337 */       cloned.unknownFieldData = original.unknownFieldData.clone();
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.InternalNano
 * JD-Core Version:    0.6.2
 */
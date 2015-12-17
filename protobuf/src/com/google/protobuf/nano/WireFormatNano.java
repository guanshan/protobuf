/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class WireFormatNano
/*     */ {
/*     */   static final int WIRETYPE_VARINT = 0;
/*     */   static final int WIRETYPE_FIXED64 = 1;
/*     */   static final int WIRETYPE_LENGTH_DELIMITED = 2;
/*     */   static final int WIRETYPE_START_GROUP = 3;
/*     */   static final int WIRETYPE_END_GROUP = 4;
/*     */   static final int WIRETYPE_FIXED32 = 5;
/*     */   static final int TAG_TYPE_BITS = 3;
/*     */   static final int TAG_TYPE_MASK = 7;
/*  75 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/*  76 */   public static final long[] EMPTY_LONG_ARRAY = new long[0];
/*  77 */   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
/*  78 */   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
/*  79 */   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/*  80 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*  81 */   public static final byte[][] EMPTY_BYTES_ARRAY = new byte[0][];
/*  82 */   public static final byte[] EMPTY_BYTES = new byte[0];
/*     */ 
/*     */   static int getTagWireType(int tag)
/*     */   {
/*  62 */     return tag & 0x7;
/*     */   }
/*     */ 
/*     */   public static int getTagFieldNumber(int tag)
/*     */   {
/*  67 */     return tag >>> 3;
/*     */   }
/*     */ 
/*     */   static int makeTag(int fieldNumber, int wireType)
/*     */   {
/*  72 */     return fieldNumber << 3 | wireType;
/*     */   }
/*     */ 
/*     */   public static boolean parseUnknownField(CodedInputByteBufferNano input, int tag)
/*     */     throws IOException
/*     */   {
/*  95 */     return input.skipField(tag);
/*     */   }
/*     */ 
/*     */   public static final int getRepeatedFieldArrayLength(CodedInputByteBufferNano input, int tag)
/*     */     throws IOException
/*     */   {
/* 113 */     int arrayLength = 1;
/* 114 */     int startPos = input.getPosition();
/* 115 */     input.skipField(tag);
/* 116 */     while (input.readTag() == tag) {
/* 117 */       input.skipField(tag);
/* 118 */       arrayLength++;
/*     */     }
/* 120 */     input.rewindToPosition(startPos);
/* 121 */     return arrayLength;
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.WireFormatNano
 * JD-Core Version:    0.6.2
 */
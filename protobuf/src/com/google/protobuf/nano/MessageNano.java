/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public abstract class MessageNano
/*     */ {
/*  42 */   protected volatile int cachedSize = -1;
/*     */ 
/*     */   public int getCachedSize()
/*     */   {
/*  52 */     if (this.cachedSize < 0)
/*     */     {
/*  54 */       getSerializedSize();
/*     */     }
/*  56 */     return this.cachedSize;
/*     */   }
/*     */ 
/*     */   public int getSerializedSize()
/*     */   {
/*  65 */     int size = computeSerializedSize();
/*  66 */     this.cachedSize = size;
/*  67 */     return size;
/*     */   }
/*     */ 
/*     */   protected int computeSerializedSize()
/*     */   {
/*  76 */     return 0;
/*     */   }
/*     */ 
/*     */   public void writeTo(CodedOutputByteBufferNano output)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract MessageNano mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
/*     */     throws IOException;
/*     */ 
/*     */   public static final byte[] toByteArray(MessageNano msg)
/*     */   {
/* 100 */     byte[] result = new byte[msg.getSerializedSize()];
/* 101 */     toByteArray(msg, result, 0, result.length);
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */   public static final void toByteArray(MessageNano msg, byte[] data, int offset, int length)
/*     */   {
/*     */     try
/*     */     {
/* 115 */       CodedOutputByteBufferNano output = CodedOutputByteBufferNano.newInstance(data, offset, length);
/*     */ 
/* 117 */       msg.writeTo(output);
/* 118 */       output.checkNoSpaceLeft();
/*     */     } catch (IOException e) {
/* 120 */       throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final <T extends MessageNano> T mergeFrom(T msg, byte[] data)
/*     */     throws InvalidProtocolBufferNanoException
/*     */   {
/* 131 */     return mergeFrom(msg, data, 0, data.length);
/*     */   }
/*     */ 
/*     */   public static final <T extends MessageNano> T mergeFrom(T msg, byte[] data, int off, int len)
/*     */     throws InvalidProtocolBufferNanoException
/*     */   {
/*     */     try
/*     */     {
/* 141 */       CodedInputByteBufferNano input = CodedInputByteBufferNano.newInstance(data, off, len);
/*     */ 
/* 143 */       msg.mergeFrom(input);
/* 144 */       input.checkLastTagWas(0);
/* 145 */       return msg;
/*     */     } catch (InvalidProtocolBufferNanoException e) {
/* 147 */       throw e; } catch (IOException e) {
/*     */     }
/* 149 */     throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
/*     */   }
/*     */ 
/*     */   public static final boolean messageNanoEquals(MessageNano a, MessageNano b)
/*     */   {
/* 159 */     if (a == b) {
/* 160 */       return true;
/*     */     }
/* 162 */     if ((a == null) || (b == null)) {
/* 163 */       return false;
/*     */     }
/* 165 */     if (a.getClass() != b.getClass()) {
/* 166 */       return false;
/*     */     }
/* 168 */     int serializedSize = a.getSerializedSize();
/* 169 */     if (b.getSerializedSize() != serializedSize) {
/* 170 */       return false;
/*     */     }
/* 172 */     byte[] aByteArray = new byte[serializedSize];
/* 173 */     byte[] bByteArray = new byte[serializedSize];
/* 174 */     toByteArray(a, aByteArray, 0, serializedSize);
/* 175 */     toByteArray(b, bByteArray, 0, serializedSize);
/* 176 */     return Arrays.equals(aByteArray, bByteArray);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     return MessageNanoPrinter.print(this);
/*     */   }
/*     */ 
/*     */   public MessageNano clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 196 */     return (MessageNano)super.clone();
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.MessageNano
 * JD-Core Version:    0.6.2
 */
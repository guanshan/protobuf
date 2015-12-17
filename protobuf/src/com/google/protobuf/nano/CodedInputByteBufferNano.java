/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class CodedInputByteBufferNano
/*     */ {
/*     */   private final byte[] buffer;
/*     */   private int bufferStart;
/*     */   private int bufferSize;
/*     */   private int bufferSizeAfterLimit;
/*     */   private int bufferPos;
/*     */   private int lastTag;
/* 403 */   private int currentLimit = 2147483647;
/*     */   private int recursionDepth;
/* 407 */   private int recursionLimit = 64;
/*     */ 
/* 410 */   private int sizeLimit = 67108864;
/*     */   private static final int DEFAULT_RECURSION_LIMIT = 64;
/*     */   private static final int DEFAULT_SIZE_LIMIT = 67108864;
/*     */ 
/*     */   public static CodedInputByteBufferNano newInstance(byte[] buf)
/*     */   {
/*  52 */     return newInstance(buf, 0, buf.length);
/*     */   }
/*     */ 
/*     */   public static CodedInputByteBufferNano newInstance(byte[] buf, int off, int len)
/*     */   {
/*  60 */     return new CodedInputByteBufferNano(buf, off, len);
/*     */   }
/*     */ 
/*     */   public int readTag()
/*     */     throws IOException
/*     */   {
/*  71 */     if (isAtEnd()) {
/*  72 */       this.lastTag = 0;
/*  73 */       return 0;
/*     */     }
/*     */ 
/*  76 */     this.lastTag = readRawVarint32();
/*  77 */     if (this.lastTag == 0)
/*     */     {
/*  79 */       throw InvalidProtocolBufferNanoException.invalidTag();
/*     */     }
/*  81 */     return this.lastTag;
/*     */   }
/*     */ 
/*     */   public void checkLastTagWas(int value)
/*     */     throws InvalidProtocolBufferNanoException
/*     */   {
/*  94 */     if (this.lastTag != value)
/*  95 */       throw InvalidProtocolBufferNanoException.invalidEndTag();
/*     */   }
/*     */ 
/*     */   public boolean skipField(int tag)
/*     */     throws IOException
/*     */   {
/* 106 */     switch (WireFormatNano.getTagWireType(tag)) {
/*     */     case 0:
/* 108 */       readInt32();
/* 109 */       return true;
/*     */     case 1:
/* 111 */       readRawLittleEndian64();
/* 112 */       return true;
/*     */     case 2:
/* 114 */       skipRawBytes(readRawVarint32());
/* 115 */       return true;
/*     */     case 3:
/* 117 */       skipMessage();
/* 118 */       checkLastTagWas(WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(tag), 4));
/*     */ 
/* 121 */       return true;
/*     */     case 4:
/* 123 */       return false;
/*     */     case 5:
/* 125 */       readRawLittleEndian32();
/* 126 */       return true;
/*     */     }
/* 128 */     throw InvalidProtocolBufferNanoException.invalidWireType();
/*     */   }
/*     */ 
/*     */   public void skipMessage()
/*     */     throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 138 */       int tag = readTag();
/* 139 */       if ((tag == 0) || (!skipField(tag)))
/* 140 */         return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public double readDouble()
/*     */     throws IOException
/*     */   {
/* 149 */     return Double.longBitsToDouble(readRawLittleEndian64());
/*     */   }
/*     */ 
/*     */   public float readFloat() throws IOException
/*     */   {
/* 154 */     return Float.intBitsToFloat(readRawLittleEndian32());
/*     */   }
/*     */ 
/*     */   public long readUInt64() throws IOException
/*     */   {
/* 159 */     return readRawVarint64();
/*     */   }
/*     */ 
/*     */   public long readInt64() throws IOException
/*     */   {
/* 164 */     return readRawVarint64();
/*     */   }
/*     */ 
/*     */   public int readInt32() throws IOException
/*     */   {
/* 169 */     return readRawVarint32();
/*     */   }
/*     */ 
/*     */   public long readFixed64() throws IOException
/*     */   {
/* 174 */     return readRawLittleEndian64();
/*     */   }
/*     */ 
/*     */   public int readFixed32() throws IOException
/*     */   {
/* 179 */     return readRawLittleEndian32();
/*     */   }
/*     */ 
/*     */   public boolean readBool() throws IOException
/*     */   {
/* 184 */     return readRawVarint32() != 0;
/*     */   }
/*     */ 
/*     */   public String readString() throws IOException
/*     */   {
/* 189 */     int size = readRawVarint32();
/* 190 */     if ((size <= this.bufferSize - this.bufferPos) && (size > 0))
/*     */     {
/* 193 */       String result = new String(this.buffer, this.bufferPos, size, "UTF-8");
/* 194 */       this.bufferPos += size;
/* 195 */       return result;
/*     */     }
/*     */ 
/* 198 */     return new String(readRawBytes(size), "UTF-8");
/*     */   }
/*     */ 
/*     */   public void readGroup(MessageNano msg, int fieldNumber)
/*     */     throws IOException
/*     */   {
/* 205 */     if (this.recursionDepth >= this.recursionLimit) {
/* 206 */       throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
/*     */     }
/* 208 */     this.recursionDepth += 1;
/* 209 */     msg.mergeFrom(this);
/* 210 */     checkLastTagWas(WireFormatNano.makeTag(fieldNumber, 4));
/*     */ 
/* 212 */     this.recursionDepth -= 1;
/*     */   }
/*     */ 
/*     */   public void readMessage(MessageNano msg) throws IOException
/*     */   {
/* 217 */     int length = readRawVarint32();
/* 218 */     if (this.recursionDepth >= this.recursionLimit) {
/* 219 */       throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
/*     */     }
/* 221 */     int oldLimit = pushLimit(length);
/* 222 */     this.recursionDepth += 1;
/* 223 */     msg.mergeFrom(this);
/* 224 */     checkLastTagWas(0);
/* 225 */     this.recursionDepth -= 1;
/* 226 */     popLimit(oldLimit);
/*     */   }
/*     */ 
/*     */   public byte[] readBytes() throws IOException
/*     */   {
/* 231 */     int size = readRawVarint32();
/* 232 */     if ((size <= this.bufferSize - this.bufferPos) && (size > 0))
/*     */     {
/* 235 */       byte[] result = new byte[size];
/* 236 */       System.arraycopy(this.buffer, this.bufferPos, result, 0, size);
/* 237 */       this.bufferPos += size;
/* 238 */       return result;
/*     */     }
/*     */ 
/* 241 */     return readRawBytes(size);
/*     */   }
/*     */ 
/*     */   public int readUInt32()
/*     */     throws IOException
/*     */   {
/* 247 */     return readRawVarint32();
/*     */   }
/*     */ 
/*     */   public int readEnum()
/*     */     throws IOException
/*     */   {
/* 255 */     return readRawVarint32();
/*     */   }
/*     */ 
/*     */   public int readSFixed32() throws IOException
/*     */   {
/* 260 */     return readRawLittleEndian32();
/*     */   }
/*     */ 
/*     */   public long readSFixed64() throws IOException
/*     */   {
/* 265 */     return readRawLittleEndian64();
/*     */   }
/*     */ 
/*     */   public int readSInt32() throws IOException
/*     */   {
/* 270 */     return decodeZigZag32(readRawVarint32());
/*     */   }
/*     */ 
/*     */   public long readSInt64() throws IOException
/*     */   {
/* 275 */     return decodeZigZag64(readRawVarint64());
/*     */   }
/*     */ 
/*     */   public int readRawVarint32()
/*     */     throws IOException
/*     */   {
/* 285 */     byte tmp = readRawByte();
/* 286 */     if (tmp >= 0) {
/* 287 */       return tmp;
/*     */     }
/* 289 */     int result = tmp & 0x7F;
/* 290 */     if ((tmp = readRawByte()) >= 0) {
/* 291 */       result |= tmp << 7;
/*     */     } else {
/* 293 */       result |= (tmp & 0x7F) << 7;
/* 294 */       if ((tmp = readRawByte()) >= 0) {
/* 295 */         result |= tmp << 14;
/*     */       } else {
/* 297 */         result |= (tmp & 0x7F) << 14;
/* 298 */         if ((tmp = readRawByte()) >= 0) {
/* 299 */           result |= tmp << 21;
/*     */         } else {
/* 301 */           result |= (tmp & 0x7F) << 21;
/* 302 */           result |= (tmp = readRawByte()) << 28;
/* 303 */           if (tmp < 0)
/*     */           {
/* 305 */             for (int i = 0; i < 5; i++) {
/* 306 */               if (readRawByte() >= 0) {
/* 307 */                 return result;
/*     */               }
/*     */             }
/* 310 */             throw InvalidProtocolBufferNanoException.malformedVarint();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 315 */     return result;
/*     */   }
/*     */ 
/*     */   public long readRawVarint64() throws IOException
/*     */   {
/* 320 */     int shift = 0;
/* 321 */     long result = 0L;
/* 322 */     while (shift < 64) {
/* 323 */       byte b = readRawByte();
/* 324 */       result |= (b & 0x7F) << shift;
/* 325 */       if ((b & 0x80) == 0) {
/* 326 */         return result;
/*     */       }
/* 328 */       shift += 7;
/*     */     }
/* 330 */     throw InvalidProtocolBufferNanoException.malformedVarint();
/*     */   }
/*     */ 
/*     */   public int readRawLittleEndian32() throws IOException
/*     */   {
/* 335 */     byte b1 = readRawByte();
/* 336 */     byte b2 = readRawByte();
/* 337 */     byte b3 = readRawByte();
/* 338 */     byte b4 = readRawByte();
/* 339 */     return b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16 | (b4 & 0xFF) << 24;
/*     */   }
/*     */ 
/*     */   public long readRawLittleEndian64()
/*     */     throws IOException
/*     */   {
/* 347 */     byte b1 = readRawByte();
/* 348 */     byte b2 = readRawByte();
/* 349 */     byte b3 = readRawByte();
/* 350 */     byte b4 = readRawByte();
/* 351 */     byte b5 = readRawByte();
/* 352 */     byte b6 = readRawByte();
/* 353 */     byte b7 = readRawByte();
/* 354 */     byte b8 = readRawByte();
/* 355 */     return b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16 | (b4 & 0xFF) << 24 | (b5 & 0xFF) << 32 | (b6 & 0xFF) << 40 | (b7 & 0xFF) << 48 | (b8 & 0xFF) << 56;
/*     */   }
/*     */ 
/*     */   public static int decodeZigZag32(int n)
/*     */   {
/* 376 */     return n >>> 1 ^ -(n & 0x1);
/*     */   }
/*     */ 
/*     */   public static long decodeZigZag64(long n)
/*     */   {
/* 390 */     return n >>> 1 ^ -(n & 1L);
/*     */   }
/*     */ 
/*     */   private CodedInputByteBufferNano(byte[] buffer, int off, int len)
/*     */   {
/* 416 */     this.buffer = buffer;
/* 417 */     this.bufferStart = off;
/* 418 */     this.bufferSize = (off + len);
/* 419 */     this.bufferPos = off;
/*     */   }
/*     */ 
/*     */   public int setRecursionLimit(int limit)
/*     */   {
/* 430 */     if (limit < 0) {
/* 431 */       throw new IllegalArgumentException("Recursion limit cannot be negative: " + limit);
/*     */     }
/*     */ 
/* 434 */     int oldLimit = this.recursionLimit;
/* 435 */     this.recursionLimit = limit;
/* 436 */     return oldLimit;
/*     */   }
/*     */ 
/*     */   public int setSizeLimit(int limit)
/*     */   {
/* 455 */     if (limit < 0) {
/* 456 */       throw new IllegalArgumentException("Size limit cannot be negative: " + limit);
/*     */     }
/*     */ 
/* 459 */     int oldLimit = this.sizeLimit;
/* 460 */     this.sizeLimit = limit;
/* 461 */     return oldLimit;
/*     */   }
/*     */ 
/*     */   public void resetSizeCounter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public int pushLimit(int byteLimit)
/*     */     throws InvalidProtocolBufferNanoException
/*     */   {
/* 477 */     if (byteLimit < 0) {
/* 478 */       throw InvalidProtocolBufferNanoException.negativeSize();
/*     */     }
/* 480 */     byteLimit += this.bufferPos;
/* 481 */     int oldLimit = this.currentLimit;
/* 482 */     if (byteLimit > oldLimit) {
/* 483 */       throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */     }
/* 485 */     this.currentLimit = byteLimit;
/*     */ 
/* 487 */     recomputeBufferSizeAfterLimit();
/*     */ 
/* 489 */     return oldLimit;
/*     */   }
/*     */ 
/*     */   private void recomputeBufferSizeAfterLimit() {
/* 493 */     this.bufferSize += this.bufferSizeAfterLimit;
/* 494 */     int bufferEnd = this.bufferSize;
/* 495 */     if (bufferEnd > this.currentLimit)
/*     */     {
/* 497 */       this.bufferSizeAfterLimit = (bufferEnd - this.currentLimit);
/* 498 */       this.bufferSize -= this.bufferSizeAfterLimit;
/*     */     } else {
/* 500 */       this.bufferSizeAfterLimit = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void popLimit(int oldLimit)
/*     */   {
/* 510 */     this.currentLimit = oldLimit;
/* 511 */     recomputeBufferSizeAfterLimit();
/*     */   }
/*     */ 
/*     */   public int getBytesUntilLimit()
/*     */   {
/* 519 */     if (this.currentLimit == 2147483647) {
/* 520 */       return -1;
/*     */     }
/*     */ 
/* 523 */     int currentAbsolutePosition = this.bufferPos;
/* 524 */     return this.currentLimit - currentAbsolutePosition;
/*     */   }
/*     */ 
/*     */   public boolean isAtEnd()
/*     */   {
/* 533 */     return this.bufferPos == this.bufferSize;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 540 */     return this.bufferPos - this.bufferStart;
/*     */   }
/*     */ 
/*     */   public byte[] getData(int offset, int length)
/*     */   {
/* 551 */     if (length == 0) {
/* 552 */       return WireFormatNano.EMPTY_BYTES;
/*     */     }
/* 554 */     byte[] copy = new byte[length];
/* 555 */     int start = this.bufferStart + offset;
/* 556 */     System.arraycopy(this.buffer, start, copy, 0, length);
/* 557 */     return copy;
/*     */   }
/*     */ 
/*     */   public void rewindToPosition(int position)
/*     */   {
/* 564 */     if (position > this.bufferPos - this.bufferStart) {
/* 565 */       throw new IllegalArgumentException("Position " + position + " is beyond current " + (this.bufferPos - this.bufferStart));
/*     */     }
/*     */ 
/* 568 */     if (position < 0) {
/* 569 */       throw new IllegalArgumentException("Bad position " + position);
/*     */     }
/* 571 */     this.bufferPos = (this.bufferStart + position);
/*     */   }
/*     */ 
/*     */   public byte readRawByte()
/*     */     throws IOException
/*     */   {
/* 581 */     if (this.bufferPos == this.bufferSize) {
/* 582 */       throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */     }
/* 584 */     return this.buffer[(this.bufferPos++)];
/*     */   }
/*     */ 
/*     */   public byte[] readRawBytes(int size)
/*     */     throws IOException
/*     */   {
/* 594 */     if (size < 0) {
/* 595 */       throw InvalidProtocolBufferNanoException.negativeSize();
/*     */     }
/*     */ 
/* 598 */     if (this.bufferPos + size > this.currentLimit)
/*     */     {
/* 600 */       skipRawBytes(this.currentLimit - this.bufferPos);
/*     */ 
/* 602 */       throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */     }
/*     */ 
/* 605 */     if (size <= this.bufferSize - this.bufferPos)
/*     */     {
/* 607 */       byte[] bytes = new byte[size];
/* 608 */       System.arraycopy(this.buffer, this.bufferPos, bytes, 0, size);
/* 609 */       this.bufferPos += size;
/* 610 */       return bytes;
/*     */     }
/* 612 */     throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */   }
/*     */ 
/*     */   public void skipRawBytes(int size)
/*     */     throws IOException
/*     */   {
/* 623 */     if (size < 0) {
/* 624 */       throw InvalidProtocolBufferNanoException.negativeSize();
/*     */     }
/*     */ 
/* 627 */     if (this.bufferPos + size > this.currentLimit)
/*     */     {
/* 629 */       skipRawBytes(this.currentLimit - this.bufferPos);
/*     */ 
/* 631 */       throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */     }
/*     */ 
/* 634 */     if (size <= this.bufferSize - this.bufferPos)
/*     */     {
/* 636 */       this.bufferPos += size;
/*     */     }
/* 638 */     else throw InvalidProtocolBufferNanoException.truncatedMessage();
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.CodedInputByteBufferNano
 * JD-Core Version:    0.6.2
 */
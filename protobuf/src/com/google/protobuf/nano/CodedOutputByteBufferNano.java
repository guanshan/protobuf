/*      */ package com.google.protobuf.nano;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.BufferOverflowException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ReadOnlyBufferException;
/*      */ 
/*      */ public final class CodedOutputByteBufferNano
/*      */ {
/*      */   private static final int MAX_UTF8_EXPANSION = 3;
/*      */   private final ByteBuffer buffer;
/*      */   public static final int LITTLE_ENDIAN_32_SIZE = 4;
/*      */   public static final int LITTLE_ENDIAN_64_SIZE = 8;
/*      */ 
/*      */   private CodedOutputByteBufferNano(byte[] buffer, int offset, int length)
/*      */   {
/*   59 */     this(ByteBuffer.wrap(buffer, offset, length));
/*      */   }
/*      */ 
/*      */   private CodedOutputByteBufferNano(ByteBuffer buffer) {
/*   63 */     this.buffer = buffer;
/*      */   }
/*      */ 
/*      */   public static CodedOutputByteBufferNano newInstance(byte[] flatArray)
/*      */   {
/*   73 */     return newInstance(flatArray, 0, flatArray.length);
/*      */   }
/*      */ 
/*      */   public static CodedOutputByteBufferNano newInstance(byte[] flatArray, int offset, int length)
/*      */   {
/*   85 */     return new CodedOutputByteBufferNano(flatArray, offset, length);
/*      */   }
/*      */ 
/*      */   public void writeDouble(int fieldNumber, double value)
/*      */     throws IOException
/*      */   {
/*   93 */     writeTag(fieldNumber, 1);
/*   94 */     writeDoubleNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeFloat(int fieldNumber, float value)
/*      */     throws IOException
/*      */   {
/*  100 */     writeTag(fieldNumber, 5);
/*  101 */     writeFloatNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeUInt64(int fieldNumber, long value)
/*      */     throws IOException
/*      */   {
/*  107 */     writeTag(fieldNumber, 0);
/*  108 */     writeUInt64NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeInt64(int fieldNumber, long value)
/*      */     throws IOException
/*      */   {
/*  114 */     writeTag(fieldNumber, 0);
/*  115 */     writeInt64NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeInt32(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  121 */     writeTag(fieldNumber, 0);
/*  122 */     writeInt32NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeFixed64(int fieldNumber, long value)
/*      */     throws IOException
/*      */   {
/*  128 */     writeTag(fieldNumber, 1);
/*  129 */     writeFixed64NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeFixed32(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  135 */     writeTag(fieldNumber, 5);
/*  136 */     writeFixed32NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeBool(int fieldNumber, boolean value)
/*      */     throws IOException
/*      */   {
/*  142 */     writeTag(fieldNumber, 0);
/*  143 */     writeBoolNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeString(int fieldNumber, String value)
/*      */     throws IOException
/*      */   {
/*  149 */     writeTag(fieldNumber, 2);
/*  150 */     writeStringNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeGroup(int fieldNumber, MessageNano value)
/*      */     throws IOException
/*      */   {
/*  156 */     writeTag(fieldNumber, 3);
/*  157 */     writeGroupNoTag(value);
/*  158 */     writeTag(fieldNumber, 4);
/*      */   }
/*      */ 
/*      */   public void writeMessage(int fieldNumber, MessageNano value)
/*      */     throws IOException
/*      */   {
/*  164 */     writeTag(fieldNumber, 2);
/*  165 */     writeMessageNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeBytes(int fieldNumber, byte[] value)
/*      */     throws IOException
/*      */   {
/*  171 */     writeTag(fieldNumber, 2);
/*  172 */     writeBytesNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeUInt32(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  178 */     writeTag(fieldNumber, 0);
/*  179 */     writeUInt32NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeEnum(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  188 */     writeTag(fieldNumber, 0);
/*  189 */     writeEnumNoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeSFixed32(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  195 */     writeTag(fieldNumber, 5);
/*  196 */     writeSFixed32NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeSFixed64(int fieldNumber, long value)
/*      */     throws IOException
/*      */   {
/*  202 */     writeTag(fieldNumber, 1);
/*  203 */     writeSFixed64NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeSInt32(int fieldNumber, int value)
/*      */     throws IOException
/*      */   {
/*  209 */     writeTag(fieldNumber, 0);
/*  210 */     writeSInt32NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeSInt64(int fieldNumber, long value)
/*      */     throws IOException
/*      */   {
/*  216 */     writeTag(fieldNumber, 0);
/*  217 */     writeSInt64NoTag(value);
/*      */   }
/*      */ 
/*      */   public void writeDoubleNoTag(double value)
/*      */     throws IOException
/*      */   {
/*  250 */     writeRawLittleEndian64(Double.doubleToLongBits(value));
/*      */   }
/*      */ 
/*      */   public void writeFloatNoTag(float value) throws IOException
/*      */   {
/*  255 */     writeRawLittleEndian32(Float.floatToIntBits(value));
/*      */   }
/*      */ 
/*      */   public void writeUInt64NoTag(long value) throws IOException
/*      */   {
/*  260 */     writeRawVarint64(value);
/*      */   }
/*      */ 
/*      */   public void writeInt64NoTag(long value) throws IOException
/*      */   {
/*  265 */     writeRawVarint64(value);
/*      */   }
/*      */ 
/*      */   public void writeInt32NoTag(int value) throws IOException
/*      */   {
/*  270 */     if (value >= 0) {
/*  271 */       writeRawVarint32(value);
/*      */     }
/*      */     else
/*  274 */       writeRawVarint64(value);
/*      */   }
/*      */ 
/*      */   public void writeFixed64NoTag(long value)
/*      */     throws IOException
/*      */   {
/*  280 */     writeRawLittleEndian64(value);
/*      */   }
/*      */ 
/*      */   public void writeFixed32NoTag(int value) throws IOException
/*      */   {
/*  285 */     writeRawLittleEndian32(value);
/*      */   }
/*      */ 
/*      */   public void writeBoolNoTag(boolean value) throws IOException
/*      */   {
/*  290 */     writeRawByte(value ? 1 : 0);
/*      */   }
/*      */ 
/*      */   public void writeStringNoTag(String value)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  299 */       int minLengthVarIntSize = computeRawVarint32Size(value.length());
/*  300 */       int maxLengthVarIntSize = computeRawVarint32Size(value.length() * 3);
/*  301 */       if (minLengthVarIntSize == maxLengthVarIntSize) {
/*  302 */         int oldPosition = this.buffer.position();
/*  303 */         this.buffer.position(oldPosition + minLengthVarIntSize);
/*  304 */         encode(value, this.buffer);
/*  305 */         int newPosition = this.buffer.position();
/*  306 */         this.buffer.position(oldPosition);
/*  307 */         writeRawVarint32(newPosition - oldPosition - minLengthVarIntSize);
/*  308 */         this.buffer.position(newPosition);
/*      */       } else {
/*  310 */         writeRawVarint32(encodedLength(value));
/*  311 */         encode(value, this.buffer);
/*      */       }
/*      */     } catch (BufferOverflowException e) {
/*  314 */       throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int encodedLength(CharSequence sequence)
/*      */   {
/*  329 */     int utf16Length = sequence.length();
/*  330 */     int utf8Length = utf16Length;
/*  331 */     int i = 0;
/*      */ 
/*  334 */     while ((i < utf16Length) && (sequence.charAt(i) < '')) {
/*  335 */       i++;
/*      */     }
/*      */ 
/*  339 */     for (; i < utf16Length; i++) {
/*  340 */       char c = sequence.charAt(i);
/*  341 */       if (c < 'ࠀ') {
/*  342 */         utf8Length += ('' - c >>> 31);
/*      */       } else {
/*  344 */         utf8Length += encodedLengthGeneral(sequence, i);
/*  345 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  349 */     if (utf8Length < utf16Length)
/*      */     {
/*  351 */       throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + 4294967296L));
/*      */     }
/*      */ 
/*  354 */     return utf8Length;
/*      */   }
/*      */ 
/*      */   private static int encodedLengthGeneral(CharSequence sequence, int start) {
/*  358 */     int utf16Length = sequence.length();
/*  359 */     int utf8Length = 0;
/*  360 */     for (int i = start; i < utf16Length; i++) {
/*  361 */       char c = sequence.charAt(i);
/*  362 */       if (c < 'ࠀ') {
/*  363 */         utf8Length += ('' - c >>> 31);
/*      */       } else {
/*  365 */         utf8Length += 2;
/*      */ 
/*  367 */         if ((55296 <= c) && (c <= 57343))
/*      */         {
/*  369 */           int cp = Character.codePointAt(sequence, i);
/*  370 */           if (cp < 65536) {
/*  371 */             throw new IllegalArgumentException("Unpaired surrogate at index " + i);
/*      */           }
/*  373 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*  377 */     return utf8Length;
/*      */   }
/*      */ 
/*      */   private static void encode(CharSequence sequence, ByteBuffer byteBuffer)
/*      */   {
/*  397 */     if (byteBuffer.isReadOnly())
/*  398 */       throw new ReadOnlyBufferException();
/*  399 */     if (byteBuffer.hasArray())
/*      */       try {
/*  401 */         int encoded = encode(sequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
/*      */ 
/*  405 */         byteBuffer.position(encoded - byteBuffer.arrayOffset());
/*      */       } catch (ArrayIndexOutOfBoundsException e) {
/*  407 */         BufferOverflowException boe = new BufferOverflowException();
/*  408 */         boe.initCause(e);
/*  409 */         throw boe;
/*      */       }
/*      */     else
/*  412 */       encodeDirect(sequence, byteBuffer);
/*      */   }
/*      */ 
/*      */   private static void encodeDirect(CharSequence sequence, ByteBuffer byteBuffer)
/*      */   {
/*  417 */     int utf16Length = sequence.length();
/*  418 */     for (int i = 0; i < utf16Length; i++) {
/*  419 */       char c = sequence.charAt(i);
/*  420 */       if (c < '') {
/*  421 */         byteBuffer.put((byte)c);
/*  422 */       } else if (c < 'ࠀ') {
/*  423 */         byteBuffer.put((byte)(0x3C0 | c >>> '\006'));
/*  424 */         byteBuffer.put((byte)(0x80 | 0x3F & c));
/*  425 */       } else if ((c < 55296) || (57343 < c))
/*      */       {
/*  427 */         byteBuffer.put((byte)(0x1E0 | c >>> '\f'));
/*  428 */         byteBuffer.put((byte)(0x80 | 0x3F & c >>> '\006'));
/*  429 */         byteBuffer.put((byte)(0x80 | 0x3F & c));
/*      */       }
/*      */       else
/*      */       {
/*      */         char low;
/*  432 */         if ((i + 1 == sequence.length()) || (!Character.isSurrogatePair(c, low = sequence.charAt(++i))))
/*      */         {
/*  434 */           throw new IllegalArgumentException("Unpaired surrogate at index " + (i - 1));
/*      */         }
/*      */         char low;
/*  436 */         int codePoint = Character.toCodePoint(c, low);
/*  437 */         byteBuffer.put((byte)(0xF0 | codePoint >>> 18));
/*  438 */         byteBuffer.put((byte)(0x80 | 0x3F & codePoint >>> 12));
/*  439 */         byteBuffer.put((byte)(0x80 | 0x3F & codePoint >>> 6));
/*  440 */         byteBuffer.put((byte)(0x80 | 0x3F & codePoint));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int encode(CharSequence sequence, byte[] bytes, int offset, int length) {
/*  446 */     int utf16Length = sequence.length();
/*  447 */     int j = offset;
/*  448 */     int i = 0;
/*  449 */     int limit = offset + length;
/*      */     char c;
/*  452 */     for (; (i < utf16Length) && (i + j < limit) && ((c = sequence.charAt(i)) < ''); i++) {
/*  453 */       bytes[(j + i)] = ((byte)c);
/*      */     }
/*  455 */     if (i == utf16Length) {
/*  456 */       return j + utf16Length;
/*      */     }
/*  458 */     j += i;
/*  459 */     for (; i < utf16Length; i++) {
/*  460 */       char c = sequence.charAt(i);
/*  461 */       if ((c < '') && (j < limit)) {
/*  462 */         bytes[(j++)] = ((byte)c);
/*  463 */       } else if ((c < 'ࠀ') && (j <= limit - 2)) {
/*  464 */         bytes[(j++)] = ((byte)(0x3C0 | c >>> '\006'));
/*  465 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & c));
/*  466 */       } else if (((c < 55296) || (57343 < c)) && (j <= limit - 3))
/*      */       {
/*  468 */         bytes[(j++)] = ((byte)(0x1E0 | c >>> '\f'));
/*  469 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & c >>> '\006'));
/*  470 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & c));
/*  471 */       } else if (j <= limit - 4)
/*      */       {
/*      */         char low;
/*  474 */         if ((i + 1 == sequence.length()) || (!Character.isSurrogatePair(c, low = sequence.charAt(++i))))
/*      */         {
/*  476 */           throw new IllegalArgumentException("Unpaired surrogate at index " + (i - 1));
/*      */         }
/*      */         char low;
/*  478 */         int codePoint = Character.toCodePoint(c, low);
/*  479 */         bytes[(j++)] = ((byte)(0xF0 | codePoint >>> 18));
/*  480 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & codePoint >>> 12));
/*  481 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & codePoint >>> 6));
/*  482 */         bytes[(j++)] = ((byte)(0x80 | 0x3F & codePoint));
/*      */       } else {
/*  484 */         throw new ArrayIndexOutOfBoundsException("Failed writing " + c + " at index " + j);
/*      */       }
/*      */     }
/*  487 */     return j;
/*      */   }
/*      */ 
/*      */   public void writeGroupNoTag(MessageNano value)
/*      */     throws IOException
/*      */   {
/*  494 */     value.writeTo(this);
/*      */   }
/*      */ 
/*      */   public void writeMessageNoTag(MessageNano value) throws IOException
/*      */   {
/*  499 */     writeRawVarint32(value.getCachedSize());
/*  500 */     value.writeTo(this);
/*      */   }
/*      */ 
/*      */   public void writeBytesNoTag(byte[] value) throws IOException
/*      */   {
/*  505 */     writeRawVarint32(value.length);
/*  506 */     writeRawBytes(value);
/*      */   }
/*      */ 
/*      */   public void writeUInt32NoTag(int value) throws IOException
/*      */   {
/*  511 */     writeRawVarint32(value);
/*      */   }
/*      */ 
/*      */   public void writeEnumNoTag(int value)
/*      */     throws IOException
/*      */   {
/*  519 */     writeRawVarint32(value);
/*      */   }
/*      */ 
/*      */   public void writeSFixed32NoTag(int value) throws IOException
/*      */   {
/*  524 */     writeRawLittleEndian32(value);
/*      */   }
/*      */ 
/*      */   public void writeSFixed64NoTag(long value) throws IOException
/*      */   {
/*  529 */     writeRawLittleEndian64(value);
/*      */   }
/*      */ 
/*      */   public void writeSInt32NoTag(int value) throws IOException
/*      */   {
/*  534 */     writeRawVarint32(encodeZigZag32(value));
/*      */   }
/*      */ 
/*      */   public void writeSInt64NoTag(long value) throws IOException
/*      */   {
/*  539 */     writeRawVarint64(encodeZigZag64(value));
/*      */   }
/*      */ 
/*      */   public static int computeDoubleSize(int fieldNumber, double value)
/*      */   {
/*  550 */     return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeFloatSize(int fieldNumber, float value)
/*      */   {
/*  558 */     return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeUInt64Size(int fieldNumber, long value)
/*      */   {
/*  566 */     return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeInt64Size(int fieldNumber, long value)
/*      */   {
/*  574 */     return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeInt32Size(int fieldNumber, int value)
/*      */   {
/*  582 */     return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeFixed64Size(int fieldNumber, long value)
/*      */   {
/*  591 */     return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeFixed32Size(int fieldNumber, int value)
/*      */   {
/*  600 */     return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeBoolSize(int fieldNumber, boolean value)
/*      */   {
/*  609 */     return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeStringSize(int fieldNumber, String value)
/*      */   {
/*  618 */     return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeGroupSize(int fieldNumber, MessageNano value)
/*      */   {
/*  627 */     return computeTagSize(fieldNumber) * 2 + computeGroupSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeMessageSize(int fieldNumber, MessageNano value)
/*      */   {
/*  636 */     return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeBytesSize(int fieldNumber, byte[] value)
/*      */   {
/*  645 */     return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeUInt32Size(int fieldNumber, int value)
/*      */   {
/*  653 */     return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeEnumSize(int fieldNumber, int value)
/*      */   {
/*  662 */     return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeSFixed32Size(int fieldNumber, int value)
/*      */   {
/*  671 */     return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeSFixed64Size(int fieldNumber, long value)
/*      */   {
/*  680 */     return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeSInt32Size(int fieldNumber, int value)
/*      */   {
/*  688 */     return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeSInt64Size(int fieldNumber, long value)
/*      */   {
/*  696 */     return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
/*      */   }
/*      */ 
/*      */   public static int computeDoubleSizeNoTag(double value)
/*      */   {
/*  730 */     return 8;
/*      */   }
/*      */ 
/*      */   public static int computeFloatSizeNoTag(float value)
/*      */   {
/*  738 */     return 4;
/*      */   }
/*      */ 
/*      */   public static int computeUInt64SizeNoTag(long value)
/*      */   {
/*  746 */     return computeRawVarint64Size(value);
/*      */   }
/*      */ 
/*      */   public static int computeInt64SizeNoTag(long value)
/*      */   {
/*  754 */     return computeRawVarint64Size(value);
/*      */   }
/*      */ 
/*      */   public static int computeInt32SizeNoTag(int value)
/*      */   {
/*  762 */     if (value >= 0) {
/*  763 */       return computeRawVarint32Size(value);
/*      */     }
/*      */ 
/*  766 */     return 10;
/*      */   }
/*      */ 
/*      */   public static int computeFixed64SizeNoTag(long value)
/*      */   {
/*  775 */     return 8;
/*      */   }
/*      */ 
/*      */   public static int computeFixed32SizeNoTag(int value)
/*      */   {
/*  783 */     return 4;
/*      */   }
/*      */ 
/*      */   public static int computeBoolSizeNoTag(boolean value)
/*      */   {
/*  791 */     return 1;
/*      */   }
/*      */ 
/*      */   public static int computeStringSizeNoTag(String value)
/*      */   {
/*  799 */     int length = encodedLength(value);
/*  800 */     return computeRawVarint32Size(length) + length;
/*      */   }
/*      */ 
/*      */   public static int computeGroupSizeNoTag(MessageNano value)
/*      */   {
/*  808 */     return value.getSerializedSize();
/*      */   }
/*      */ 
/*      */   public static int computeMessageSizeNoTag(MessageNano value)
/*      */   {
/*  816 */     int size = value.getSerializedSize();
/*  817 */     return computeRawVarint32Size(size) + size;
/*      */   }
/*      */ 
/*      */   public static int computeBytesSizeNoTag(byte[] value)
/*      */   {
/*  825 */     return computeRawVarint32Size(value.length) + value.length;
/*      */   }
/*      */ 
/*      */   public static int computeUInt32SizeNoTag(int value)
/*      */   {
/*  833 */     return computeRawVarint32Size(value);
/*      */   }
/*      */ 
/*      */   public static int computeEnumSizeNoTag(int value)
/*      */   {
/*  841 */     return computeRawVarint32Size(value);
/*      */   }
/*      */ 
/*      */   public static int computeSFixed32SizeNoTag(int value)
/*      */   {
/*  849 */     return 4;
/*      */   }
/*      */ 
/*      */   public static int computeSFixed64SizeNoTag(long value)
/*      */   {
/*  857 */     return 8;
/*      */   }
/*      */ 
/*      */   public static int computeSInt32SizeNoTag(int value)
/*      */   {
/*  865 */     return computeRawVarint32Size(encodeZigZag32(value));
/*      */   }
/*      */ 
/*      */   public static int computeSInt64SizeNoTag(long value)
/*      */   {
/*  873 */     return computeRawVarint64Size(encodeZigZag64(value));
/*      */   }
/*      */ 
/*      */   public int spaceLeft()
/*      */   {
/*  883 */     return this.buffer.remaining();
/*      */   }
/*      */ 
/*      */   public void checkNoSpaceLeft()
/*      */   {
/*  894 */     if (spaceLeft() != 0)
/*  895 */       throw new IllegalStateException("Did not write as much data as expected.");
/*      */   }
/*      */ 
/*      */   public int position()
/*      */   {
/*  904 */     return this.buffer.position();
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  914 */     this.buffer.clear();
/*      */   }
/*      */ 
/*      */   public void writeRawByte(byte value)
/*      */     throws IOException
/*      */   {
/*  933 */     if (!this.buffer.hasRemaining())
/*      */     {
/*  935 */       throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
/*      */     }
/*      */ 
/*  938 */     this.buffer.put(value);
/*      */   }
/*      */ 
/*      */   public void writeRawByte(int value) throws IOException
/*      */   {
/*  943 */     writeRawByte((byte)value);
/*      */   }
/*      */ 
/*      */   public void writeRawBytes(byte[] value) throws IOException
/*      */   {
/*  948 */     writeRawBytes(value, 0, value.length);
/*      */   }
/*      */ 
/*      */   public void writeRawBytes(byte[] value, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  954 */     if (this.buffer.remaining() >= length) {
/*  955 */       this.buffer.put(value, offset, length);
/*      */     }
/*      */     else
/*  958 */       throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
/*      */   }
/*      */ 
/*      */   public void writeTag(int fieldNumber, int wireType)
/*      */     throws IOException
/*      */   {
/*  965 */     writeRawVarint32(WireFormatNano.makeTag(fieldNumber, wireType));
/*      */   }
/*      */ 
/*      */   public static int computeTagSize(int fieldNumber)
/*      */   {
/*  970 */     return computeRawVarint32Size(WireFormatNano.makeTag(fieldNumber, 0));
/*      */   }
/*      */ 
/*      */   public void writeRawVarint32(int value)
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/*  979 */       if ((value & 0xFFFFFF80) == 0) {
/*  980 */         writeRawByte(value);
/*  981 */         return;
/*      */       }
/*  983 */       writeRawByte(value & 0x7F | 0x80);
/*  984 */       value >>>= 7;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int computeRawVarint32Size(int value)
/*      */   {
/*  995 */     if ((value & 0xFFFFFF80) == 0) return 1;
/*  996 */     if ((value & 0xFFFFC000) == 0) return 2;
/*  997 */     if ((value & 0xFFE00000) == 0) return 3;
/*  998 */     if ((value & 0xF0000000) == 0) return 4;
/*  999 */     return 5;
/*      */   }
/*      */ 
/*      */   public void writeRawVarint64(long value) throws IOException
/*      */   {
/*      */     while (true) {
/* 1005 */       if ((value & 0xFFFFFF80) == 0L) {
/* 1006 */         writeRawByte((int)value);
/* 1007 */         return;
/*      */       }
/* 1009 */       writeRawByte((int)value & 0x7F | 0x80);
/* 1010 */       value >>>= 7;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static int computeRawVarint64Size(long value)
/*      */   {
/* 1017 */     if ((value & 0xFFFFFF80) == 0L) return 1;
/* 1018 */     if ((value & 0xFFFFC000) == 0L) return 2;
/* 1019 */     if ((value & 0xFFE00000) == 0L) return 3;
/* 1020 */     if ((value & 0xF0000000) == 0L) return 4;
/* 1021 */     if ((value & 0x0) == 0L) return 5;
/* 1022 */     if ((value & 0x0) == 0L) return 6;
/* 1023 */     if ((value & 0x0) == 0L) return 7;
/* 1024 */     if ((value & 0x0) == 0L) return 8;
/* 1025 */     if ((value & 0x0) == 0L) return 9;
/* 1026 */     return 10;
/*      */   }
/*      */ 
/*      */   public void writeRawLittleEndian32(int value) throws IOException
/*      */   {
/* 1031 */     writeRawByte(value & 0xFF);
/* 1032 */     writeRawByte(value >> 8 & 0xFF);
/* 1033 */     writeRawByte(value >> 16 & 0xFF);
/* 1034 */     writeRawByte(value >> 24 & 0xFF);
/*      */   }
/*      */ 
/*      */   public void writeRawLittleEndian64(long value)
/*      */     throws IOException
/*      */   {
/* 1041 */     writeRawByte((int)value & 0xFF);
/* 1042 */     writeRawByte((int)(value >> 8) & 0xFF);
/* 1043 */     writeRawByte((int)(value >> 16) & 0xFF);
/* 1044 */     writeRawByte((int)(value >> 24) & 0xFF);
/* 1045 */     writeRawByte((int)(value >> 32) & 0xFF);
/* 1046 */     writeRawByte((int)(value >> 40) & 0xFF);
/* 1047 */     writeRawByte((int)(value >> 48) & 0xFF);
/* 1048 */     writeRawByte((int)(value >> 56) & 0xFF);
/*      */   }
/*      */ 
/*      */   public static int encodeZigZag32(int n)
/*      */   {
/* 1065 */     return n << 1 ^ n >> 31;
/*      */   }
/*      */ 
/*      */   public static long encodeZigZag64(long n)
/*      */   {
/* 1080 */     return n << 1 ^ n >> 63;
/*      */   }
/*      */ 
/*      */   public static class OutOfSpaceException extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = -6947486886997889499L;
/*      */ 
/*      */     OutOfSpaceException(int position, int limit)
/*      */     {
/*  926 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.CodedOutputByteBufferNano
 * JD-Core Version:    0.6.2
 */
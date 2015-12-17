/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Extension<M extends ExtendableMessageNano<M>, T>
/*     */ {
/*     */   public static final int TYPE_DOUBLE = 1;
/*     */   public static final int TYPE_FLOAT = 2;
/*     */   public static final int TYPE_INT64 = 3;
/*     */   public static final int TYPE_UINT64 = 4;
/*     */   public static final int TYPE_INT32 = 5;
/*     */   public static final int TYPE_FIXED64 = 6;
/*     */   public static final int TYPE_FIXED32 = 7;
/*     */   public static final int TYPE_BOOL = 8;
/*     */   public static final int TYPE_STRING = 9;
/*     */   public static final int TYPE_GROUP = 10;
/*     */   public static final int TYPE_MESSAGE = 11;
/*     */   public static final int TYPE_BYTES = 12;
/*     */   public static final int TYPE_UINT32 = 13;
/*     */   public static final int TYPE_ENUM = 14;
/*     */   public static final int TYPE_SFIXED32 = 15;
/*     */   public static final int TYPE_SFIXED64 = 16;
/*     */   public static final int TYPE_SINT32 = 17;
/*     */   public static final int TYPE_SINT64 = 18;
/*     */   protected final int type;
/*     */   protected final Class<T> clazz;
/*     */   public final int tag;
/*     */   protected final boolean repeated;
/*     */ 
/*     */   @Deprecated
/*     */   public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, int tag)
/*     */   {
/*  87 */     return new Extension(type, clazz, tag, false);
/*     */   }
/*     */ 
/*     */   public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, long tag)
/*     */   {
/* 103 */     return new Extension(type, clazz, (int)tag, false);
/*     */   }
/*     */ 
/*     */   public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(int type, Class<T[]> clazz, long tag)
/*     */   {
/* 114 */     return new Extension(type, clazz, (int)tag, true);
/*     */   }
/*     */ 
/*     */   public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(int type, Class<T> clazz, long tag)
/*     */   {
/* 126 */     return new PrimitiveExtension(type, clazz, (int)tag, false, 0, 0);
/*     */   }
/*     */ 
/*     */   public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(int type, Class<T> clazz, long tag, long nonPackedTag, long packedTag)
/*     */   {
/* 139 */     return new PrimitiveExtension(type, clazz, (int)tag, true, (int)nonPackedTag, (int)packedTag);
/*     */   }
/*     */ 
/*     */   private Extension(int type, Class<T> clazz, int tag, boolean repeated)
/*     */   {
/* 168 */     this.type = type;
/* 169 */     this.clazz = clazz;
/* 170 */     this.tag = tag;
/* 171 */     this.repeated = repeated;
/*     */   }
/*     */ 
/*     */   final T getValueFrom(List<UnknownFieldData> unknownFields)
/*     */   {
/* 183 */     if (unknownFields == null) {
/* 184 */       return null;
/*     */     }
/* 186 */     return this.repeated ? getRepeatedValueFrom(unknownFields) : getSingularValueFrom(unknownFields);
/*     */   }
/*     */ 
/*     */   private T getRepeatedValueFrom(List<UnknownFieldData> unknownFields)
/*     */   {
/* 191 */     List resultList = new ArrayList();
/* 192 */     for (int i = 0; i < unknownFields.size(); i++) {
/* 193 */       UnknownFieldData data = (UnknownFieldData)unknownFields.get(i);
/* 194 */       if (data.bytes.length != 0) {
/* 195 */         readDataInto(data, resultList);
/*     */       }
/*     */     }
/*     */ 
/* 199 */     int resultSize = resultList.size();
/* 200 */     if (resultSize == 0) {
/* 201 */       return null;
/*     */     }
/* 203 */     Object result = this.clazz.cast(Array.newInstance(this.clazz.getComponentType(), resultSize));
/* 204 */     for (int i = 0; i < resultSize; i++) {
/* 205 */       Array.set(result, i, resultList.get(i));
/*     */     }
/* 207 */     return result;
/*     */   }
/*     */ 
/*     */   private T getSingularValueFrom(List<UnknownFieldData> unknownFields)
/*     */   {
/* 213 */     if (unknownFields.isEmpty()) {
/* 214 */       return null;
/*     */     }
/* 216 */     UnknownFieldData lastData = (UnknownFieldData)unknownFields.get(unknownFields.size() - 1);
/* 217 */     return this.clazz.cast(readData(CodedInputByteBufferNano.newInstance(lastData.bytes)));
/*     */   }
/*     */ 
/*     */   protected Object readData(CodedInputByteBufferNano input)
/*     */   {
/* 222 */     Class messageType = this.repeated ? this.clazz.getComponentType() : this.clazz;
/*     */     try {
/* 224 */       switch (this.type) {
/*     */       case 10:
/* 226 */         MessageNano group = (MessageNano)messageType.newInstance();
/* 227 */         input.readGroup(group, WireFormatNano.getTagFieldNumber(this.tag));
/* 228 */         return group;
/*     */       case 11:
/* 230 */         MessageNano message = (MessageNano)messageType.newInstance();
/* 231 */         input.readMessage(message);
/* 232 */         return message;
/*     */       }
/* 234 */       throw new IllegalArgumentException("Unknown type " + this.type);
/*     */     }
/*     */     catch (InstantiationException e) {
/* 237 */       throw new IllegalArgumentException("Error creating instance of class " + messageType, e);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 240 */       throw new IllegalArgumentException("Error creating instance of class " + messageType, e);
/*     */     }
/*     */     catch (IOException e) {
/* 243 */       throw new IllegalArgumentException("Error reading extension field", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void readDataInto(UnknownFieldData data, List<Object> resultList)
/*     */   {
/* 249 */     resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
/*     */   }
/*     */ 
/*     */   void writeTo(Object value, CodedOutputByteBufferNano output) throws IOException {
/* 253 */     if (this.repeated)
/* 254 */       writeRepeatedData(value, output);
/*     */     else
/* 256 */       writeSingularData(value, output);
/*     */   }
/*     */ 
/*     */   protected void writeSingularData(Object value, CodedOutputByteBufferNano out)
/*     */   {
/*     */     try
/*     */     {
/* 263 */       out.writeRawVarint32(this.tag);
/* 264 */       switch (this.type) {
/*     */       case 10:
/* 266 */         MessageNano groupValue = (MessageNano)value;
/* 267 */         int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
/* 268 */         out.writeGroupNoTag(groupValue);
/*     */ 
/* 270 */         out.writeTag(fieldNumber, 4);
/* 271 */         break;
/*     */       case 11:
/* 273 */         MessageNano messageValue = (MessageNano)value;
/* 274 */         out.writeMessageNoTag(messageValue);
/* 275 */         break;
/*     */       default:
/* 277 */         throw new IllegalArgumentException("Unknown type " + this.type);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 281 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output)
/*     */   {
/* 287 */     int arrayLength = Array.getLength(array);
/* 288 */     for (int i = 0; i < arrayLength; i++) {
/* 289 */       Object element = Array.get(array, i);
/* 290 */       if (element != null)
/* 291 */         writeSingularData(element, output);
/*     */     }
/*     */   }
/*     */ 
/*     */   int computeSerializedSize(Object value)
/*     */   {
/* 297 */     if (this.repeated) {
/* 298 */       return computeRepeatedSerializedSize(value);
/*     */     }
/* 300 */     return computeSingularSerializedSize(value);
/*     */   }
/*     */ 
/*     */   protected int computeRepeatedSerializedSize(Object array)
/*     */   {
/* 306 */     int size = 0;
/* 307 */     int arrayLength = Array.getLength(array);
/* 308 */     for (int i = 0; i < arrayLength; i++) {
/* 309 */       Object element = Array.get(array, i);
/* 310 */       if (element != null) {
/* 311 */         size += computeSingularSerializedSize(Array.get(array, i));
/*     */       }
/*     */     }
/* 314 */     return size;
/*     */   }
/*     */ 
/*     */   protected int computeSingularSerializedSize(Object value)
/*     */   {
/* 319 */     int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
/* 320 */     switch (this.type) {
/*     */     case 10:
/* 322 */       MessageNano groupValue = (MessageNano)value;
/* 323 */       return CodedOutputByteBufferNano.computeGroupSize(fieldNumber, groupValue);
/*     */     case 11:
/* 325 */       MessageNano messageValue = (MessageNano)value;
/* 326 */       return CodedOutputByteBufferNano.computeMessageSize(fieldNumber, messageValue);
/*     */     }
/* 328 */     throw new IllegalArgumentException("Unknown type " + this.type);
/*     */   }
/*     */ 
/*     */   private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T> extends Extension<M, T>
/*     */   {
/*     */     private final int nonPackedTag;
/*     */     private final int packedTag;
/*     */ 
/*     */     public PrimitiveExtension(int type, Class<T> clazz, int tag, boolean repeated, int nonPackedTag, int packedTag)
/*     */     {
/* 352 */       super(clazz, tag, repeated, null);
/* 353 */       this.nonPackedTag = nonPackedTag;
/* 354 */       this.packedTag = packedTag;
/*     */     }
/*     */ 
/*     */     protected Object readData(CodedInputByteBufferNano input)
/*     */     {
/*     */       try {
/* 360 */         switch (this.type) {
/*     */         case 1:
/* 362 */           return Double.valueOf(input.readDouble());
/*     */         case 2:
/* 364 */           return Float.valueOf(input.readFloat());
/*     */         case 3:
/* 366 */           return Long.valueOf(input.readInt64());
/*     */         case 4:
/* 368 */           return Long.valueOf(input.readUInt64());
/*     */         case 5:
/* 370 */           return Integer.valueOf(input.readInt32());
/*     */         case 6:
/* 372 */           return Long.valueOf(input.readFixed64());
/*     */         case 7:
/* 374 */           return Integer.valueOf(input.readFixed32());
/*     */         case 8:
/* 376 */           return Boolean.valueOf(input.readBool());
/*     */         case 9:
/* 378 */           return input.readString();
/*     */         case 12:
/* 380 */           return input.readBytes();
/*     */         case 13:
/* 382 */           return Integer.valueOf(input.readUInt32());
/*     */         case 14:
/* 384 */           return Integer.valueOf(input.readEnum());
/*     */         case 15:
/* 386 */           return Integer.valueOf(input.readSFixed32());
/*     */         case 16:
/* 388 */           return Long.valueOf(input.readSFixed64());
/*     */         case 17:
/* 390 */           return Integer.valueOf(input.readSInt32());
/*     */         case 18:
/* 392 */           return Long.valueOf(input.readSInt64());
/*     */         case 10:
/* 394 */         case 11: } throw new IllegalArgumentException("Unknown type " + this.type);
/*     */       }
/*     */       catch (IOException e) {
/* 397 */         throw new IllegalArgumentException("Error reading extension field", e);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void readDataInto(UnknownFieldData data, List<Object> resultList)
/*     */     {
/* 405 */       if (data.tag == this.nonPackedTag) {
/* 406 */         resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
/*     */       } else {
/* 408 */         CodedInputByteBufferNano buffer = CodedInputByteBufferNano.newInstance(data.bytes);
/*     */         try
/*     */         {
/* 411 */           buffer.pushLimit(buffer.readRawVarint32());
/*     */         } catch (IOException e) {
/* 413 */           throw new IllegalArgumentException("Error reading extension field", e);
/*     */         }
/* 415 */         while (!buffer.isAtEnd())
/* 416 */           resultList.add(readData(buffer));
/*     */       }
/*     */     }
/*     */ 
/*     */     protected final void writeSingularData(Object value, CodedOutputByteBufferNano output)
/*     */     {
/*     */       try
/*     */       {
/* 424 */         output.writeRawVarint32(this.tag);
/* 425 */         switch (this.type) {
/*     */         case 1:
/* 427 */           Double doubleValue = (Double)value;
/* 428 */           output.writeDoubleNoTag(doubleValue.doubleValue());
/* 429 */           break;
/*     */         case 2:
/* 431 */           Float floatValue = (Float)value;
/* 432 */           output.writeFloatNoTag(floatValue.floatValue());
/* 433 */           break;
/*     */         case 3:
/* 435 */           Long int64Value = (Long)value;
/* 436 */           output.writeInt64NoTag(int64Value.longValue());
/* 437 */           break;
/*     */         case 4:
/* 439 */           Long uint64Value = (Long)value;
/* 440 */           output.writeUInt64NoTag(uint64Value.longValue());
/* 441 */           break;
/*     */         case 5:
/* 443 */           Integer int32Value = (Integer)value;
/* 444 */           output.writeInt32NoTag(int32Value.intValue());
/* 445 */           break;
/*     */         case 6:
/* 447 */           Long fixed64Value = (Long)value;
/* 448 */           output.writeFixed64NoTag(fixed64Value.longValue());
/* 449 */           break;
/*     */         case 7:
/* 451 */           Integer fixed32Value = (Integer)value;
/* 452 */           output.writeFixed32NoTag(fixed32Value.intValue());
/* 453 */           break;
/*     */         case 8:
/* 455 */           Boolean boolValue = (Boolean)value;
/* 456 */           output.writeBoolNoTag(boolValue.booleanValue());
/* 457 */           break;
/*     */         case 9:
/* 459 */           String stringValue = (String)value;
/* 460 */           output.writeStringNoTag(stringValue);
/* 461 */           break;
/*     */         case 12:
/* 463 */           byte[] bytesValue = (byte[])value;
/* 464 */           output.writeBytesNoTag(bytesValue);
/* 465 */           break;
/*     */         case 13:
/* 467 */           Integer uint32Value = (Integer)value;
/* 468 */           output.writeUInt32NoTag(uint32Value.intValue());
/* 469 */           break;
/*     */         case 14:
/* 471 */           Integer enumValue = (Integer)value;
/* 472 */           output.writeEnumNoTag(enumValue.intValue());
/* 473 */           break;
/*     */         case 15:
/* 475 */           Integer sfixed32Value = (Integer)value;
/* 476 */           output.writeSFixed32NoTag(sfixed32Value.intValue());
/* 477 */           break;
/*     */         case 16:
/* 479 */           Long sfixed64Value = (Long)value;
/* 480 */           output.writeSFixed64NoTag(sfixed64Value.longValue());
/* 481 */           break;
/*     */         case 17:
/* 483 */           Integer sint32Value = (Integer)value;
/* 484 */           output.writeSInt32NoTag(sint32Value.intValue());
/* 485 */           break;
/*     */         case 18:
/* 487 */           Long sint64Value = (Long)value;
/* 488 */           output.writeSInt64NoTag(sint64Value.longValue());
/* 489 */           break;
/*     */         case 10:
/*     */         case 11:
/*     */         default:
/* 491 */           throw new IllegalArgumentException("Unknown type " + this.type);
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 495 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output)
/*     */     {
/* 501 */       if (this.tag == this.nonPackedTag)
/*     */       {
/* 503 */         super.writeRepeatedData(array, output);
/* 504 */       } else if (this.tag == this.packedTag)
/*     */       {
/* 507 */         int arrayLength = Array.getLength(array);
/* 508 */         int dataSize = computePackedDataSize(array);
/*     */         try
/*     */         {
/* 511 */           output.writeRawVarint32(this.tag);
/* 512 */           output.writeRawVarint32(dataSize);
/* 513 */           switch (this.type) {
/*     */           case 8:
/* 515 */             for (int i = 0; i < arrayLength; i++) {
/* 516 */               output.writeBoolNoTag(Array.getBoolean(array, i));
/*     */             }
/* 518 */             break;
/*     */           case 7:
/* 520 */             for (int i = 0; i < arrayLength; i++) {
/* 521 */               output.writeFixed32NoTag(Array.getInt(array, i));
/*     */             }
/* 523 */             break;
/*     */           case 15:
/* 525 */             for (int i = 0; i < arrayLength; i++) {
/* 526 */               output.writeSFixed32NoTag(Array.getInt(array, i));
/*     */             }
/* 528 */             break;
/*     */           case 2:
/* 530 */             for (int i = 0; i < arrayLength; i++) {
/* 531 */               output.writeFloatNoTag(Array.getFloat(array, i));
/*     */             }
/* 533 */             break;
/*     */           case 6:
/* 535 */             for (int i = 0; i < arrayLength; i++) {
/* 536 */               output.writeFixed64NoTag(Array.getLong(array, i));
/*     */             }
/* 538 */             break;
/*     */           case 16:
/* 540 */             for (int i = 0; i < arrayLength; i++) {
/* 541 */               output.writeSFixed64NoTag(Array.getLong(array, i));
/*     */             }
/* 543 */             break;
/*     */           case 1:
/* 545 */             for (int i = 0; i < arrayLength; i++) {
/* 546 */               output.writeDoubleNoTag(Array.getDouble(array, i));
/*     */             }
/* 548 */             break;
/*     */           case 5:
/* 550 */             for (int i = 0; i < arrayLength; i++) {
/* 551 */               output.writeInt32NoTag(Array.getInt(array, i));
/*     */             }
/* 553 */             break;
/*     */           case 17:
/* 555 */             for (int i = 0; i < arrayLength; i++) {
/* 556 */               output.writeSInt32NoTag(Array.getInt(array, i));
/*     */             }
/* 558 */             break;
/*     */           case 13:
/* 560 */             for (int i = 0; i < arrayLength; i++) {
/* 561 */               output.writeUInt32NoTag(Array.getInt(array, i));
/*     */             }
/* 563 */             break;
/*     */           case 3:
/* 565 */             for (int i = 0; i < arrayLength; i++) {
/* 566 */               output.writeInt64NoTag(Array.getLong(array, i));
/*     */             }
/* 568 */             break;
/*     */           case 18:
/* 570 */             for (int i = 0; i < arrayLength; i++) {
/* 571 */               output.writeSInt64NoTag(Array.getLong(array, i));
/*     */             }
/* 573 */             break;
/*     */           case 4:
/* 575 */             for (int i = 0; i < arrayLength; i++) {
/* 576 */               output.writeUInt64NoTag(Array.getLong(array, i));
/*     */             }
/* 578 */             break;
/*     */           case 14:
/* 580 */             for (int i = 0; i < arrayLength; i++) {
/* 581 */               output.writeEnumNoTag(Array.getInt(array, i));
/*     */             }
/* 583 */             break;
/*     */           case 9:
/*     */           case 10:
/*     */           case 11:
/*     */           case 12:
/*     */           default:
/* 585 */             throw new IllegalArgumentException("Unpackable type " + this.type);
/*     */           }
/*     */         }
/*     */         catch (IOException e) {
/* 589 */           throw new IllegalStateException(e);
/*     */         }
/*     */       } else {
/* 592 */         throw new IllegalArgumentException("Unexpected repeated extension tag " + this.tag + ", unequal to both non-packed variant " + this.nonPackedTag + " and packed variant " + this.packedTag);
/*     */       }
/*     */     }
/*     */ 
/*     */     private int computePackedDataSize(Object array)
/*     */     {
/* 599 */       int dataSize = 0;
/* 600 */       int arrayLength = Array.getLength(array);
/* 601 */       switch (this.type)
/*     */       {
/*     */       case 8:
/* 604 */         dataSize = arrayLength;
/* 605 */         break;
/*     */       case 2:
/*     */       case 7:
/*     */       case 15:
/* 609 */         dataSize = arrayLength * 4;
/* 610 */         break;
/*     */       case 1:
/*     */       case 6:
/*     */       case 16:
/* 614 */         dataSize = arrayLength * 8;
/* 615 */         break;
/*     */       case 5:
/* 617 */         for (int i = 0; i < arrayLength; i++) {
/* 618 */           dataSize += CodedOutputByteBufferNano.computeInt32SizeNoTag(Array.getInt(array, i));
/*     */         }
/*     */ 
/* 621 */         break;
/*     */       case 17:
/* 623 */         for (int i = 0; i < arrayLength; i++) {
/* 624 */           dataSize += CodedOutputByteBufferNano.computeSInt32SizeNoTag(Array.getInt(array, i));
/*     */         }
/*     */ 
/* 627 */         break;
/*     */       case 13:
/* 629 */         for (int i = 0; i < arrayLength; i++) {
/* 630 */           dataSize += CodedOutputByteBufferNano.computeUInt32SizeNoTag(Array.getInt(array, i));
/*     */         }
/*     */ 
/* 633 */         break;
/*     */       case 3:
/* 635 */         for (int i = 0; i < arrayLength; i++) {
/* 636 */           dataSize += CodedOutputByteBufferNano.computeInt64SizeNoTag(Array.getLong(array, i));
/*     */         }
/*     */ 
/* 639 */         break;
/*     */       case 18:
/* 641 */         for (int i = 0; i < arrayLength; i++) {
/* 642 */           dataSize += CodedOutputByteBufferNano.computeSInt64SizeNoTag(Array.getLong(array, i));
/*     */         }
/*     */ 
/* 645 */         break;
/*     */       case 4:
/* 647 */         for (int i = 0; i < arrayLength; i++) {
/* 648 */           dataSize += CodedOutputByteBufferNano.computeUInt64SizeNoTag(Array.getLong(array, i));
/*     */         }
/*     */ 
/* 651 */         break;
/*     */       case 14:
/* 653 */         for (int i = 0; i < arrayLength; i++) {
/* 654 */           dataSize += CodedOutputByteBufferNano.computeEnumSizeNoTag(Array.getInt(array, i));
/*     */         }
/*     */ 
/* 657 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       default:
/* 659 */         throw new IllegalArgumentException("Unexpected non-packable type " + this.type);
/*     */       }
/* 661 */       return dataSize;
/*     */     }
/*     */ 
/*     */     protected int computeRepeatedSerializedSize(Object array)
/*     */     {
/* 666 */       if (this.tag == this.nonPackedTag)
/*     */       {
/* 668 */         return super.computeRepeatedSerializedSize(array);
/* 669 */       }if (this.tag == this.packedTag)
/*     */       {
/* 671 */         int dataSize = computePackedDataSize(array);
/* 672 */         int payloadSize = dataSize + CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */ 
/* 674 */         return payloadSize + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
/*     */       }
/* 676 */       throw new IllegalArgumentException("Unexpected repeated extension tag " + this.tag + ", unequal to both non-packed variant " + this.nonPackedTag + " and packed variant " + this.packedTag);
/*     */     }
/*     */ 
/*     */     protected final int computeSingularSerializedSize(Object value)
/*     */     {
/* 684 */       int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
/* 685 */       switch (this.type) {
/*     */       case 1:
/* 687 */         Double doubleValue = (Double)value;
/* 688 */         return CodedOutputByteBufferNano.computeDoubleSize(fieldNumber, doubleValue.doubleValue());
/*     */       case 2:
/* 690 */         Float floatValue = (Float)value;
/* 691 */         return CodedOutputByteBufferNano.computeFloatSize(fieldNumber, floatValue.floatValue());
/*     */       case 3:
/* 693 */         Long int64Value = (Long)value;
/* 694 */         return CodedOutputByteBufferNano.computeInt64Size(fieldNumber, int64Value.longValue());
/*     */       case 4:
/* 696 */         Long uint64Value = (Long)value;
/* 697 */         return CodedOutputByteBufferNano.computeUInt64Size(fieldNumber, uint64Value.longValue());
/*     */       case 5:
/* 699 */         Integer int32Value = (Integer)value;
/* 700 */         return CodedOutputByteBufferNano.computeInt32Size(fieldNumber, int32Value.intValue());
/*     */       case 6:
/* 702 */         Long fixed64Value = (Long)value;
/* 703 */         return CodedOutputByteBufferNano.computeFixed64Size(fieldNumber, fixed64Value.longValue());
/*     */       case 7:
/* 705 */         Integer fixed32Value = (Integer)value;
/* 706 */         return CodedOutputByteBufferNano.computeFixed32Size(fieldNumber, fixed32Value.intValue());
/*     */       case 8:
/* 708 */         Boolean boolValue = (Boolean)value;
/* 709 */         return CodedOutputByteBufferNano.computeBoolSize(fieldNumber, boolValue.booleanValue());
/*     */       case 9:
/* 711 */         String stringValue = (String)value;
/* 712 */         return CodedOutputByteBufferNano.computeStringSize(fieldNumber, stringValue);
/*     */       case 12:
/* 714 */         byte[] bytesValue = (byte[])value;
/* 715 */         return CodedOutputByteBufferNano.computeBytesSize(fieldNumber, bytesValue);
/*     */       case 13:
/* 717 */         Integer uint32Value = (Integer)value;
/* 718 */         return CodedOutputByteBufferNano.computeUInt32Size(fieldNumber, uint32Value.intValue());
/*     */       case 14:
/* 720 */         Integer enumValue = (Integer)value;
/* 721 */         return CodedOutputByteBufferNano.computeEnumSize(fieldNumber, enumValue.intValue());
/*     */       case 15:
/* 723 */         Integer sfixed32Value = (Integer)value;
/* 724 */         return CodedOutputByteBufferNano.computeSFixed32Size(fieldNumber, sfixed32Value.intValue());
/*     */       case 16:
/* 727 */         Long sfixed64Value = (Long)value;
/* 728 */         return CodedOutputByteBufferNano.computeSFixed64Size(fieldNumber, sfixed64Value.longValue());
/*     */       case 17:
/* 731 */         Integer sint32Value = (Integer)value;
/* 732 */         return CodedOutputByteBufferNano.computeSInt32Size(fieldNumber, sint32Value.intValue());
/*     */       case 18:
/* 734 */         Long sint64Value = (Long)value;
/* 735 */         return CodedOutputByteBufferNano.computeSInt64Size(fieldNumber, sint64Value.longValue());
/*     */       case 10:
/* 737 */       case 11: } throw new IllegalArgumentException("Unknown type " + this.type);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.Extension
 * JD-Core Version:    0.6.2
 */
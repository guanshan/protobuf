/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano
/*     */ {
/*     */   protected FieldArray unknownFieldData;
/*     */ 
/*     */   protected int computeSerializedSize()
/*     */   {
/*  49 */     int size = 0;
/*  50 */     if (this.unknownFieldData != null) {
/*  51 */       for (int i = 0; i < this.unknownFieldData.size(); i++) {
/*  52 */         FieldData field = this.unknownFieldData.dataAt(i);
/*  53 */         size += field.computeSerializedSize();
/*     */       }
/*     */     }
/*  56 */     return size;
/*     */   }
/*     */ 
/*     */   public void writeTo(CodedOutputByteBufferNano output) throws IOException
/*     */   {
/*  61 */     if (this.unknownFieldData == null) {
/*  62 */       return;
/*     */     }
/*  64 */     for (int i = 0; i < this.unknownFieldData.size(); i++) {
/*  65 */       FieldData field = this.unknownFieldData.dataAt(i);
/*  66 */       field.writeTo(output);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean hasExtension(Extension<M, ?> extension)
/*     */   {
/*  75 */     if (this.unknownFieldData == null) {
/*  76 */       return false;
/*     */     }
/*  78 */     FieldData field = this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag));
/*  79 */     return field != null;
/*     */   }
/*     */ 
/*     */   public final <T> T getExtension(Extension<M, T> extension)
/*     */   {
/*  86 */     if (this.unknownFieldData == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     FieldData field = this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag));
/*  90 */     return field == null ? null : field.getValue(extension);
/*     */   }
/*     */ 
/*     */   public final <T> M setExtension(Extension<M, T> extension, T value)
/*     */   {
/*  97 */     int fieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
/*  98 */     if (value == null) {
/*  99 */       if (this.unknownFieldData != null) {
/* 100 */         this.unknownFieldData.remove(fieldNumber);
/* 101 */         if (this.unknownFieldData.isEmpty())
/* 102 */           this.unknownFieldData = null;
/*     */       }
/*     */     }
/*     */     else {
/* 106 */       FieldData field = null;
/* 107 */       if (this.unknownFieldData == null)
/* 108 */         this.unknownFieldData = new FieldArray();
/*     */       else {
/* 110 */         field = this.unknownFieldData.get(fieldNumber);
/*     */       }
/* 112 */       if (field == null)
/* 113 */         this.unknownFieldData.put(fieldNumber, new FieldData(extension, value));
/*     */       else {
/* 115 */         field.setValue(extension, value);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 120 */     ExtendableMessageNano typedThis = this;
/* 121 */     return typedThis;
/*     */   }
/*     */ 
/*     */   protected final boolean storeUnknownField(CodedInputByteBufferNano input, int tag)
/*     */     throws IOException
/*     */   {
/* 140 */     int startPos = input.getPosition();
/* 141 */     if (!input.skipField(tag)) {
/* 142 */       return false;
/*     */     }
/* 144 */     int fieldNumber = WireFormatNano.getTagFieldNumber(tag);
/* 145 */     int endPos = input.getPosition();
/* 146 */     byte[] bytes = input.getData(startPos, endPos - startPos);
/* 147 */     UnknownFieldData unknownField = new UnknownFieldData(tag, bytes);
/*     */ 
/* 149 */     FieldData field = null;
/* 150 */     if (this.unknownFieldData == null)
/* 151 */       this.unknownFieldData = new FieldArray();
/*     */     else {
/* 153 */       field = this.unknownFieldData.get(fieldNumber);
/*     */     }
/* 155 */     if (field == null) {
/* 156 */       field = new FieldData();
/* 157 */       this.unknownFieldData.put(fieldNumber, field);
/*     */     }
/* 159 */     field.addUnknownField(unknownField);
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   public M clone() throws CloneNotSupportedException
/*     */   {
/* 165 */     ExtendableMessageNano cloned = (ExtendableMessageNano)super.clone();
/* 166 */     InternalNano.cloneUnknownFieldData(this, cloned);
/* 167 */     return cloned;
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.ExtendableMessageNano
 * JD-Core Version:    0.6.2
 */
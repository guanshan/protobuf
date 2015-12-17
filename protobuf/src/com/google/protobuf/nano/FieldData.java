/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ class FieldData
/*     */   implements Cloneable
/*     */ {
/*     */   private Extension<?, ?> cachedExtension;
/*     */   private Object value;
/*     */   private List<UnknownFieldData> unknownFieldData;
/*     */ 
/*     */   <T> FieldData(Extension<?, T> extension, T newValue)
/*     */   {
/*  49 */     this.cachedExtension = extension;
/*  50 */     this.value = newValue;
/*     */   }
/*     */ 
/*     */   FieldData() {
/*  54 */     this.unknownFieldData = new ArrayList();
/*     */   }
/*     */ 
/*     */   void addUnknownField(UnknownFieldData unknownField) {
/*  58 */     this.unknownFieldData.add(unknownField);
/*     */   }
/*     */ 
/*     */   UnknownFieldData getUnknownField(int index) {
/*  62 */     if (this.unknownFieldData == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     if (index < this.unknownFieldData.size()) {
/*  66 */       return (UnknownFieldData)this.unknownFieldData.get(index);
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   int getUnknownFieldSize() {
/*  72 */     if (this.unknownFieldData == null) {
/*  73 */       return 0;
/*     */     }
/*  75 */     return this.unknownFieldData.size();
/*     */   }
/*     */ 
/*     */   <T> T getValue(Extension<?, T> extension) {
/*  79 */     if (this.value != null) {
/*  80 */       if (this.cachedExtension != extension)
/*  81 */         throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
/*     */     }
/*     */     else
/*     */     {
/*  85 */       this.cachedExtension = extension;
/*  86 */       this.value = extension.getValueFrom(this.unknownFieldData);
/*  87 */       this.unknownFieldData = null;
/*     */     }
/*  89 */     return this.value;
/*     */   }
/*     */ 
/*     */   <T> void setValue(Extension<?, T> extension, T newValue) {
/*  93 */     this.cachedExtension = extension;
/*  94 */     this.value = newValue;
/*  95 */     this.unknownFieldData = null;
/*     */   }
/*     */ 
/*     */   int computeSerializedSize() {
/*  99 */     int size = 0;
/* 100 */     if (this.value != null)
/* 101 */       size = this.cachedExtension.computeSerializedSize(this.value);
/*     */     else {
/* 103 */       for (UnknownFieldData unknownField : this.unknownFieldData) {
/* 104 */         size += unknownField.computeSerializedSize();
/*     */       }
/*     */     }
/* 107 */     return size;
/*     */   }
/*     */ 
/*     */   void writeTo(CodedOutputByteBufferNano output) throws IOException {
/* 111 */     if (this.value != null)
/* 112 */       this.cachedExtension.writeTo(this.value, output);
/*     */     else
/* 114 */       for (UnknownFieldData unknownField : this.unknownFieldData)
/* 115 */         unknownField.writeTo(output);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 122 */     if (o == this) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (!(o instanceof FieldData)) {
/* 126 */       return false;
/*     */     }
/*     */ 
/* 129 */     FieldData other = (FieldData)o;
/* 130 */     if ((this.value != null) && (other.value != null))
/*     */     {
/* 134 */       if (this.cachedExtension != other.cachedExtension) {
/* 135 */         return false;
/*     */       }
/* 137 */       if (!this.cachedExtension.clazz.isArray())
/*     */       {
/* 139 */         return this.value.equals(other.value);
/*     */       }
/* 141 */       if ((this.value instanceof byte[]))
/* 142 */         return Arrays.equals((byte[])this.value, (byte[])other.value);
/* 143 */       if ((this.value instanceof int[]))
/* 144 */         return Arrays.equals((int[])this.value, (int[])other.value);
/* 145 */       if ((this.value instanceof long[]))
/* 146 */         return Arrays.equals((long[])this.value, (long[])other.value);
/* 147 */       if ((this.value instanceof float[]))
/* 148 */         return Arrays.equals((float[])this.value, (float[])other.value);
/* 149 */       if ((this.value instanceof double[]))
/* 150 */         return Arrays.equals((double[])this.value, (double[])other.value);
/* 151 */       if ((this.value instanceof boolean[])) {
/* 152 */         return Arrays.equals((boolean[])this.value, (boolean[])other.value);
/*     */       }
/* 154 */       return Arrays.deepEquals((Object[])this.value, (Object[])other.value);
/*     */     }
/*     */ 
/* 157 */     if ((this.unknownFieldData != null) && (other.unknownFieldData != null))
/*     */     {
/* 159 */       return this.unknownFieldData.equals(other.unknownFieldData);
/*     */     }
/*     */     try
/*     */     {
/* 163 */       return Arrays.equals(toByteArray(), other.toByteArray());
/*     */     }
/*     */     catch (IOException e) {
/* 166 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 172 */     int result = 17;
/*     */     try
/*     */     {
/* 175 */       result = 31 * result + Arrays.hashCode(toByteArray());
/*     */     }
/*     */     catch (IOException e) {
/* 178 */       throw new IllegalStateException(e);
/*     */     }
/* 180 */     return result;
/*     */   }
/*     */ 
/*     */   private byte[] toByteArray() throws IOException {
/* 184 */     byte[] result = new byte[computeSerializedSize()];
/* 185 */     CodedOutputByteBufferNano output = CodedOutputByteBufferNano.newInstance(result);
/* 186 */     writeTo(output);
/* 187 */     return result;
/*     */   }
/*     */ 
/*     */   public final FieldData clone()
/*     */   {
/* 192 */     FieldData clone = new FieldData();
/*     */     try {
/* 194 */       clone.cachedExtension = this.cachedExtension;
/* 195 */       if (this.unknownFieldData == null)
/* 196 */         clone.unknownFieldData = null;
/*     */       else {
/* 198 */         clone.unknownFieldData.addAll(this.unknownFieldData);
/*     */       }
/*     */ 
/* 204 */       if (this.value != null)
/*     */       {
/* 206 */         if ((this.value instanceof MessageNano)) {
/* 207 */           clone.value = ((MessageNano)this.value).clone();
/* 208 */         } else if ((this.value instanceof byte[])) {
/* 209 */           clone.value = ((byte[])this.value).clone();
/* 210 */         } else if ((this.value instanceof byte[][])) {
/* 211 */           byte[][] valueArray = (byte[][])this.value;
/* 212 */           byte[][] cloneArray = new byte[valueArray.length][];
/* 213 */           clone.value = cloneArray;
/* 214 */           for (int i = 0; i < valueArray.length; i++)
/* 215 */             cloneArray[i] = ((byte[])valueArray[i].clone());
/*     */         }
/* 217 */         else if ((this.value instanceof boolean[])) {
/* 218 */           clone.value = ((boolean[])this.value).clone();
/* 219 */         } else if ((this.value instanceof int[])) {
/* 220 */           clone.value = ((int[])this.value).clone();
/* 221 */         } else if ((this.value instanceof long[])) {
/* 222 */           clone.value = ((long[])this.value).clone();
/* 223 */         } else if ((this.value instanceof float[])) {
/* 224 */           clone.value = ((float[])this.value).clone();
/* 225 */         } else if ((this.value instanceof double[])) {
/* 226 */           clone.value = ((double[])this.value).clone();
/* 227 */         } else if ((this.value instanceof MessageNano[])) {
/* 228 */           MessageNano[] valueArray = (MessageNano[])this.value;
/* 229 */           MessageNano[] cloneArray = new MessageNano[valueArray.length];
/* 230 */           clone.value = cloneArray;
/* 231 */           for (int i = 0; i < valueArray.length; i++)
/* 232 */             cloneArray[i] = valueArray[i].clone();
/*     */         }
/*     */       }
/* 235 */       return clone;
/*     */     } catch (CloneNotSupportedException e) {
/* 237 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.FieldData
 * JD-Core Version:    0.6.2
 */
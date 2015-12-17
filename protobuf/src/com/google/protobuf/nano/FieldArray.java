/*     */ package com.google.protobuf.nano;
/*     */ 
/*     */ public final class FieldArray
/*     */   implements Cloneable
/*     */ {
/*  44 */   private static final FieldData DELETED = new FieldData();
/*  45 */   private boolean mGarbage = false;
/*     */   private int[] mFieldNumbers;
/*     */   private FieldData[] mData;
/*     */   private int mSize;
/*     */ 
/*     */   FieldArray()
/*     */   {
/*  55 */     this(10);
/*     */   }
/*     */ 
/*     */   FieldArray(int initialCapacity)
/*     */   {
/*  64 */     initialCapacity = idealIntArraySize(initialCapacity);
/*  65 */     this.mFieldNumbers = new int[initialCapacity];
/*  66 */     this.mData = new FieldData[initialCapacity];
/*  67 */     this.mSize = 0;
/*     */   }
/*     */ 
/*     */   FieldData get(int fieldNumber)
/*     */   {
/*  75 */     int i = binarySearch(fieldNumber);
/*     */ 
/*  77 */     if ((i < 0) || (this.mData[i] == DELETED)) {
/*  78 */       return null;
/*     */     }
/*  80 */     return this.mData[i];
/*     */   }
/*     */ 
/*     */   void remove(int fieldNumber)
/*     */   {
/*  88 */     int i = binarySearch(fieldNumber);
/*     */ 
/*  90 */     if ((i >= 0) && (this.mData[i] != DELETED)) {
/*  91 */       this.mData[i] = DELETED;
/*  92 */       this.mGarbage = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void gc() {
/*  97 */     int n = this.mSize;
/*  98 */     int o = 0;
/*  99 */     int[] keys = this.mFieldNumbers;
/* 100 */     FieldData[] values = this.mData;
/*     */ 
/* 102 */     for (int i = 0; i < n; i++) {
/* 103 */       FieldData val = values[i];
/*     */ 
/* 105 */       if (val != DELETED) {
/* 106 */         if (i != o) {
/* 107 */           keys[o] = keys[i];
/* 108 */           values[o] = val;
/* 109 */           values[i] = null;
/*     */         }
/*     */ 
/* 112 */         o++;
/*     */       }
/*     */     }
/*     */ 
/* 116 */     this.mGarbage = false;
/* 117 */     this.mSize = o;
/*     */   }
/*     */ 
/*     */   void put(int fieldNumber, FieldData data)
/*     */   {
/* 125 */     int i = binarySearch(fieldNumber);
/*     */ 
/* 127 */     if (i >= 0) {
/* 128 */       this.mData[i] = data;
/*     */     } else {
/* 130 */       i ^= -1;
/*     */ 
/* 132 */       if ((i < this.mSize) && (this.mData[i] == DELETED)) {
/* 133 */         this.mFieldNumbers[i] = fieldNumber;
/* 134 */         this.mData[i] = data;
/* 135 */         return;
/*     */       }
/*     */ 
/* 138 */       if ((this.mGarbage) && (this.mSize >= this.mFieldNumbers.length)) {
/* 139 */         gc();
/*     */ 
/* 142 */         i = binarySearch(fieldNumber) ^ 0xFFFFFFFF;
/*     */       }
/*     */ 
/* 145 */       if (this.mSize >= this.mFieldNumbers.length) {
/* 146 */         int n = idealIntArraySize(this.mSize + 1);
/*     */ 
/* 148 */         int[] nkeys = new int[n];
/* 149 */         FieldData[] nvalues = new FieldData[n];
/*     */ 
/* 151 */         System.arraycopy(this.mFieldNumbers, 0, nkeys, 0, this.mFieldNumbers.length);
/* 152 */         System.arraycopy(this.mData, 0, nvalues, 0, this.mData.length);
/*     */ 
/* 154 */         this.mFieldNumbers = nkeys;
/* 155 */         this.mData = nvalues;
/*     */       }
/*     */ 
/* 158 */       if (this.mSize - i != 0) {
/* 159 */         System.arraycopy(this.mFieldNumbers, i, this.mFieldNumbers, i + 1, this.mSize - i);
/* 160 */         System.arraycopy(this.mData, i, this.mData, i + 1, this.mSize - i);
/*     */       }
/*     */ 
/* 163 */       this.mFieldNumbers[i] = fieldNumber;
/* 164 */       this.mData[i] = data;
/* 165 */       this.mSize += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   int size()
/*     */   {
/* 174 */     if (this.mGarbage) {
/* 175 */       gc();
/*     */     }
/*     */ 
/* 178 */     return this.mSize;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 182 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   FieldData dataAt(int index)
/*     */   {
/* 191 */     if (this.mGarbage) {
/* 192 */       gc();
/*     */     }
/*     */ 
/* 195 */     return this.mData[index];
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 200 */     if (o == this) {
/* 201 */       return true;
/*     */     }
/* 203 */     if (!(o instanceof FieldArray)) {
/* 204 */       return false;
/*     */     }
/*     */ 
/* 207 */     FieldArray other = (FieldArray)o;
/* 208 */     if (size() != other.size()) {
/* 209 */       return false;
/*     */     }
/* 211 */     return (arrayEquals(this.mFieldNumbers, other.mFieldNumbers, this.mSize)) && (arrayEquals(this.mData, other.mData, this.mSize));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 217 */     if (this.mGarbage) {
/* 218 */       gc();
/*     */     }
/* 220 */     int result = 17;
/* 221 */     for (int i = 0; i < this.mSize; i++) {
/* 222 */       result = 31 * result + this.mFieldNumbers[i];
/* 223 */       result = 31 * result + this.mData[i].hashCode();
/*     */     }
/* 225 */     return result;
/*     */   }
/*     */ 
/*     */   private int idealIntArraySize(int need) {
/* 229 */     return idealByteArraySize(need * 4) / 4;
/*     */   }
/*     */ 
/*     */   private int idealByteArraySize(int need) {
/* 233 */     for (int i = 4; i < 32; i++) {
/* 234 */       if (need <= (1 << i) - 12)
/* 235 */         return (1 << i) - 12;
/*     */     }
/* 237 */     return need;
/*     */   }
/*     */ 
/*     */   private int binarySearch(int value) {
/* 241 */     int lo = 0;
/* 242 */     int hi = this.mSize - 1;
/*     */ 
/* 244 */     while (lo <= hi) {
/* 245 */       int mid = lo + hi >>> 1;
/* 246 */       int midVal = this.mFieldNumbers[mid];
/*     */ 
/* 248 */       if (midVal < value)
/* 249 */         lo = mid + 1;
/* 250 */       else if (midVal > value)
/* 251 */         hi = mid - 1;
/*     */       else {
/* 253 */         return mid;
/*     */       }
/*     */     }
/* 256 */     return lo ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   private boolean arrayEquals(int[] a, int[] b, int size) {
/* 260 */     for (int i = 0; i < size; i++) {
/* 261 */       if (a[i] != b[i]) {
/* 262 */         return false;
/*     */       }
/*     */     }
/* 265 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean arrayEquals(FieldData[] a, FieldData[] b, int size) {
/* 269 */     for (int i = 0; i < size; i++) {
/* 270 */       if (!a[i].equals(b[i])) {
/* 271 */         return false;
/*     */       }
/*     */     }
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   public final FieldArray clone()
/*     */   {
/* 280 */     int size = size();
/* 281 */     FieldArray clone = new FieldArray(size);
/* 282 */     System.arraycopy(this.mFieldNumbers, 0, clone.mFieldNumbers, 0, size);
/* 283 */     for (int i = 0; i < size; i++) {
/* 284 */       if (this.mData[i] != null) {
/* 285 */         clone.mData[i] = this.mData[i].clone();
/*     */       }
/*     */     }
/* 288 */     clone.mSize = size;
/* 289 */     return clone;
/*     */   }
/*     */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.FieldArray
 * JD-Core Version:    0.6.2
 */
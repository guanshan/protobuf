/*    */ package com.google.protobuf.nano;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ final class UnknownFieldData
/*    */ {
/*    */   final int tag;
/*    */   final byte[] bytes;
/*    */ 
/*    */   UnknownFieldData(int tag, byte[] bytes)
/*    */   {
/* 52 */     this.tag = tag;
/* 53 */     this.bytes = bytes;
/*    */   }
/*    */ 
/*    */   int computeSerializedSize() {
/* 57 */     int size = 0;
/* 58 */     size += CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
/* 59 */     size += this.bytes.length;
/* 60 */     return size;
/*    */   }
/*    */ 
/*    */   void writeTo(CodedOutputByteBufferNano output) throws IOException {
/* 64 */     output.writeRawVarint32(this.tag);
/* 65 */     output.writeRawBytes(this.bytes);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 70 */     if (o == this) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (!(o instanceof UnknownFieldData)) {
/* 74 */       return false;
/*    */     }
/*    */ 
/* 77 */     UnknownFieldData other = (UnknownFieldData)o;
/* 78 */     return (this.tag == other.tag) && (Arrays.equals(this.bytes, other.bytes));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 83 */     int result = 17;
/* 84 */     result = 31 * result + this.tag;
/* 85 */     result = 31 * result + Arrays.hashCode(this.bytes);
/* 86 */     return result;
/*    */   }
/*    */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.UnknownFieldData
 * JD-Core Version:    0.6.2
 */
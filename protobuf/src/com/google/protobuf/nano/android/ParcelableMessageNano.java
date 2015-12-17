/*    */ package com.google.protobuf.nano.android;
/*    */ 
/*    */ import android.os.Parcel;
/*    */ import android.os.Parcelable;
/*    */ import com.google.protobuf.nano.MessageNano;
/*    */ 
/*    */ public abstract class ParcelableMessageNano extends MessageNano
/*    */   implements Parcelable
/*    */ {
/*    */   public int describeContents()
/*    */   {
/* 45 */     return 0;
/*    */   }
/*    */ 
/*    */   public void writeToParcel(Parcel out, int flags)
/*    */   {
/* 50 */     ParcelableMessageNanoCreator.writeToParcel(getClass(), this, out);
/*    */   }
/*    */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.android.ParcelableMessageNano
 * JD-Core Version:    0.6.2
 */
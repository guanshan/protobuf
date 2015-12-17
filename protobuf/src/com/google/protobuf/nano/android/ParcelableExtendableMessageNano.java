/*    */ package com.google.protobuf.nano.android;
/*    */ 
/*    */ import android.os.Parcel;
/*    */ import android.os.Parcelable;
/*    */ import com.google.protobuf.nano.ExtendableMessageNano;
/*    */ 
/*    */ public abstract class ParcelableExtendableMessageNano<M extends ExtendableMessageNano<M>> extends ExtendableMessageNano<M>
/*    */   implements Parcelable
/*    */ {
/*    */   public int describeContents()
/*    */   {
/* 47 */     return 0;
/*    */   }
/*    */ 
/*    */   public void writeToParcel(Parcel out, int flags)
/*    */   {
/* 52 */     ParcelableMessageNanoCreator.writeToParcel(getClass(), this, out);
/*    */   }
/*    */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.android.ParcelableExtendableMessageNano
 * JD-Core Version:    0.6.2
 */
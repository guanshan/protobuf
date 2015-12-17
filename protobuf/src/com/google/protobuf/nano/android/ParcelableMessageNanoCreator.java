/*    */ package com.google.protobuf.nano.android;
/*    */ 
/*    */ import android.os.Parcel;
/*    */ import android.os.Parcelable.Creator;
/*    */ import android.util.Log;
/*    */ import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
/*    */ import com.google.protobuf.nano.MessageNano;
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ public final class ParcelableMessageNanoCreator<T extends MessageNano>
/*    */   implements Parcelable.Creator<T>
/*    */ {
/*    */   private static final String TAG = "PMNCreator";
/*    */   private final Class<T> mClazz;
/*    */ 
/*    */   public ParcelableMessageNanoCreator(Class<T> clazz)
/*    */   {
/* 49 */     this.mClazz = clazz;
/*    */   }
/*    */ 
/*    */   public T createFromParcel(Parcel in)
/*    */   {
/* 55 */     String className = in.readString();
/* 56 */     byte[] data = in.createByteArray();
/*    */ 
/* 58 */     MessageNano proto = null;
/*    */     try
/*    */     {
/* 61 */       Class clazz = Class.forName(className);
/* 62 */       Object instance = clazz.newInstance();
/* 63 */       proto = (MessageNano)instance;
/* 64 */       MessageNano.mergeFrom(proto, data);
/*    */     } catch (ClassNotFoundException e) {
/* 66 */       Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
/*    */     } catch (IllegalAccessException e) {
/* 68 */       Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
/*    */     } catch (InstantiationException e) {
/* 70 */       Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
/*    */     } catch (InvalidProtocolBufferNanoException e) {
/* 72 */       Log.e("PMNCreator", "Exception trying to create proto from parcel", e);
/*    */     }
/*    */ 
/* 75 */     return proto;
/*    */   }
/*    */ 
/*    */   public T[] newArray(int i)
/*    */   {
/* 81 */     return (MessageNano[])Array.newInstance(this.mClazz, i);
/*    */   }
/*    */ 
/*    */   static <T extends MessageNano> void writeToParcel(Class<T> clazz, MessageNano message, Parcel out)
/*    */   {
/* 86 */     out.writeString(clazz.getName());
/* 87 */     out.writeByteArray(MessageNano.toByteArray(message));
/*    */   }
/*    */ }

/* Location:           /Users/guanshanshan/Documents/各种SDK相关/libprotobuf-java-2.6-nano.jar
 * Qualified Name:     com.google.protobuf.nano.android.ParcelableMessageNanoCreator
 * JD-Core Version:    0.6.2
 */
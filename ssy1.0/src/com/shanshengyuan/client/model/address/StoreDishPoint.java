package com.shanshengyuan.client.model.address;

import android.os.Parcel;
import android.os.Parcelable;

public class StoreDishPoint implements Parcelable{

	private int storeId ;
	
	private String address;
	
	private String pointXY;
	
	private String phone;

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPointXY() {
		return pointXY;
	}

	public void setPointXY(String pointXY) {
		this.pointXY = pointXY;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(storeId);  
        dest.writeString(address);  
        dest.writeString(pointXY); 
        dest.writeString(phone); 
	}
	
	
	 public static final Parcelable.Creator<StoreDishPoint> CREATOR  = new Creator<StoreDishPoint>() {
	        //实现从source中创建出类的实例的功能
	        @Override
	        public StoreDishPoint createFromParcel(Parcel source) {
	        	StoreDishPoint parInfo  = new StoreDishPoint();
	            parInfo.storeId = source.readInt();
	            parInfo.address= source.readString();
	            parInfo.pointXY = source.readString();
	            parInfo.phone = source.readString();
	            return parInfo;
	        }
	        //创建一个类型为T，长度为size的数组
	        @Override
	        public StoreDishPoint[] newArray(int size) {
	            return new StoreDishPoint[size];
	        }
	    };   
}

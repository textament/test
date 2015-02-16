/**
 * 
 */
package com.shanshengyuan.client.model.address;

import java.util.List;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author lihao
 *
 */
public class AddressListResponse extends BaseResponse {

	List<Address> data = null;
	
	List<StoreDishPoint> storeDishPointList = null;
	public List<Address> getData() {
		return data;
	}

	public void setData(List<Address> data) {
		this.data = data;
	}

	public List<StoreDishPoint> getStoreDishPointList() {
		return storeDishPointList;
	}

	public void setStoreDishPointList(List<StoreDishPoint> storeDishPointList) {
		this.storeDishPointList = storeDishPointList;
	}
	
	
	
}

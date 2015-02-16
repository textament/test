/**
 * 
 */
package com.shanshengyuan.client.model.order;

import java.util.List;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class OrderListResponse extends BaseResponse {

	List<OrderList> data = null;

	public List<OrderList> getData() {
		return data;
	}

	public void setData(List<OrderList> data) {
		this.data = data;
	}
	
	
}

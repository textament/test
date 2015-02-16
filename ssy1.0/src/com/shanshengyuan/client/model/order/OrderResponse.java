/**
 * 
 */
package com.shanshengyuan.client.model.order;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author lihao
 *
 */
public class OrderResponse extends BaseResponse {

	private Order data = null;

	public Order getData() {
		return data;
	}

	public void setData(Order data) {
		this.data = data;
	}
	
	
}

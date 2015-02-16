/**
 * 
 */
package com.shanshengyuan.client.model.order;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class OrderFinishResponse extends BaseResponse {
     
	OrderFinish data = null;

	public OrderFinish getData() {
		return data;
	}

	public void setData(OrderFinish data) {
		this.data = data;
	}
	
	
}

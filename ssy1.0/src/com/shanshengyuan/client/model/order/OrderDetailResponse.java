/**
 * 
 */
package com.shanshengyuan.client.model.order;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class OrderDetailResponse extends BaseResponse {

		OrderDetail data = null;

		public OrderDetail getData() {
			return data;
		}

		public void setData(OrderDetail data) {
			this.data = data;
		}
		
		
	
}

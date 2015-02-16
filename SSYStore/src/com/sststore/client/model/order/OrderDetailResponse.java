/**
 * 
 */
package com.sststore.client.model.order;

import com.sststore.client.BaseResponse;

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

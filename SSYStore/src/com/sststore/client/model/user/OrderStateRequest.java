/**
 * 
 */
package com.sststore.client.model.user;

import com.sststore.client.BaseRequest;

/**
 * @author lihao
 *
 */
public class OrderStateRequest extends BaseRequest {

	private String orderid;
	
	private String userid;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
}

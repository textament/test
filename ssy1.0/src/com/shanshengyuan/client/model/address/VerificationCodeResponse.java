/**
 * 
 */
package com.shanshengyuan.client.model.address;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class VerificationCodeResponse extends BaseResponse {

	VerificationCode data;

	public VerificationCode getData() {
		return data;
	}

	public void setData(VerificationCode data) {
		this.data = data;
	}
	
	
}

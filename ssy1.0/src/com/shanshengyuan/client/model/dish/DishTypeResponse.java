/**
 * 
 */
package com.shanshengyuan.client.model.dish;

import java.util.List;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author lihao
 *
 */
public class DishTypeResponse extends BaseResponse {
	
	private List<DishType> data = null;

	public List<DishType> getData() {
		return data;
	}
	public void setData(List<DishType> data) {
		this.data = data;
	}

}

/**
 * 
 */
package com.shanshengyuan.client.model.dish;

import java.util.List;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class DishListResponse extends BaseResponse {

	private List<Dishes> data = null;

	public List<Dishes> getData() {
		return data;
	}
	public void setData(List<Dishes> data) {
		this.data = data;
	}
}

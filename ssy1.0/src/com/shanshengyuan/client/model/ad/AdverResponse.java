/**
 * AdverResponse.java
 * com.zhubajie.client.model.ad
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-25 		lihao
 *
 *  Copyright (c) 2013 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.model.ad;

import java.util.List;

import com.shanshengyuan.client.BaseResponse;

/**
 * ClassName:AdverResponse Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2013-12-25
 * 
 * @see
 * 
 */
public class AdverResponse extends BaseResponse {

	private List<Adver> data = null;

	public List<Adver> getData() {
		return data;
	}
	public void setData(List<Adver> data) {
		this.data = data;
	}
}

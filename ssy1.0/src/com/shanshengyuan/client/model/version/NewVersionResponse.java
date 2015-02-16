/**
 * 
 */
package com.shanshengyuan.client.model.version;

import com.shanshengyuan.client.BaseResponse;

/**
 * @author Administrator
 *
 */
public class NewVersionResponse extends BaseResponse {

	int updateState;
	
	String updateUrl;
	
	String updateMessage;
	

	

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getUpdateMessage() {
		return updateMessage;
	}

	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

	public int getUpdateState() {
		return updateState;
	}

	public void setUpdateState(int updateState) {
		this.updateState = updateState;
	}
	
	
}

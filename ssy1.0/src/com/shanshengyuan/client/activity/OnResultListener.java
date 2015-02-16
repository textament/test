package com.shanshengyuan.client.activity;

import com.shanshengyuan.client.BaseResponse;

public interface OnResultListener {
	public void onSuccess(int actionType);

	public void onFailed(int actionType, BaseResponse result);
}

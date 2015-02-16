package com.sststore.client.activity;

import com.sststore.client.BaseResponse;

public interface OnResultListener {
	public void onSuccess(int actionType);

	public void onFailed(int actionType, BaseResponse result);
}

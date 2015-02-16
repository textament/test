package com.sststore.client.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sststore.client.AsyncTaskPayload;
import com.sststore.client.BaseRequest;
import com.sststore.client.BaseResponse;
import com.sststore.client.Config;
import com.sststore.client.ErrorResponse;
import com.sststore.client.activity.OnResultListener;
import com.sststore.client.utils.JSONHelper;
import com.sststore.client.utils.Log;
import com.sststore.client.utils.LogManager;
import com.sststore.client.utils.StringUtils;
import com.sststore.client.widgets.CustomDialog;

public class BaseController {
	protected CustomDialog progressDialog;
	protected ErrorResponse er = null;
	protected OnResultListener mListener;
	protected Context mContext;
	public boolean isActivityRun;

	public boolean isActivityRun() {
		return isActivityRun;
	}

	public void setActivityRun(boolean isActivityRun) {
		this.isActivityRun = isActivityRun;
	}

	public BaseController(Context self) {
		mContext = self;
		isActivityRun = true;
	}

	protected void dismisDialog() {
		handler.sendEmptyMessage(1);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isActivityRun == false) {
				return;
			}
			switch (msg.what) {
			case 0:
				if (progressDialog == null) {
					progressDialog = new CustomDialog(mContext,
							android.R.style.Theme_Translucent_NoTitleBar);

				}
				String hasProcess = msg.getData().getString("PRO");
				if (StringUtils.isEmpty(hasProcess)) {
					progressDialog.show();
				}
				break;
			case 1:
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				break;
			default:
				break;
			}
		};
	};

	protected void showDialog(String hasProcess) {
		Message msg = new Message();
		msg.what = 0;
		Bundle data = new Bundle();
		data.putString("PRO", hasProcess);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	protected boolean checkErrorResponse(final AsyncTaskPayload payload,
			BaseResponse response, boolean isShow) {
		if (response instanceof ErrorResponse) {
			if (StringUtils.isEmpty(response.getVerification())) {
				if (response.getResult() != 10047 && response.getResult() != 10) {
					if (isShow && !StringUtils.isEmpty(response.getMsg())) {
						Toast.makeText(mContext.getApplicationContext(),
								response.getMsg(), Toast.LENGTH_LONG).show();
					}
				}
			}
			payload.setResponse(response);
			Log.e("error:" + response.getResult(), response.getMsg() + "");
			onfailure(payload, response.getMsg());
			return true;
		}
		if(response.getStatus()==0){
			onfailure(payload, response.getMsg());
			return true;
		}
		
		return false;
	}

	protected boolean checkErrorResponse(final AsyncTaskPayload payload,
			BaseResponse response) {
		if (Config.DEBUG && !Config.GATEWAY_URL.contains("i.api.zhubajie.com")
				) {
			BaseRequest request = (BaseRequest) payload.getData()[0];
			String jsonStr = JSONHelper.objToJson(request);
			LogManager.getInstance().insertLog(jsonStr);
			if (LogManager.getInstance().isDoWeb()) {
				onfailure(payload, "网络请求执行成功");
				return true;
			}
		}
		return checkErrorResponse(payload, response, true);
	
	}

	protected void onfailure(final AsyncTaskPayload payload) {
		onfailure(payload, null);
	}

	protected void onfailures(final AsyncTaskPayload payload, String errstr) {
		onfailure(payload, errstr);
	}

	protected void onfailure(final AsyncTaskPayload payload, String errString) {
		if (!(payload.getResponse() instanceof ErrorResponse)) {
			if (errString == null) {
				er = new ErrorResponse("数据加载出错，请稍后再试..");
			} else {
				er = new ErrorResponse(errString);
				if (!errString.contains("html")) {
					Toast.makeText(mContext.getApplicationContext(), errString,
							Toast.LENGTH_LONG).show();
				}

			}
			payload.setResponse(er);
		}
		if (mListener != null)
			mListener.onFailed(payload.getTaskType(), payload.getResponse());
		dismisDialog();
	}

}

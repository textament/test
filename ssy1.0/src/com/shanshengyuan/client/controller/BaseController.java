package com.shanshengyuan.client.controller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.shanshengyuan.client.AsyncTaskPayload;
import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.Config;
import com.shanshengyuan.client.ErrorResponse;
import com.shanshengyuan.client.activity.OnResultListener;
import com.shanshengyuan.client.utils.JSONHelper;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.LogManager;
import com.shanshengyuan.client.utils.StringUtils;
import com.shanshengyuan.client.widgets.CustomDialog;

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
			onfailure(payload, response.getMsg(),response);
			return true;
		}
		if(response.getStatus()==0){
			onfailure(payload, response.getMsg(),response);
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
				onfailure(payload, "网络请求执行成功",response);
				return true;
			}
		}
		return checkErrorResponse(payload, response, true);
	
	}

	protected void onfailure(final AsyncTaskPayload payload) {
		onfailure(payload, null,null);
	}

	protected void onfailures(final AsyncTaskPayload payload, String errstr) {
		onfailure(payload, errstr,null);
	}

	protected void onfailure(final AsyncTaskPayload payload, String errString,BaseResponse res) {
		if (!(payload.getResponse() instanceof ErrorResponse)) {
			if (errString == null) {
				er = new ErrorResponse("数据加载出错，请稍后再试..");
			} else {
				er = new ErrorResponse(errString);
				er.setFailType(res.getFailType());
				er.setResult(res.getResult());
				er.setStatus(res.getStatus());
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

/**
 * 
 */
package com.shanshengyuan.client.controller;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.AsyncTaskPayload;
import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.ServiceConstants;
import com.shanshengyuan.client.activity.OnResultListener;
import com.shanshengyuan.client.model.ad.Adver;
import com.shanshengyuan.client.model.ad.AdverRequest;
import com.shanshengyuan.client.model.ad.AdverResponse;
import com.shanshengyuan.client.utils.JSONHelper;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.NetworkHelper;
import com.shanshengyuan.client.utils.StringUtils;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *
 */
public class ADController extends BaseController {
	
	private static final String TAG = ADController.class.getSimpleName();
	
	private List<Adver> mAdList = null;
	
	String err;
	
	
	

	public List<Adver> getmAdList() {
		return mAdList;
	}

	public void setmAdList(List<Adver> mAdList) {
		this.mAdList = mAdList;
	}

	public ADController(Context self, OnResultListener l) {
		super(self);
		mListener = l;
	}

	public void execute(int actionType, BaseRequest request) {
		showDialog(request.getHasProcessDialog());
		final AsyncTaskPayload payload = new AsyncTaskPayload(actionType,
				new Object[] { request });
		switch (payload.getTaskType()) {
		case Actions.ACTION_AD:
			doGetAdList(payload);
			break;
		default:
			break;
		}
	}

	private void doGetAdList(final AsyncTaskPayload payload) {
		AdverRequest request = (AdverRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_AD);
		Log.i(TAG + " url: =====>", url);
		Log.i(TAG + " jsonStr: =====>", jsonStr);
		NetworkHelper.asyncHttpClient.post(mContext, url, entity,
				"application/json; charset=utf-8",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						Log.i(TAG + " Response: =====>", content);
						BaseResponse response = null;
						response = NetworkHelper.doObject(url, content,
								AdverResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof AdverResponse) {
							mAdList = ((AdverResponse) response).getData();
						}
						dismisDialog();
						mListener.onSuccess(payload.getTaskType());
					}

					@Override
					protected void handleFailureMessage(Throwable e,
							String responseBody) {
						err = responseBody;
						super.handleFailureMessage(e, responseBody);
					}

					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
						if(StringUtils.isEmpty(err)){
							onfailure(payload);
						}else{
							onfailures(payload,err);	
						}
					
					}
				});

	}
	
}

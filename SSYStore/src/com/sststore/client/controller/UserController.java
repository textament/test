/**
 * 
 */
package com.sststore.client.controller;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;

import com.sststore.client.Actions;
import com.sststore.client.AsyncTaskPayload;
import com.sststore.client.BaseRequest;
import com.sststore.client.BaseResponse;
import com.sststore.client.ServiceConstants;
import com.sststore.client.activity.OnResultListener;
import com.sststore.client.model.user.UserInfo;
import com.sststore.client.model.user.UserRequest;
import com.sststore.client.model.user.UserResponse;
import com.sststore.client.utils.JSONHelper;
import com.sststore.client.utils.Log;
import com.sststore.client.utils.NetworkHelper;
import com.sststore.client.utils.Settings;
import com.sststore.client.utils.StringUtils;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 * 
 */
public class UserController extends BaseController {

	private static final String TAG = UserController.class.getSimpleName();

	private static UserInfo mUser;

	String err;
	
	

	public static UserInfo getmUser() {
		return mUser;
	}

	public static void setmUser(UserInfo mUser) {
		UserController.mUser = mUser;
	}

	public UserController(Context self, OnResultListener l) {
		super(self);
		mListener = l;
	}

	public void execute(int actionType, BaseRequest request) {
		showDialog(request.getHasProcessDialog());
		final AsyncTaskPayload payload = new AsyncTaskPayload(actionType,
				new Object[] { request });
		switch (payload.getTaskType()) {
		case Actions.ACTION_LOGIN:
			doGetLogin(payload);
			break;
		default:
			break;
		}
	}

	private void doGetLogin(final AsyncTaskPayload payload) {
		final UserRequest request = (UserRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);

		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_LOGIN);
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
								UserResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof UserResponse) {
							UserInfo u = ((UserResponse) response).getData();
							if(mUser==null){
								mUser = new UserInfo();
								mUser.setId(u.getId());
								mUser.setName(request.getUsername());
							}else{
								mUser.setId(u.getId());
								mUser.setName(request.getUsername());
							}
							Settings.saveUserInfo(mUser);
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
						if (StringUtils.isEmpty(err)) {
							onfailure(payload);
						} else {
							onfailures(payload, err);
						}

					}
				});

	}
}

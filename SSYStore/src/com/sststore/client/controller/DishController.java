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
import com.sststore.client.model.order.OrderDetail;
import com.sststore.client.model.order.OrderDetailRequest;
import com.sststore.client.model.order.OrderDetailResponse;
import com.sststore.client.utils.JSONHelper;
import com.sststore.client.utils.Log;
import com.sststore.client.utils.NetworkHelper;
import com.sststore.client.utils.StringUtils;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *
 */
public class DishController extends BaseController {


private static final String TAG = DishController.class.getSimpleName();
	
	
	String err;
	
	private OrderDetail mOrderDetail;
	
	

	public OrderDetail getmOrderDetail() {
		return mOrderDetail;
	}

	public void setmOrderDetail(OrderDetail mOrderDetail) {
		this.mOrderDetail = mOrderDetail;
	}


	public DishController(Context self, OnResultListener l) {
		super(self);
		mListener = l;
	}

	public void execute(int actionType, BaseRequest request) {
		showDialog(request.getHasProcessDialog());
		final AsyncTaskPayload payload = new AsyncTaskPayload(actionType,
				new Object[] { request });
		switch (payload.getTaskType()) {
		case Actions.ACTION_ORDER_DETAIL:
			doGetOrderDetail(payload);
			break;
		case Actions.ACTION_ORDER_UPDATE:
			doGetOrderUpdate(payload);
		default:
			break;
		}
	}
	
	private void doGetOrderUpdate(final AsyncTaskPayload payload) {
		OrderDetailRequest request = (OrderDetailRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ORDER_UPDATE);
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
								BaseResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
//						if (response instanceof OrderDetailResponse) {
//							mOrderDetail = ((OrderDetailResponse) response).getData();
//						}
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
		
	private void doGetOrderDetail(final AsyncTaskPayload payload) {
		OrderDetailRequest request = (OrderDetailRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ORDER_DETAIL);
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
								OrderDetailResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof OrderDetailResponse) {
							mOrderDetail = ((OrderDetailResponse) response).getData();
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

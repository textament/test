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
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.ServiceConstants;
import com.shanshengyuan.client.activity.OnResultListener;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.AddressAddRequest;
import com.shanshengyuan.client.model.address.AddressEditRequest;
import com.shanshengyuan.client.model.address.AddressListRequest;
import com.shanshengyuan.client.model.address.AddressListResponse;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.model.address.VerificationCode;
import com.shanshengyuan.client.model.address.VerificationCodeRequest;
import com.shanshengyuan.client.model.address.VerificationCodeResponse;
import com.shanshengyuan.client.model.arrviedVolume.ArrviedVolumeRequest;
import com.shanshengyuan.client.model.arrviedVolume.ArrviedVolumeResponse;
import com.shanshengyuan.client.model.arrviedVolume.arrviedVolume;
import com.shanshengyuan.client.model.redpackage.MyNum;
import com.shanshengyuan.client.model.redpackage.MyNumRequest;
import com.shanshengyuan.client.model.redpackage.MyNumResponse;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.model.redpackage.MyredRequest;
import com.shanshengyuan.client.model.redpackage.MyredResponse;
import com.shanshengyuan.client.model.version.NewVersionResponse;
import com.shanshengyuan.client.model.version.NewVersionResquest;
import com.shanshengyuan.client.utils.JSONHelper;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.NetworkHelper;
import com.shanshengyuan.client.utils.Settings;
import com.shanshengyuan.client.utils.StringUtils;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * @author lihao
 *
 */
public class AddressController extends BaseController {

private static final String TAG = AddressController.class.getSimpleName();
	
	String err;
	
	List<Address> mAddressList = null;
	
	List<StoreDishPoint> ponitList = null;
	
	VerificationCode mVerificationCode;

    List<MyRed> myRedLists = null;
	
	int updateState;
	
	String urlStr;
	
	String message;

    MyNum myNum;

    List<arrviedVolume> arrviedVolumes;

    public List<arrviedVolume> getArrviedVolumes() {
        return arrviedVolumes;
    }

    public void setArrviedVolumes(List<arrviedVolume> arrviedVolumes) {
        this.arrviedVolumes = arrviedVolumes;
    }

    public MyNum getMyNum() {
        return myNum;
    }

    public void setMyNum(MyNum myNum) {
        this.myNum = myNum;
    }

    public List<MyRed> getMyRedLists() {
        return myRedLists;
    }

    public void setMyRedLists(List<MyRed> myRedLists) {
        this.myRedLists = myRedLists;
    }

    public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getUpdateState() {
		return updateState;
	}

	public void setUpdateState(int updateState) {
		this.updateState = updateState;
	}

	public VerificationCode getmVerificationCode() {
		return mVerificationCode;
	}

	public void setmVerificationCode(VerificationCode mVerificationCode) {
		this.mVerificationCode = mVerificationCode;
	}

	public List<StoreDishPoint> getPonitList() {
		return ponitList;
	}

	public void setPonitList(List<StoreDishPoint> ponitList) {
		this.ponitList = ponitList;
	}

	public List<Address> getmAddressList() {
		return mAddressList;
	}

	public void setmAddressList(List<Address> mAddressList) {
		this.mAddressList = mAddressList;
	}

	public AddressController(Context self, OnResultListener l) {
		super(self);
		mListener = l;
	}

	public void execute(int actionType, BaseRequest request) {
		showDialog(request.getHasProcessDialog());
		final AsyncTaskPayload payload = new AsyncTaskPayload(actionType,
				new Object[] { request });
		switch (payload.getTaskType()) {
		case Actions.ACTION_ADDRESS_LIST:
			doAddressList(payload);
			break;
		case Actions.ACTION_ADDRESS_ADD:
			doAddressAdd(payload);
			break;
		case Actions.ACTION_ADDRESS_UPDATE:
			doAddressUpdate(payload);
			break;
		case Actions.ACTION_VER_CODE:
			doVerCode(payload);
			break;
		case Actions.ACTION_NEW_VERSION:
			doVersion(payload);
			break;
        case Actions.ACTION_MY_RED:
            doMyRedList(payload);
            break;
        case Actions.ACTION_RED_AR_NUM:
            doMyNum(payload);
            break;
        case Actions.ACTION_ARR_VOL:
            doArrviedVolumeList(payload);
            break;
        case Actions.ACTION_ARR_VOL_ISUSE:
            doArrviedVolumeUseList(payload);
            break;
		default:
			break;
		}
	}

    private void doArrviedVolumeUseList(final AsyncTaskPayload payload) {
        ArrviedVolumeRequest request = (ArrviedVolumeRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_ARR_USE_VOL);
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
                                ArrviedVolumeResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof ArrviedVolumeResponse) {
                            arrviedVolumes = ((ArrviedVolumeResponse)response).getData();
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

    private void doArrviedVolumeList(final AsyncTaskPayload payload) {
        ArrviedVolumeRequest request = (ArrviedVolumeRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_ARR_VOL);
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
                                ArrviedVolumeResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof ArrviedVolumeResponse) {
                            arrviedVolumes = ((ArrviedVolumeResponse)response).getData();
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

    private void doMyNum(final AsyncTaskPayload payload) {
        MyNumRequest request = (MyNumRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_RED_AR_NUM);
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
                                MyNumResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof MyNumResponse) {
                            myNum = ((MyNumResponse)response).getData();
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

    private void doMyRedList(final AsyncTaskPayload payload) {
        MyredRequest request = (MyredRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_MY_RED);
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
                                MyredResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof MyredResponse) {
                            myRedLists = ((MyredResponse)response).getData();
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
	
	private void doVersion(final AsyncTaskPayload payload) {
		NewVersionResquest request = (NewVersionResquest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_NEW_VERSION);
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
								NewVersionResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof NewVersionResponse) {
							updateState = ((NewVersionResponse) response).getUpdateState();
							urlStr = ((NewVersionResponse) response).getUpdateUrl();
							message = ((NewVersionResponse) response).getUpdateMessage();
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
	
	private void doVerCode(final AsyncTaskPayload payload) {
		VerificationCodeRequest request = (VerificationCodeRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_VER_CODE);
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
								VerificationCodeResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof VerificationCodeResponse) {
							mVerificationCode = ((VerificationCodeResponse) response).getData();
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
	
	
	private void doAddressUpdate(final AsyncTaskPayload payload) {
		AddressEditRequest request = (AddressEditRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ADDRESS_UPDATE);
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
//						if (response instanceof AddressListResponse) {
//							mAddressList = ((AddressListResponse) response).getData();
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
	
	private void doAddressAdd(final AsyncTaskPayload payload) {
		AddressAddRequest request = (AddressAddRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ADDRESS_ADD);
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
//						if (response instanceof AddressListResponse) {
//							mAddressList = ((AddressListResponse) response).getData();
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
	private void doAddressList(final AsyncTaskPayload payload) {
		AddressListRequest request = (AddressListRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ADDRESS_LIST);
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
								AddressListResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof AddressListResponse) {
							mAddressList = ((AddressListResponse) response).getData();
							ponitList = ((AddressListResponse) response).getStoreDishPointList();
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
	
	/**
	 * 
	 * checkLastNewVersion: 检测上次版本是否是新版本
	 */
	public boolean checkLastNewVersion() {

		// 如果记录的版本提示比当前客户端小，则提示更新内容
		int lastCode = Settings.getVersionCode();
		if (lastCode < BaseActivity.getVersionCode(mContext)) {
			lastCode = BaseActivity.getVersionCode(mContext);
			Settings.setVersionCode(lastCode);
			return true;
		} else {
			return false;
		}
	}
	
}

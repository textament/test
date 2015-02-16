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
import com.shanshengyuan.client.model.dish.DishListRequest;
import com.shanshengyuan.client.model.dish.DishListResponse;
import com.shanshengyuan.client.model.dish.DishType;
import com.shanshengyuan.client.model.dish.DishTypeRequest;
import com.shanshengyuan.client.model.dish.DishTypeResponse;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.dishDetail.DishDetail;
import com.shanshengyuan.client.model.dishDetail.DishDetailRequest;
import com.shanshengyuan.client.model.dishDetail.DishDetailResponse;
import com.shanshengyuan.client.model.fb.FeedbackRequest;
import com.shanshengyuan.client.model.meal.Meal;
import com.shanshengyuan.client.model.meal.MealDetail;
import com.shanshengyuan.client.model.meal.MealDetailRequest;
import com.shanshengyuan.client.model.meal.MealDetailResponse;
import com.shanshengyuan.client.model.meal.MealRequest;
import com.shanshengyuan.client.model.meal.MealResponse;
import com.shanshengyuan.client.model.order.Order;
import com.shanshengyuan.client.model.order.OrderDetail;
import com.shanshengyuan.client.model.order.OrderDetailRequest;
import com.shanshengyuan.client.model.order.OrderDetailResponse;
import com.shanshengyuan.client.model.order.OrderFinish;
import com.shanshengyuan.client.model.order.OrderFinishRequest;
import com.shanshengyuan.client.model.order.OrderFinishResponse;
import com.shanshengyuan.client.model.order.OrderList;
import com.shanshengyuan.client.model.order.OrderListRequest;
import com.shanshengyuan.client.model.order.OrderListResponse;
import com.shanshengyuan.client.model.order.OrderRequest;
import com.shanshengyuan.client.model.order.OrderResponse;
import com.shanshengyuan.client.model.statistics.DishStatisticsRequest;
import com.shanshengyuan.client.utils.JSONHelper;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.NetworkHelper;
import com.shanshengyuan.client.utils.StringUtils;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * @author Administrator
 *
 */
public class DishController extends BaseController {


private static final String TAG = DishController.class.getSimpleName();
	
	private List<Dishes> mDishList = null;
	
	String err;
	
	private List<DishType> mDishTypeList;
	
	private DishDetail mDishDetail;
	
	private Order mOrder;
	
	private OrderFinish mOrderFinish;
	
	private List<OrderList> mOrderList;
	
	private OrderDetail mOrderDetail;

    private List<Meal> mealList;

    private MealDetail mealDetail;

    public MealDetail getMealDetail() {
        return mealDetail;
    }

    public void setMealDetail(MealDetail mealDetail) {
        this.mealDetail = mealDetail;
    }



    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    public OrderDetail getmOrderDetail() {
		return mOrderDetail;
	}

	public void setmOrderDetail(OrderDetail mOrderDetail) {
		this.mOrderDetail = mOrderDetail;
	}

	public List<OrderList> getmOrderList() {
		return mOrderList;
	}

	public void setmOrderList(List<OrderList> mOrderList) {
		this.mOrderList = mOrderList;
	}

	public OrderFinish getmOrderFinish() {
		return mOrderFinish;
	}

	public void setmOrderFinish(OrderFinish mOrderFinish) {
		this.mOrderFinish = mOrderFinish;
	}

	public Order getmOrder() {
		return mOrder;
	}

	public void setmOrder(Order mOrder) {
		this.mOrder = mOrder;
	}

	public DishDetail getmDishDetail() {
		return mDishDetail;
	}

	public void setmDishDetail(DishDetail mDishDetail) {
		this.mDishDetail = mDishDetail;
	}

	public List<DishType> getmDishTypeList() {
		return mDishTypeList;
	}

	public void setmDishTypeList(List<DishType> mDishTypeList) {
		this.mDishTypeList = mDishTypeList;
	}

	public List<Dishes> getmDishList() {
		return mDishList;
	}

	public void setmDishList(List<Dishes> mDishList) {
		this.mDishList = mDishList;
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
		case Actions.ACTION_DISHES_LIST:
			doGetDishList(payload);
			break;
		case Actions.ACTION_DISH_TYPE:
			doGetDishType(payload);
			break;
		case Actions.ACTION_DISH_DETAIL:
			doGetDishDetail(payload);
			break;
		case Actions.ACTION_DISH_ORDER:
			doGetDishOrder(payload);
			break;
		case Actions.ACTION_DISH_ORDER_FINISH:
			doGetDishOrderFinish(payload);
			break;
		case Actions.ACTION_ORDER_LIST:
			doGetOrderList(payload);
			break;
		case Actions.ACTION_ORDER_DETAIL:
			doGetOrderDetail(payload);
			break;
		case Actions.ACTION_FEED_BACK:
			doFeedBack(payload);
			break;
		case Actions.ACTION_DISH_SC:
			doDishSC(payload);
			break;
            case Actions.ACTION_MEAL_LIST:
                doMealList(payload);
                break;
            case Actions.ACTION_MEAL_DETAIL:
                doMealDetailList(payload);
                break;
		default:
			break;
		}
	}

    private void doMealDetailList(final AsyncTaskPayload payload) {
        MealDetailRequest request = (MealDetailRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_MEAL_DETAIL);
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
                                MealDetailResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof MealDetailResponse) {
                            mealDetail = ((MealDetailResponse) response).getData();
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

    private void doMealList(final AsyncTaskPayload payload) {
        MealRequest request = (MealRequest) payload.getData()[0];

        String jsonStr = JSONHelper.objToJson(request);

        HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
        final String url = NetworkHelper
                .executeUrl(ServiceConstants.SERVICE_MEAL_LIST);
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
                                MealResponse.class);
                        if (checkErrorResponse(payload, response, false)) {
                            return;
                        }
                        if (response instanceof MealResponse) {
                            mealList = ((MealResponse) response).getData();
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
	
	
	private void doDishSC(final AsyncTaskPayload payload) {
		DishStatisticsRequest request = (DishStatisticsRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_SC);
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
//						if (response instanceof BaseResponse) {
//							mOrderDetail = ((BaseResponse) response).getData();
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
	
	private void doFeedBack(final AsyncTaskPayload payload) {
		FeedbackRequest request = (FeedbackRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_FEED_BACK);
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
//						if (response instanceof BaseResponse) {
//							mOrderDetail = ((BaseResponse) response).getData();
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
	

	private void doGetOrderList(final AsyncTaskPayload payload) {
		OrderListRequest request = (OrderListRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_ORDER_LIST);
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
								OrderListResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof OrderListResponse) {
							mOrderList = ((OrderListResponse) response).getData();
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
	
	private void doGetDishOrderFinish(final AsyncTaskPayload payload) {
		OrderFinishRequest request = (OrderFinishRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_FINISH);
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
								OrderFinishResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof OrderFinishResponse) {
							mOrderFinish = ((OrderFinishResponse) response).getData();
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
	
	private void doGetDishOrder(final AsyncTaskPayload payload) {
		OrderRequest request = (OrderRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_ORDER);
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
								OrderResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof OrderResponse) {
							mOrder = ((OrderResponse) response).getData();
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
	
	
	private void doGetDishDetail(final AsyncTaskPayload payload) {
		DishDetailRequest request = (DishDetailRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_DETAIL);
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
								DishDetailResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof DishDetailResponse) {
							mDishDetail = ((DishDetailResponse) response).getData();
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
	
	
	private void doGetDishType(final AsyncTaskPayload payload) {
		DishTypeRequest request = (DishTypeRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_TYPE);
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
								DishTypeResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof DishTypeResponse) {
							mDishTypeList = ((DishTypeResponse) response).getData();
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

	private void doGetDishList(final AsyncTaskPayload payload) {
		DishListRequest request = (DishListRequest) payload.getData()[0];

		String jsonStr = JSONHelper.objToJson(request);
		
		HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
		final String url = NetworkHelper
				.executeUrl(ServiceConstants.SERVICE_DISH_LIST);
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
								DishListResponse.class);
						if (checkErrorResponse(payload, response, false)) {
							return;
						}
						if (response instanceof DishListResponse) {
							mDishList = ((DishListResponse) response).getData();
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

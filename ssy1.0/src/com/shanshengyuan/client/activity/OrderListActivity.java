/**
 * 
 */
package com.shanshengyuan.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.OrderListAdapter;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.model.order.OrderList;
import com.shanshengyuan.client.model.order.OrderListRequest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;

import java.util.List;

/**
 * @author  lihao   
 *
 */
public class OrderListActivity extends BaseActivity implements OnResultListener,OnItemClickListener,UpLoadListener,OnClickListener {

	OrderListActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView mOrderList;
	private OrderListAdapter mOrderListAdapter;
	
	//订单列表请求
	private OrderListRequest mOrderListRequest;
	private DishController mDishController;
	
	
	List<OrderList> list = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		initView();
		initTopBar();
		initData();
	//	updateUIData();
	}
	
	private void initData(){
		mOrderListRequest = new OrderListRequest();
		mOrderListRequest.setPage(1);
		mOrderListRequest.setPageSize(10);
		mOrderListRequest.setDk();
		mDishController.execute(Actions.ACTION_ORDER_LIST, mOrderListRequest);
		
	}
	
	private void updateUI() {

		if (mOrderListRequest == null) {
			mOrderListRequest = new OrderListRequest();
			mOrderListRequest.setHasProcessDialog("0");
		} else {
			mOrderListRequest.setHasProcessDialog("0");
		}
		mOrderListRequest.setPage(mOrderListRequest.getPage());
		mOrderListRequest.setPageSize(mOrderListRequest.getPageSize());
		mOrderListRequest.setDk();
		mDishController.execute(Actions.ACTION_ORDER_LIST, mOrderListRequest);

	}
	
	private void updateUIData(){
		list = mDishController.getmOrderList();
		if(list!=null){
			if (list.size() == 0) {
				mOrderList.noDataFinishNoScroll();
			//	mOrderList.setVisibility(View.GONE);
			//	mNoReleaseLayout.setVisibility(View.VISIBLE);
				return;
			} else if (list.size() < 10) {
				mOrderList.setVisibility(View.VISIBLE);
			//	mNoReleaseLayout.setVisibility(View.GONE);
				mOrderList.setHiddenFooterViewForScroll();

				com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
			} else {
				mOrderList.setVisibility(View.VISIBLE);
			//	mNoReleaseLayout.setVisibility(View.GONE);
				mOrderList.loadedFinish();
				mOrderList.isFirst = true;
			}

			if (mOrderList.getAdapter() == null) {
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);

				mOrderListAdapter = new OrderListAdapter(self, list);
				mOrderList.setAdapter(mOrderListAdapter);
			} else {
				mOrderListAdapter = (OrderListAdapter) mOrderList.getAdapter();

				mOrderListAdapter.addListItems(list);

			}	
		}
		
	}
	
	private void initView(){
		mOrderList = (ClimbListView)findViewById(R.id.order_list);
		mOrderList.initFooterView();
		mOrderList.setUpLoadListener(this);
		mOrderList.setOnItemClickListener(this);
	}
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ORDER_LIST:
			mOrderListRequest.next();
			updateUIData();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		super.onFailed(actionType, result);
		
	}

	@Override
	public void scrollTopAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollBottomAction() {
		if (mOrderList.getAdapter() != null) {
			if (mOrderListRequest != null
					&& mOrderList.getAdapter().getCount() > 0) {
				mOrderList.loadingFinish();
				updateUI();

			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		OrderList t = mOrderListAdapter.getItem(position-1);
		Intent intent = new Intent();
		intent.setClass(self, OrderDishListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("orderId", t.getId()+"");
		bundle.putInt("isShowPay", 0);
		bundle.putInt("is_clear", 0);
		bundle.putInt("payType", 0);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
	}
}

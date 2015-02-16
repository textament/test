/**
 * 
 */
package com.sststore.client.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sststore.client.Actions;
import com.sststore.client.BaseActivity;
import com.sststore.client.BaseApplication;
import com.sststore.client.BaseResponse;
import com.sststore.client.adapter.OrderResultAdapter;
import com.sststore.client.controller.DishController;
import com.sststore.client.controller.UserController;
import com.sststore.client.model.dish.DishesOrderFinish;
import com.sststore.client.model.order.OrderDetail;
import com.sststore.client.model.order.OrderDetailRequest;
import com.sststore.client.model.user.UserInfo;
import com.sststore.client.utils.ClimbListView;
import com.sststore.client.utils.ClimbListView.UpLoadListener;
import com.sststore.client.utils.Log;
import com.sststore.client.R;

/**
 * @author Administrator
 *
 */
public class OrderDishListActivity extends BaseActivity implements
		OnResultListener,OnItemClickListener,UpLoadListener,OnClickListener {

	OrderDishListActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView morderpageList;
	private OrderResultAdapter morderpageAdapter;
	
	private View mHeadView;
	private LayoutInflater mInflater;
	
	List<DishesOrderFinish> list;
	
	Bundle b = null;
	
	//金额
	private TextView amountTv;
	//订单状态
	private TextView stateTv;
	//取菜时间
	private TextView timeTv;
	//取菜人
	private TextView nameTv;
	//电话
	private TextView phoneTv;
	//支付状态
	private TextView payTv;
	
	//取菜地址
	private TextView addressTv;
	
	String orderNo;
	
	private TextView infoTv;
	
	private OrderDetailRequest mOrderDetailRequest;
	private DishController mDishController;
	
	private OrderDetail orderDetail;
	
	private Button finishBtn;
	
	UserInfo u = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_dish_list);
		u = UserController.getmUser();
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		b = getIntent().getExtras();
		if(b!=null){
			orderNo = b.getString("ordernum");
		}
		
		initView();
		initTopBar();
		initData();
	//	updateUIData();
		setListener();
	}
	
	
	private void setListener(){
		
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(self, "订单刷新以后就完成",Toast.LENGTH_LONG);
				if(orderDetail!=null){
					AlertDialog.Builder builder = new AlertDialog.Builder(self);
					builder.setMessage("是否确认签收?")
					       .setCancelable(false)
					       .setPositiveButton("是", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	    mOrderDetailRequest = new OrderDetailRequest();
									mOrderDetailRequest.setOrderId(orderDetail.getId());
									mOrderDetailRequest.setStoreId(Integer.parseInt(u.getId()));
									mOrderDetailRequest.setDk();
									mDishController.execute(Actions.ACTION_ORDER_UPDATE, mOrderDetailRequest);
					           }
					       })
					       .setNegativeButton("否", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}else{
					showToast("无效订单，无法签收");
				}
				
				
			}
		});
	}
	
	private void initData(){
		mOrderDetailRequest = new OrderDetailRequest();
		mOrderDetailRequest.setOrderNo(orderNo);
		mOrderDetailRequest.setStoreId(Integer.parseInt(u.getId()));
		mOrderDetailRequest.setDk();
		mDishController.execute(Actions.ACTION_ORDER_DETAIL, mOrderDetailRequest);
		
	}
	
	private void updateUIData(){
		orderDetail = mDishController.getmOrderDetail();
		if(orderDetail!=null){
			infoTv.setText(orderDetail.getExplain());
			amountTv.setText("￥"+orderDetail.getTotalPrice());
			stateTv.setText(orderDetail.getStateName());
			timeTv.setText(orderDetail.getSendTime());
			nameTv.setText(getString(R.string.address_username,orderDetail.getUserName()));
			phoneTv.setText("电话:"+orderDetail.getUserPhone());
			addressTv.setText("收货地址:"+orderDetail.getUserAddress());
			if("0".equals(orderDetail.getAlipayState())){
				payTv.setText("未支付");
			}else if("1".equals(orderDetail.getAlipayState())){
				payTv.setText("已支付");
			}
			list = orderDetail.getDishList();
			
			if("订单签收".equals(orderDetail.getStateName())){
				finishBtn.setVisibility(View.GONE);
			}else{
				finishBtn.setVisibility(View.VISIBLE);
			}
		}
	
		
	
		if(list!=null){
			if (list.size() == 0) {
				morderpageList.noDataFinishNoScroll();
			//	morderpageList.setVisibility(View.GONE);
			//	mNoReleaseLayout.setVisibility(View.VISIBLE);
			//	return;
			} else if (list.size() < 10) {
				morderpageList.setVisibility(View.VISIBLE);
			//	mNoReleaseLayout.setVisibility(View.GONE);
				morderpageList.setHiddenFooterViewForScroll();

				Log.e("listsize", list.size() + "");
			} else {
				morderpageList.setVisibility(View.VISIBLE);
			//	mNoReleaseLayout.setVisibility(View.GONE);
				morderpageList.loadedFinish();
				morderpageList.isFirst = true;
			}
			if (morderpageList.getAdapter() == null) {
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				morderpageAdapter = new OrderResultAdapter(self, list);
				morderpageList.setAdapter(morderpageAdapter);
			} else {
				morderpageAdapter = (OrderResultAdapter) morderpageList.getAdapter();
				morderpageAdapter.addListItems(list);

			}
		}
		
	}
	
	private void initView(){
		mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.order_result_info, null);
		morderpageList = (ClimbListView)findViewById(R.id.order_dish_list);
		morderpageList.addHeaderView(mHeadView);
		morderpageList.initFooterView();
		morderpageList.setUpLoadListener(this);
		morderpageList.setOnItemClickListener(this);
		
		amountTv = (TextView)mHeadView.findViewById(R.id.order_result_amount);
		stateTv = (TextView)mHeadView.findViewById(R.id.order_result_state);
		timeTv = (TextView)mHeadView.findViewById(R.id.order_result_time);
		nameTv = (TextView)mHeadView.findViewById(R.id.order_result_address_name);
		phoneTv = (TextView)mHeadView.findViewById(R.id.order_result_address_phone);
		addressTv = (TextView)mHeadView.findViewById(R.id.address_content);
		infoTv = (TextView)mHeadView.findViewById(R.id.order_result_info);
		payTv = (TextView)mHeadView.findViewById(R.id.order_result_pay);
		finishBtn = (Button)findViewById(R.id.order_result);
	}
	
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				BaseApplication.mInstance.exit();
			}
		});
	}
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ORDER_DETAIL:
			updateUIData();
			break;
		case Actions.ACTION_ORDER_UPDATE:
			showToast("订单签收成功");
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	
	}

	@Override
	public void scrollTopAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollBottomAction() {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			BaseApplication.mInstance.exit();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}

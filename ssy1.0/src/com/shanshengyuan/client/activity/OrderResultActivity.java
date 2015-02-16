package com.shanshengyuan.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.OrderResultAdapter;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.order.OrderFinish;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;

/**
 * 
 * @author lihao
 *
 */
public class OrderResultActivity extends BaseActivity implements
		OnResultListener,UpLoadListener,OnItemClickListener,OnKeyListener{
	
	OrderResultActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView morderpageList;
	private OrderResultAdapter morderpageAdapter;
	
	private View mHeadView;
	private LayoutInflater mInflater;
	
	List<DishesOrderFinish> list;
	
	Bundle b = null;
	
	OrderFinish df;
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
	//说明
	private TextView infoTv;
	
	//取菜地址
	private TextView addressTv;
	//完成
	private Button finishBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		BaseApplication.mInstance.addActivity(this);
		setContentView(R.layout.activity_order_result);
		
		b = getIntent().getExtras();
		df = (OrderFinish) b.getSerializable("orderFinish");
		//购物完成，清空购物车信息
		BuyCar.deleteAll(ClientDBHelper.getInstance(this));
		
		initView();
		initTopBar();
		initData();
		updateUIData();
		setListener();
	}
	
	
	private void setListener(){
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				BaseApplication.mInstance.exit();
				
			}
		});
	}
	
	private void initData(){
		list = new ArrayList<DishesOrderFinish>();
		
		amountTv.setText("￥"+df.getTotalPrice());
		stateTv.setText(df.getStateName());
		infoTv.setText(df.getExplain());
		timeTv.setText(df.getSendTime());
		nameTv.setText(getString(R.string.address_username,df.getUserName()));
		phoneTv.setText("电话:"+df.getUserPhone());
		addressTv.setText("收货地址:"+df.getUserAddress());
		list = df.getDishList();
	}
	
	private void updateUIData(){
		if (list.size() == 0) {
			morderpageList.noDataFinishNoScroll();
			morderpageList.setVisibility(View.GONE);
		//	mNoReleaseLayout.setVisibility(View.VISIBLE);
			return;
		} else if (list.size() < 10) {
			morderpageList.setVisibility(View.VISIBLE);
		//	mNoReleaseLayout.setVisibility(View.GONE);
			morderpageList.setHiddenFooterViewForScroll();

			com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
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
	
	private void initView(){
		mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.order_result_info, null);
		morderpageList = (ClimbListView)findViewById(R.id.order_result_list);
		morderpageList.addHeaderView(mHeadView);
		morderpageList.initFooterView();
		morderpageList.setUpLoadListener(this);
		morderpageList.setOnItemClickListener(this);
		
//		amountTv = (TextView)mHeadView.findViewById(R.id.order_result_amount);
//		stateTv = (TextView)mHeadView.findViewById(R.id.order_result_state);
//		timeTv = (TextView)mHeadView.findViewById(R.id.order_result_time);
//		nameTv = (TextView)mHeadView.findViewById(R.id.order_result_address_name);
//		phoneTv = (TextView)mHeadView.findViewById(R.id.order_result_address_phone);
//		addressTv = (TextView)mHeadView.findViewById(R.id.address_content);
//		finishBtn = (Button)findViewById(R.id.order_result);
//		infoTv = (TextView)mHeadView.findViewById(R.id.order_result_info);
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
		// TODO Auto-generated method stub
		super.onSuccess(actionType);
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		super.onFailed(actionType, result);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
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
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
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

}

package com.sststore.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sststore.client.Actions;
import com.sststore.client.BaseActivity;
import com.sststore.client.BaseResponse;
import com.sststore.client.R;
import com.sststore.client.controller.DishController;
import com.sststore.client.controller.UserController;
import com.sststore.client.model.order.OrderDetail;
import com.sststore.client.model.order.OrderDetailRequest;
import com.sststore.client.model.user.UserInfo;
import com.sststore.client.utils.StringUtils;

public class MainActivity extends BaseActivity implements OnResultListener,OnKeyListener {

	private final static int SCANNIN_GREQUEST_CODE = 1;
	
	MainActivity self = null;
	
	private EditText orderEd;
	
	private Button searchBtn;
	
	private Button saoBtn;
	
	private DishController mDishController;
	private OrderDetailRequest mOrderDetailRequest;
	private OrderDetail mOrderDetail;
	private long mExitTime = 0;
	UserInfo u = null;
	
	String erweimaStr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		
		u = UserController.getmUser();
		
		setContentView(R.layout.activity_main);
		if(mDishController == null){
			mDishController = new DishController(self, self);
		}
		initViews();
		setListener();
	}
	
	private void initViews(){
		orderEd = (EditText)findViewById(R.id.order_et);
		searchBtn = (Button)findViewById(R.id.search);
		saoBtn = (Button)findViewById(R.id.saomiao);
	}
	
	private void setListener(){
		saoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(StringUtils.isEmpty(orderEd.getText().toString())){
					showToast("请输入订单号");
				}else{
					if(orderEd.getText().toString().length()>19){
						showToast("订单号不能大于19位");
					}else{
						String num = orderEd.getText().toString();
						mOrderDetailRequest = new OrderDetailRequest();
						mOrderDetailRequest.setOrderNo(num);
						mOrderDetailRequest.setStoreId(Integer.parseInt(u.getId()));
						mOrderDetailRequest.setDk();
						mDishController.execute(Actions.ACTION_ORDER_DETAIL, mOrderDetailRequest);
					}
				}
			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
			//	mTextView.setText(bundle.getString("result"));
			
				if(bundle.getString("result").contains("http")||bundle.getString("result").contains("www")){
					showToast("无效订单");
				}else{
						mOrderDetailRequest = new OrderDetailRequest();
						mOrderDetailRequest.setOrderNo(bundle.getString("result"));
						mOrderDetailRequest.setStoreId(Integer.parseInt(u.getId()));
						mOrderDetailRequest.setDk();
						mDishController.execute(Actions.ACTION_ORDER_DETAIL, mOrderDetailRequest);
						erweimaStr = bundle.getString("result");
					
					//Intent intent = new Intent();
				//	Bundle b = new Bundle();
					Log.e("aaa", bundle.getString("result"));
//					intent.setClass(self, OrderDishListActivity.class);
//					b.putString("ordernum", bundle.getString("result"));
//					intent.putExtras(b);
//					startActivity(intent);
				}
				
			
				
//				//显示
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		}
    }	

	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ORDER_DETAIL:
			mOrderDetail = mDishController.getmOrderDetail();
			if(mOrderDetail==null){
				showToast("没有该订单号或取菜点提取位置错误");
			}else{
				Intent intent = new Intent();
				intent.setClass(self, OrderDishListActivity.class);
				Bundle bundle = new Bundle();
				if(!StringUtils.isEmpty(orderEd.getText().toString())&&StringUtils.isEmpty(erweimaStr))
					bundle.putString("ordernum", orderEd.getText().toString());
				else if(StringUtils.isEmpty(orderEd.getText().toString())&&!StringUtils.isEmpty(erweimaStr))
					bundle.putString("ordernum", erweimaStr);
				else if(!StringUtils.isEmpty(orderEd.getText().toString())&&!StringUtils.isEmpty(erweimaStr)){
					bundle.putString("ordernum", erweimaStr);
				}
				intent.putExtras(bundle);
				startActivity(intent);
				
				erweimaStr = "";
			}
			
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		super.onFailed(actionType, result);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			exit();
			return false;
		}
	
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}


}

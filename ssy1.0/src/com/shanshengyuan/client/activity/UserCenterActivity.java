package com.shanshengyuan.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.model.redpackage.MyNum;
import com.shanshengyuan.client.model.redpackage.MyNumRequest;

import org.w3c.dom.Text;

public class UserCenterActivity extends BaseActivity implements OnResultListener{

	UserCenterActivity self = null;
	
	private ImageView mBack;
	
	private LinearLayout addressLy;
	
	private LinearLayout feedLy;
	
	private LinearLayout orderLy;
	
	private LinearLayout aboutLy;

    private LinearLayout myredLy;
    private LinearLayout volLy;

    private TextView redNumTv;
    private TextView volNumTv;

    private MyNumRequest myNumRequest;
    private AddressController addressController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
        if(addressController==null){
            addressController = new AddressController(self,self);
        }
		initTopBar();
		initView();
		setListener();

	}

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData(){
        myNumRequest = new MyNumRequest();
        myNumRequest.setHasProcessDialog("0");
        myNumRequest.setDk();
        addressController.execute(Actions.ACTION_RED_AR_NUM,myNumRequest);
    }
	
	private void setListener(){
        volLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(self, ArrviedVolumeActivity.class);
                startActivity(intent);
            }
        });

        myredLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(self, MyRedActivity.class);
                startActivity(intent);
            }
        });

		feedLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, FeedBackActivity.class);
				startActivity(intent);
			}
		});
		
		addressLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, AddressActivity.class);
				startActivity(intent);
			}
		});
		
		orderLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, OrderListActivity.class);
				startActivity(intent);
			}
		});
		
		aboutLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, AboutActivity.class);
				startActivity(intent);
			}
		});
		
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
	
	private void initView(){
        volLy = (LinearLayout)findViewById(R.id.my_volume);
        myredLy = (LinearLayout)findViewById(R.id.my_red);
        volNumTv = (TextView)findViewById(R.id.volume_num);
        redNumTv = (TextView)findViewById(R.id.red_num);
		addressLy = (LinearLayout)findViewById(R.id.my_address);
		feedLy = (LinearLayout)findViewById(R.id.feedback);
		orderLy = (LinearLayout)findViewById(R.id.my_order);
		aboutLy = (LinearLayout)findViewById(R.id.about_us);
	}

    @Override
    public void onSuccess(int actionType) {
            switch (actionType){
                case Actions.ACTION_RED_AR_NUM:
                    MyNum myNum = addressController.getMyNum();
                    redNumTv.setText(self.getString(R.string.how_sheet, myNum.getRedTotalNum()));
                    volNumTv.setText(self.getString(R.string.how_sheet,myNum.getArTotalNum()));
                    break;
            }
    }

    @Override
    public void onFailed(int actionType, BaseResponse result) {
        super.onFailed(actionType, result);
    }
}

/**
 * 
 */
package com.shanshengyuan.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.model.fb.FeedbackRequest;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author LIHAO
 *
 */
public class FeedBackActivity extends BaseActivity implements OnResultListener {
	
	FeedBackActivity self = null;
	
	View mBack;
	
	//意见反馈
	private EditText mContentEd;
	private EditText mUserEd;
	private EditText mPhoneEd;
	//提交
	private Button mSureBtn;
	
	//请求
	private FeedbackRequest mFeedbackRequest;
	private DishController mDishController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		initTopBar();
		initView();
		setListener();
	}
	
	private void setListener(){
		mSureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if(validate()){
						mFeedbackRequest = new FeedbackRequest();
						mFeedbackRequest.setContent( mContentEd.getText().toString());
						if(!StringUtils.isEmpty(mPhoneEd.getText().toString())){
							mFeedbackRequest.setContent(mPhoneEd.getText().toString());
						}
						if(!StringUtils.isEmpty(mUserEd.getText().toString())){
							mFeedbackRequest.setContent(mUserEd.getText().toString());
						}
						mFeedbackRequest.setDk();
						mDishController.execute(Actions.ACTION_FEED_BACK, mFeedbackRequest);
					}
			}
		});
	}
	
	private boolean validate(){
		boolean result = true;
		//验证内容
		String user = mContentEd.getText().toString();
		if(StringUtils.isEmpty(user)){
			Toast.makeText(self, "请说说您的意见！", Toast.LENGTH_LONG).show();
			result = false;
			return result;
		}
		
		//电话
		String sUserPhone = mPhoneEd.getText().toString();
		if(!StringUtils.isEmpty(sUserPhone)){
			if (!StringUtils.isPhoneNumber(sUserPhone)) {
				Toast.makeText(self, "请输入正确的手机号码！",
						Toast.LENGTH_LONG).show();
				result = false;
				return result;
			}
			
		}
		return result;
	}
	
	private void initView(){
		mContentEd = (EditText)findViewById(R.id.fb_content_et);
		mUserEd = (EditText)findViewById(R.id.feedback_username_et);
		mPhoneEd = (EditText)findViewById(R.id.feedback_phone_et);
		mSureBtn = (Button)findViewById(R.id.feedback_sure);
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
		case Actions.ACTION_FEED_BACK:
			Toast.makeText(self, "谢谢你提供的宝贵意见！", Toast.LENGTH_SHORT).show();
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		super.onFailed(actionType, result);
	}
}

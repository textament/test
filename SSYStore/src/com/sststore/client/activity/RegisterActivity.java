/**
 * 
 */
package com.sststore.client.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sststore.client.BaseActivity;
import com.sststore.client.BaseResponse;
import com.sststore.client.R;

/**
 * @author lihao
 *
 */
public class RegisterActivity extends BaseActivity implements OnResultListener {
	
	RegisterActivity self = null;
	View mBack;
	
	private EditText userEd;
	private EditText pwdEd;
	private Button registerBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initTopBar();
		initView();
	}
	
	private void initView(){
		userEd = (EditText)findViewById(R.id.username_et_register);
		pwdEd = (EditText)findViewById(R.id.pwd_et_register);
		registerBtn = (Button)findViewById(R.id.regiter);
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
		// TODO Auto-generated method stub
		super.onSuccess(actionType);
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		super.onFailed(actionType, result);
	}
}

/**
 * 
 */
package com.sststore.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sststore.client.Actions;
import com.sststore.client.BaseActivity;
import com.sststore.client.BaseResponse;
import com.sststore.client.R;
import com.sststore.client.controller.UserController;
import com.sststore.client.model.user.UserInfo;
import com.sststore.client.model.user.UserRequest;
import com.sststore.client.utils.Settings;
import com.sststore.client.utils.StringUtils;

/**
 * @author lihao
 *
 */
public class LoginActivity extends BaseActivity implements OnResultListener {
	
	LoginActivity self = null;

	private EditText userEd;
	private EditText pwdEd;
	private Button logBtn;
	private Button regBtn;
	
	private UserController userController;
	private UserRequest mUserRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(userController==null){
			userController = new UserController(self, self);
		}
	
		
		initView();
		setListener();
	}
	
	private void initView(){
		userEd = (EditText)findViewById(R.id.username_et);
		pwdEd = (EditText)findViewById(R.id.pwd_et);
		logBtn = (Button)findViewById(R.id.login);
		regBtn = (Button)findViewById(R.id.login_regiter);
	}
	
	private void setListener(){
		logBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(validate()){
//					mUserRequest = new UserRequest();
//					mUserRequest.setUsername(userEd.getText().toString());
//					mUserRequest.setPassword(pwdEd.getText().toString());
//					mUserRequest.setDk();
//					userController.execute(Actions.ACTION_LOGIN, mUserRequest);
					UserInfo mUser = null;
					if(mUser==null){
						mUser = new UserInfo();
						mUser.setId("1");
						mUser.setName("admins");
					}else{
						mUser.setId("1");
						mUser.setName("admins");
					}
					Settings.saveUserInfo(mUser);
					
					Intent intent = new Intent();
					intent.setClass(self, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	
	private boolean validate(){
		boolean result = true;
		String name = userEd.getText().toString();
		String pwd = pwdEd.getText().toString();
		if(StringUtils.isEmpty(name)){
			showToast("请输入登陆名");
			result = false;
			return result;
		}else{
			if(!StringUtils.isCheckNum(name)){
				showToast("登陆名不能包含特殊字符或中文");
				result = false;
				return result;
			}else{
				if(name.length()<6||name.length()>16){
					showToast("登陆名只能6~16位");
					result = false;
					return result;
				}
			}
		}
		
		if(StringUtils.isEmpty(pwd)){
			showToast("密码不能为空");
			result = false;
			return result;
		}else{
			if(!StringUtils.isPwdCheck(pwd)){
				showToast("密码6~16位并且不能包含中文和特殊字符");
				result = false;
				return result;
			}
		}
		
		return result;
	}
	
	
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_LOGIN:
			Intent intent = new Intent();
			intent.setClass(self, MainActivity.class);
			startActivity(intent);
			finish();
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
}

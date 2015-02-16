/**
 * 
 */
package com.shanshengyuan.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;

/**
 * @author lihao
 *
 */
public class AboutActivity extends BaseActivity implements OnResultListener {
	
	AboutActivity self = null;
	
	View mBack;
	
	private LinearLayout updateLayout;

	private TextView versionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_about);
		initTopBar();
		updateLayout = (LinearLayout) findViewById(R.id.new_setting_about_update);
		versionView = (TextView) this.findViewById(R.id.new_setting_about_text);
		versionView.setText("版本更新(当前" + BaseApplication.VERSION+":"+BaseApplication.CHANNEL_ID + ")");
		
		// 客服电话
		this.findViewById(R.id.customer_service_phone1).setOnClickListener(
				callPhoneListener);
		
		updateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Toast.makeText(self, "已经是最新版本", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void initTopBar() {
		mBack = findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
	
	private OnClickListener callPhoneListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Uri uri = Uri.parse("tel:400-999-9227");
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);

		}
	};


	
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

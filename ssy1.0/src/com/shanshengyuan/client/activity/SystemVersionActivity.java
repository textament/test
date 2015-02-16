package com.shanshengyuan.client.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import zwan.phone.api.activity.ZwanphoneService;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.com.zwan.phone.ZwanphoneManager;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.utils.Settings;

public class SystemVersionActivity extends BaseActivity implements OnClickListener {

	public static final int STATUS_NO_VERSION = 2;	// 不需要更新
	public static final int STATUS_NEW_VERSION = 0;	// 建议更新
	public static final int STATUS_NEW_VERSION_MUST = 1;	// 强制更新
	public static final int STATUS_CLOSE = 3;	// 关闭
	public static final int STATUS_NEW_MESSAGE = 4;	// 提示

	int status = 0;
	String version = null;
	String message = null;
	String urlStr = null;
	String downPath = null;

	ProgressBar proBar = null;
	TextView proText = null;

	Boolean isShowCheckBox = false;

	// 是否开始下载
	private Boolean isStart = false;
	// 提示
	private TextView textView;
	// 内容
	private TextView text;
	// 确认按钮
	private Button sureBtn;
	// 取消按钮
	private Button cancelBtn;

	FileOutputStream fos = null; // 写入流

	CheckBox check;

	LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		isShowCheckBox = bundle.getBoolean("is_show", false);
		String sStatus = bundle.getString("status");
		if (sStatus != null) {
			status = Integer.parseInt(sStatus);
		}
		version = bundle.getString("version");
		message = bundle.getString("message");
		urlStr = bundle.getString("url");

		this.setContentView(R.layout.system_dialog_layout);
		this.stopService(new Intent(Intent.ACTION_MAIN).setClass(SystemVersionActivity.this, ZwanphoneService.class));//停止服务
		textView = (TextView) this.findViewById(R.id.update_title);
		layout = (LinearLayout) this.findViewById(R.id.update_layout);
		check = (CheckBox) findViewById(R.id.system_is_remind);
		if (isShowCheckBox) {
			check.setVisibility(View.VISIBLE);
		} else {
			check.setVisibility(View.GONE);
		}

		check.setOnCheckedChangeListener(checkListener);

		if (STATUS_NEW_MESSAGE == status) {

			TextView mTitleView = (TextView) findViewById(R.id.update_title);
			mTitleView.setText("新功能介绍");
			
			text = (TextView) findViewById(R.id.system_dialog_text);
			FrameLayout.LayoutParams insidelp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					BaseApplication.HEIGHT/2);
			
			text.setLayoutParams(insidelp);
			
			text.setPadding(18, 8, 8, 8);
			text.setGravity(Gravity.CENTER|Gravity.LEFT);
			text.setText(message);

			sureBtn = (Button) findViewById(R.id.system_dialog_btn1);
			sureBtn.setText("我知道了");
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setOnClickListener(cancelClick);
			cancelBtn = (Button) findViewById(R.id.system_dialog_btn2);
			cancelBtn.setVisibility(View.GONE);
			return;
		}
		
		if (STATUS_CLOSE == status) {

			TextView mTitleView = (TextView) findViewById(R.id.update_title);
			mTitleView.setText("旧版已无法使用，请更新新版本");
			
			text = (TextView) findViewById(R.id.system_dialog_text);
			text = (TextView) findViewById(R.id.system_dialog_text);
			FrameLayout.LayoutParams insidelp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					BaseApplication.HEIGHT/2);
			
			text.setLayoutParams(insidelp);
			
			text.setPadding(18, 8, 8, 8);
			text.setGravity(Gravity.CENTER|Gravity.LEFT);
			text.setText(message);

			sureBtn = (Button) findViewById(R.id.system_dialog_btn1);
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setOnClickListener(closeClick);
			cancelBtn = (Button) findViewById(R.id.system_dialog_btn2);
			cancelBtn.setVisibility(View.GONE);
			return;
		}

		if (STATUS_NEW_VERSION_MUST == status) {

			TextView mTitleView = (TextView) findViewById(R.id.update_title);
			mTitleView.setText("检测到新版本");
			
			text = (TextView) findViewById(R.id.system_dialog_text);
			text = (TextView) findViewById(R.id.system_dialog_text);
			FrameLayout.LayoutParams insidelp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					BaseApplication.HEIGHT/2);
			
			text.setLayoutParams(insidelp);
			
			text.setPadding(18, 8, 8, 8);
			text.setGravity(Gravity.CENTER|Gravity.LEFT);
			text.setText(message);

			sureBtn = (Button) findViewById(R.id.system_dialog_btn1);
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setOnClickListener(updateClick);
			cancelBtn = (Button) findViewById(R.id.system_dialog_btn2);
			cancelBtn.setVisibility(View.GONE);
			cancelBtn.setOnClickListener(closeClick);
			return;
		}

		if (STATUS_NEW_VERSION == status) {

			TextView mTitleView = (TextView) findViewById(R.id.update_title);
			mTitleView.setText("检测到新版本");
			
			text = (TextView) findViewById(R.id.system_dialog_text);
			text = (TextView) findViewById(R.id.system_dialog_text);
			FrameLayout.LayoutParams insidelp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					BaseApplication.HEIGHT/2);
			
			text.setLayoutParams(insidelp);
			text.setGravity(Gravity.CENTER|Gravity.LEFT);
			text.setPadding(18, 8, 8, 8);
			text.setText(message);

			sureBtn = (Button) findViewById(R.id.system_dialog_btn1);
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setOnClickListener(updateClick);
			cancelBtn = (Button) findViewById(R.id.system_dialog_btn2);
			cancelBtn.setVisibility(View.VISIBLE);
			cancelBtn.setOnClickListener(cancelClick);
			return;
		}
	}

	private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Settings.setIsRemindUpdate(isChecked);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	class DownAsyncTask extends AsyncTask<Object, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Object... params) {

			InputStream buffer = null; // 读取流

			downPath = BaseApplication.SD_DIR + "/update.apk";
			// 判断文件是否存在
			File file = new File(downPath);
			try {
				if (!file.exists()) {
                    file.mkdirs();
                    file.createNewFile();
				}else{
					file.delete();
                    file.mkdirs();
                    file.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				URL url = new URL(urlStr);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				buffer = urlConn.getInputStream();

				int fileLength = urlConn.getContentLength(); // 总大小
				int readLength = 0; // 读取大小
				int downLength = 0; // 下载大小

				// 验证下下载文件大小是否和本地文件大小相等。如果相等，则不下载直接尝试打开
				if (file.length() == fileLength) {

					return true;

				} else { // 否则尝试删除文件

					if (file.delete()) {
						file.createNewFile();
					}

				}

				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}

				byte[] bytes = new byte[1024 * 16];
				while ((readLength = buffer.read(bytes)) != -1) {
					fos.write(bytes, 0, readLength);
					downLength += readLength;
					publishProgress(downLength, fileLength);
				}
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (buffer != null) {
						buffer.close();
					}
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(downPath)), "application/vnd.android.package-archive");
				startActivity(intent);

				// Intent closeIntent = new Intent(SystemVersionActivity.this,
				// CloseActivity.class);
				// startActivity(closeIntent);

				SystemVersionActivity.this.finish();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			BigDecimal downInt = new BigDecimal(values[0] / 1024f / 1024f);
			BigDecimal allInt = new BigDecimal(values[1] / 1024f / 1024f);

			proText.setText(downInt.setScale(1, BigDecimal.ROUND_HALF_UP) + "M/"
					+ allInt.setScale(1, BigDecimal.ROUND_HALF_UP) + "M");
			proBar.setProgress((values[0] * 100) / values[1]);
			super.onProgressUpdate(values);
		}
	};

	// 强制关闭
	private OnClickListener closeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	// 取消
	private OnClickListener cancelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isStart) {
				if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				finish();
			} else {
				finish();
			}

		}

	};

	// 升级
	private OnClickListener updateClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isStart = true;
			sureBtn.setVisibility(View.GONE);
			text.setVisibility(View.GONE);
			layout.setVisibility(View.GONE);
			textView.setText("正在更新，请稍后。。。");
			proBar = (ProgressBar) findViewById(R.id.system_dialog_down_progress);
			proText = (TextView) findViewById(R.id.system_dialog_down_text);
			proBar.setVisibility(View.VISIBLE);
			proText.setVisibility(View.VISIBLE);
		
			new DownAsyncTask().execute(0);
			v.setEnabled(false);
		}
	};

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

}

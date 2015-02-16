package com.shanshengyuan.client.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import zwan.phone.api.activity.ZwanphoneService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import cn.com.zwan.core.SystemContext;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.utils.NetworkHelper;
import com.shanshengyuan.client.ziguang.AppLauncherHandler;

/**
 * @author lihao
 */
public class WelcomeActivity extends BaseActivity implements OnResultListener{

//	private Handler mHandler = new Handler();
	private SurfaceView view = null;
	private boolean running = false;
	
	  LocationClient mLocalClient;
	  
	  private AppLauncherHandler mHandler;
		private ServiceWaitThread mThread;

	  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_welcome);
		if (!NetworkHelper.checkNetwork(WelcomeActivity.this)) {
			Toast.makeText(this, "您的网络不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		mHandler= new AppLauncherHandler(this);
	SystemContext.context=getApplicationContext();
		
		DisplayMetrics  dm = new DisplayMetrics();  
		//取得窗口属性  
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		//窗口高度
		SystemContext.screenHeight=dm.heightPixels;
		SystemContext.screenWidth=dm.widthPixels; 


		
		//紫光呼叫
		if (ZwanphoneService.isReady()) {
			onServiceReady();
		} else {
			startService(new Intent(Intent.ACTION_MAIN).setClass(this, ZwanphoneService.class));
			mThread = new ServiceWaitThread();
			mThread.start();
		}
		//initMap();
	}
	

	private Runnable enterHome = new Runnable() {
		public void run() {
			goDesktopScreen();
		}
	};

	private void goDesktopScreen() {
//		if (Settings.getIsFirstStart()) {
//			Intent it = new Intent(mContext, WelcomeStartPageActivity.class);
//			mContext.startActivity(it);
//		} else {
//			Intent it = new Intent(mContext, MainNewActivity.class);
//			mContext.startActivity(it);
//		}

		finish();
	}
	
	protected void onServiceReady() {
		final Class<? extends Activity> classToStart = MainNewActivity.class;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent().setClass(WelcomeActivity.this, classToStart).setData(getIntent().getData()));
				finish();
			}
		}, 3000);
	}


	private class ServiceWaitThread extends Thread {
		public void run() {

			while (!ZwanphoneService.isReady()) {
				try {
					sleep(30);
				} catch (InterruptedException e) {
					throw new RuntimeException("waiting thread sleep() has been interrupted");
				}
			}
			
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					onServiceReady();
				}
			});
			mThread = null;
		}
	}
	
	

}

package com.sststore.client.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sststore.client.BaseActivity;
import com.sststore.client.BaseApplication;
import com.sststore.client.BaseResponse;
import com.sststore.client.R;
import com.sststore.client.controller.UserController;
import com.sststore.client.model.user.UserInfo;
import com.sststore.client.utils.NetworkHelper;
import com.sststore.client.utils.Settings;

/**
 * @author lihao
 */
public class WelcomeActivity extends BaseActivity implements OnResultListener{

	private Context mContext = this;
	private Handler mHandler = new Handler();
	private SurfaceView view = null;
	private boolean running = false;
	
	  LocationClient mLocalClient;
	  
   private UserController mUserController;
	  
	public MyLocationListenner myListener = new MyLocationListenner();

	UserInfo u = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_welcome);
		if (!NetworkHelper.checkNetwork(WelcomeActivity.this)) {
			Toast.makeText(this, "您的网络不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mUserController == null) {
			mUserController = new UserController(this, this);
		}
		u = Settings.loadUserInfo();
		if (u != null) {
			UserController.setmUser(u);
		}
	//	getCpuInfo();
		mHandler.postDelayed(enterHome, 3000);
		//initMap();
	}
	
		private void initMap(){
			// 定位初始化
			mLocalClient = new LocationClient(this);
			mLocalClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);
			option.setAddrType("all");
			option.setCoorType("bd09ll"); 
			option.setScanSpan(5000);
			mLocalClient.setLocOption(option);
			mLocalClient.start();

		}
		
		public class MyLocationListenner implements BDLocationListener {

	        @Override
	        public void onReceiveLocation(BDLocation location) {
	            if (location == null ){
	            	Toast.makeText(WelcomeActivity.this, "获取经纬度失败", Toast.LENGTH_SHORT).show();
	                return;
	            }
	            BaseApplication.latitude = location.getLatitude()+"";
	            BaseApplication.longitude = location.getLongitude()+"";
	            
	        	Log.e("BaseApplication.latitude", BaseApplication.latitude);
				Log.e("BaseApplication.longitude", BaseApplication.longitude);
	           
	        }

	        public void onReceivePoi(BDLocation poiLocation) {
	            if (poiLocation == null) {
	                return;
	            }
	        }
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
		if(u==null){
			Intent it = new Intent(mContext, LoginActivity.class);
			mContext.startActivity(it);
		}else{
			Intent it = new Intent(mContext, MainActivity.class);
			mContext.startActivity(it);
		}
		
		
//		}

		finish();
	}
	
	/**
	  * 获取手机CPU信息
	  *
	  * @return
	  */
	 public String[] getCpuInfo()
	 {
	  String str1 = "/proc/cpuinfo";
	  String str2 = "";
	  String[] cpuInfo = { "", "" };
	  String[] arrayOfString;
	  try
	  {
	   FileReader fr = new FileReader(str1);
	   BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
	   str2 = localBufferedReader.readLine();
	   arrayOfString = str2.split("\\s+");
	   for (int i = 2; i < arrayOfString.length; i++)
	   {
	    cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
	   }
	   str2 = localBufferedReader.readLine();
	   arrayOfString = str2.split("\\s+");
	   cpuInfo[1] += arrayOfString[2];
	   localBufferedReader.close();
	  }
	  catch (IOException e)
	  {
	  }
	  Log.e("111", "CPU型号 " + cpuInfo[0] + "\n" + "CPU频率: " + cpuInfo[1] + "\n");
	  return cpuInfo;
	 }
	 
	 @Override
	protected void onDestroy() {
		   destroyMap();
		super.onDestroy();
	}
	 
	    private void destroyMap() {
	        if (mLocalClient != null) {
	        	mLocalClient.stop();
	        }
	    }

		@Override
		public void onSuccess(int actionType) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFailed(int actionType, BaseResponse result) {
			// TODO Auto-generated method stub
			
		}

	   

//	@Override
//	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
//
//	}
//
//	public final void surfaceCreated(final SurfaceHolder holder) {
//		this.running = true;
//
//		final Handler handler = new Handler() {
//			public void dispatchMessage(Message msg) {
//				while (running) {
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//					}
//				}
//			}
//		};
//
//		new Thread(new Runnable() {
//			public void run() {
//				handler.sendEmptyMessage(0);
//				Bitmap bmp = BitmapFactory.decodeResource(
//						WelcomeActivity.this.getResources(), R.drawable.welcome);
//				Paint paint = new Paint();
//				paint.setAntiAlias(true);
//				int alpha = 0x00;
//				while (running && alpha <= 0xFF) {
//					Canvas canvas = holder.lockCanvas();
//					paint.setStyle(Style.FILL);
//					canvas.drawRect(0, 0, canvas.getWidth(),
//							canvas.getHeight(), paint);
//					paint.setAlpha(0xFF);
//					Rect rect = getRect(canvas, bmp);
//					canvas.drawBitmap(bmp, null, rect, paint);
//					holder.unlockCanvasAndPost(canvas);
//					try {
//						Thread.sleep(5);
//					} catch (InterruptedException e) {
//					}
//					alpha += 5;
//				}
//				running = false;
//			}
//
//		}).start();
//	}
//
//	private Rect getRect(Canvas canvas, Bitmap bmp) {
//		Rect rect = new Rect();
//		int screenWidth = canvas.getWidth();
//		int screenHeight = canvas.getHeight();
//		int imgWidth = bmp.getWidth();
//		int imgHeight = bmp.getHeight();
//		float scale = screenWidth * 1.0f / imgWidth;
//		if (imgHeight * scale < screenHeight) {
//			scale = screenHeight * 1.0f / imgHeight;
//		}
//		rect = new Rect(Math.round(screenWidth / 2 - imgWidth * scale / 2),
//				Math.round(screenHeight - imgHeight * scale),
//				Math.round(screenWidth / 2 + imgWidth * scale / 2),
//				Math.round(screenHeight));
//		return rect;
//	}
//
//	public final void surfaceDestroyed(final SurfaceHolder holder) {
//	}

}

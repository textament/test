package com.sststore.client;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioRecord;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.sststore.client.cache.SSYImageCache;
import com.sststore.client.utils.Log;
import com.sststore.client.utils.Settings;
import com.ta.exception.TANoSuchCommandException;
import com.ta.mvc.command.TACommandExecutor;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.mvc.common.TAResponse;
import com.ta.util.TALogger;

public class BaseApplication extends Application implements
TAIResponseListener  {

	public static BaseApplication mInstance = null;
	public final static String RECEIVER_LOGING = "android.intent.action.loginreceiver";
	public final static String RECEIVER_NO_LOGING = "android.intent.action.nologinreceiver";
	public final static String RECEIVER_SETTING_BIND_PHONE = "android.intent.action.bindphonereceiver";

	// 客户端定义的缓存资源存储目录aa
	public static final String SD_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/ssystore";

	private List<Activity> activityList = new LinkedList<Activity>();
	public final static String RECEIVER_NOTICE = "android.intent.action.noticereceiver";

	public static final String VERSION = "1.0.0";
	public static final String API_VERSION = "1.0.0";

	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	public static int DENSITY = 0;

	public static String sIMEI = "";
	public static String sDeviceKey;

	// 经度
	public static String longitude;
	// 纬度
	public static String latitude;

	public static boolean mTaskFinalWrokFinal = false;
	public static boolean isFrist1 = true;
	public static boolean isFrist2 = true;
	public static boolean isFrist3 = true;
	public static boolean isFrist4 = true;
	public static boolean isFrist5 = true; // 底部tab是否第一次已经加�?
	public static boolean isNew = false; // 是否有新的消�?
	public static boolean isAdver = true;
	public static boolean isAdver1 = true;
	public static boolean isFaceFirst = true;
	public static int mComeFrom = 0;
	public static String name = "";
	public static String pwd = "";
	public static String oauid = "";
	public static boolean isPushGaoJian = false;// 是否推�?稿件
	public static boolean isReflash = false; // 是否刷新我的订单
	public static boolean isReflashAmount = false; // 是否刷新用户中心余额
	public static int payJmp = 0; // 支付成功以后跳转�?
	public static boolean isAddressFlash = false;//是否刷新地址
	public static String qianjinUrl = "";
	public static String TaskId = "";
	public static String VCode = "";

	public boolean m_bKeyRight = true;

	public static String CHANNEL_ID = null; // talkingdata对应的channel_id

	public static int CHANNEL_ID_INT = 0; // 频道对应的数�?

	public static String systime = "";

	public static Context context = null;

	// BMapManager mBMapManager = null;

	/** App异常崩溃处理�?*/

	private static BaseApplication application;
	private Boolean networkAvailable = false;

	public static boolean isFragmentServer = true;
	public static boolean isFragmentHappy = true;
	public static boolean isTaskMarketFragment = true;
	public static boolean isUserCenter = true;
	public static final int DEFAULT_CACHE_SIZE = (24 /* MiB */* 1024 * 1024);

	public void onCreate() {
		// TODO Auto-generated method stub
		onPreCreateApplication();
		super.onCreate();
		doOncreate();
		onAfterCreateApplication();
		// getAppManager();
		mInstance = this;
		initWidthAndHeight();
		ApplicationInfo appInfo = null;
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		Settings.init(this);
//		UserInfo u = Settings.loadUserInfo();
//		if (u != null) {
//			UserController.setUser(u);
//		}

	//	initRecorder();
		// 确定是不�?1，如果是，则把Channel_id_int 弄成1，用来给91传日�?
		CHANNEL_ID = appInfo.metaData.getString("TD_CHANNEL_ID");

		if ("android.91.com".equals(CHANNEL_ID)) {
			CHANNEL_ID_INT = 1;
		}

		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (!telephonyManager.getDeviceId().equals("Unknow")) {
			sIMEI = telephonyManager.getDeviceId();
		}

		sDeviceKey = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Log.e("sDeviceKey", sDeviceKey);
	
		SSYImageCache.getInstance().init(this);
//		// 从缓存取百度userid
//		String userId = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
//				.getString(PushMessageReceiver.BAIDU_PUSH_USER_ID, null);
//		if (!StringUtils.isEmpty(userId)) {
//			Log.e("userId", userId);
//		}

//		Config.BAIDU_USER = userId;
	}

	public static final int SAMPLE_RATE = 44100;
	private short[] mBuffer;
	public AudioRecord mRecorder = null;

//	private void initRecorder() {
//		int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
//				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//		mBuffer = new short[bufferSize];
//		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
//				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
//				bufferSize);
//		mRecorder.stop();
//		mRecorder.startRecording();
//		mRecorder.stop();
//		mRecorder.release();
//	}

	// public static int unsignedByteToInt(byte b) {
	// return (int) b & 0xFF;
	// }
	@Override
	public void onTerminate() {
		super.onTerminate();
		// PushService.actionStop(getApplicationContext());

	}

	public void initEngineManager(Context context) {
		// if (mBMapManager == null) {
		// mBMapManager = new BMapManager(context);
		// }
		//
		// if (!mBMapManager.init(this.getString(R.string.baidu_map_key), new
		// MapGeneralListener())) {
		// Toast.makeText(BaseApplication.mInstance.getApplicationContext(),
		// "BMapManager  初始化错�?", Toast.LENGTH_LONG).show();
		// }
	}

	// // 常用事件监听，用来处理�?常的网络错误，授权验证错误等
	// static class MapGeneralListener implements MKGeneralListener {
	//
	// @Override
	// public void onGetNetworkState(int iError) {
	// if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	// Toast.makeText(BaseApplication.mInstance.getApplicationContext(),
	// "Map:您的网络出错啦！",
	// Toast.LENGTH_LONG).show();
	// } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	// Toast.makeText(BaseApplication.mInstance.getApplicationContext(),
	// "Map:输入正确的检索条件！",
	// Toast.LENGTH_LONG).show();
	// }
	// // ...
	// }
	//
	// @Override
	// public void onGetPermissionState(int iError) {
	// if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
	// // 授权Key错误�?
	// Toast.makeText(BaseApplication.mInstance.getApplicationContext(),
	// "Map:请输入正确的授权Key", Toast.LENGTH_LONG).show();
	// BaseApplication.mInstance.m_bKeyRight = false;
	// }
	// }
	// }

	private void initWidthAndHeight() {
		if (this.getResources() != null) {
			if (this.getResources().getDisplayMetrics() != null) {
				WIDTH = this.getResources().getDisplayMetrics().widthPixels;
				HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
				DENSITY = this.getResources().getDisplayMetrics().densityDpi;
			}
		}
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 获取当前栈中activity长度
	public int getActivitySize() {
		return activityList.size();
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
//		activityList.clear();
//		SSYImageCache.getInstance().clearAll();
//		android.os.Process.killProcess(android.os.Process.myPid());
//		System.exit(0);
	}

	/***/
	private void doOncreate() {
		// TODO Auto-generated method stub
		this.application = this;
		// 注册App异常崩溃处理�?
//		ZBJAppException crashHandler = ZBJAppException.getInstance();
//		crashHandler.init(getApplicationContext());

	}

	/**
	 * 获取Application
	 * 
	 * @return
	 */
	public static BaseApplication getApplication() {
		return application;
	}

	protected void onAfterCreateApplication() {
		// TODO Auto-generated method stub

	}

	protected void onPreCreateApplication() {
		// TODO Auto-generated method stub

	}

	
	/**
	 * 获取当前网络状�?，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public Boolean isNetworkAvailable() {
		return networkAvailable;
	}

	public void onFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(TAResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRuning(TAResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(TAResponse arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean record, boolean resetStack) {
		if (listener != null) {
			try {
				TACommandExecutor.getInstance().enqueueCommand(commandKey,
						request, listener);

			} catch (TANoSuchCommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			TALogger.i(BaseApplication.this, "go with cmdid=" + commandKey
					+ ", record: " + record + ",rs: " + resetStack
					+ ", request: " + request);
			if (resetStack) {
			}

			Object[] newTag = { request.getTag(), listener };
			request.setTag(newTag);

			TALogger.i(BaseApplication.this, "Enqueue-ing command");
			try {
				TACommandExecutor.getInstance().enqueueCommand(commandKey,
						request, this);
			} catch (TANoSuchCommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TALogger.i(BaseApplication.this, "Enqueued command");

		}

	}
}

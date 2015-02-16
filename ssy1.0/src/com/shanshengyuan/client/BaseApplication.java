package com.shanshengyuan.client;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioRecord;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.utils.Log;
import com.shanshengyuan.client.utils.SSYAppException;
import com.shanshengyuan.client.utils.Settings;
import com.ta.exception.TANoSuchCommandException;
import com.ta.mvc.command.TACommandExecutor;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.mvc.common.TAResponse;
import com.ta.util.TALogger;

import java.io.File;
import java.util.LinkedList;
import java.util.List;



public class BaseApplication extends FrontiaApplication implements
		TAIResponseListener {

	public static BaseApplication mInstance = null;
	public final static String RECEIVER_LOGING = "android.intent.action.loginreceiver";
	public final static String RECEIVER_NO_LOGING = "android.intent.action.nologinreceiver";
	public final static String RECEIVER_SETTING_BIND_PHONE = "android.intent.action.bindphonereceiver";

	// 客户端定义的缓存资源存储目录aa
	public static final String SD_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/ssyfooder";

	private List<Activity> activityList = new LinkedList<Activity>();
	public final static String RECEIVER_NOTICE = "android.intent.action.noticereceiver";

	public static final String VERSION = "0.9.0";
	public static final String API_VERSION = "0.9.0";

	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	public static int DENSITY = 0;

	public static String sIMEI = "";
	public static String sDeviceKey;

	// 经度
	public static String longitude;
	// 纬度
	public static String latitude;

    public static boolean isFrist1 = false;
    public static boolean isFrist2 = false;
    public static boolean isFrist3 = false;
    public static boolean isFrist4 = false;
    public static boolean isFrist5 = false;

	public static boolean isVersionFrist = true; //是否第一次检测版本更新
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
		initImageCtche();
		doOncreate();
		onAfterCreateApplication();
		// getAppManager();
		mInstance = this;
		initWidthAndHeight();
		ApplicationInfo appInfo = null;
        TelephonyManager telephonyManager = null;
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
            telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
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
		CHANNEL_ID = appInfo.metaData.getString("UMENG_CHANNEL");

		if ("android.91.com".equals(CHANNEL_ID)) {
			CHANNEL_ID_INT = 1;
		}
        //处理平板无imei
        if(telephonyManager.getDeviceId()!=null){
            if (!telephonyManager.getDeviceId().equals("Unknow")) {
                sIMEI = telephonyManager.getDeviceId();
            }
        }


		sDeviceKey = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Log.e("sDeviceKey", sDeviceKey);
	

//		// 从缓存取百度userid
//		String userId = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
//				.getString(PushMessageReceiver.BAIDU_PUSH_USER_ID, null);
//		if (!StringUtils.isEmpty(userId)) {
//			Log.e("userId", userId);
//		}

//		Config.BAIDU_USER = userId;
		SSYImageCache.getInstance().init(this);
		//Settings.setFuFuUser("");
		//Settings.setInWebIm(false);
		// initDB
		
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
	
	@SuppressWarnings("deprecation")
	private void initImageCtche() {
		File cacheDir =StorageUtils.getOwnCacheDirectory(this, "SSYFooderImage/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration 
		          .Builder(this) 
		          .memoryCacheExtraOptions(480, 320) // maxwidth, max height，即保存的每个缓存文件的最大长宽
		          .threadPoolSize(5)//线程池内加载的数量
		          .threadPriority(Thread.NORM_PRIORITY -2) 
		          .denyCacheImageMultipleSizesInMemory() 
		         // .memoryCache(new UsingFreqLimitedMemoryCache(2* 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现 
		          .memoryCache(new WeakMemoryCache()) 
		          .memoryCacheSize(4 * 1024 * 1024)
		           .tasksProcessingOrder(QueueProcessingType.LIFO) 
		           .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径 
		           .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) 
		           .imageDownloader(new BaseImageDownloader(this,5 * 1000, 20 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间 
		           .writeDebugLogs() // Remove for releaseapp 
		          .build();//开始构建 
		ImageLoader.getInstance().init(config);
	}

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
		activityList.clear();
		SSYImageCache.getInstance().clearAll();
		//android.os.Process.killProcess(android.os.Process.myPid());
		//System.exit(0);
	}

    public void exitAll(){
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
        SSYImageCache.getInstance().clearAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

	/***/
	private void doOncreate() {
		// TODO Auto-generated method stub
		this.application = this;
		// 注册App异常崩溃处理�?
//		SSYAppException crashHandler = SSYAppException.getInstance();
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

	@Override
	public void onSuccess(TAResponse response) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRuning(TAResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(TAResponse response) {
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
	public void onStart() {
		// TODO Auto-generated method stub

	}
}

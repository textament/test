package com.shanshengyuan.client.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.text.format.Formatter;
import android.widget.Toast;

import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.ServiceConstants;
import com.shanshengyuan.client.activity.WelcomeActivity;
import com.shanshengyuan.client.model.fb.FeedbackRequest;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class SSYAppException implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static SSYAppException INSTANCE = new SSYAppException();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	/** 保证只有一个CrashHandler实例 */
	private SSYAppException() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static SSYAppException getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context)
    {		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			Intent intent = new Intent(mContext.getApplicationContext(),
					WelcomeActivity.class);
			PendingIntent restartIntent = PendingIntent.getActivity(
					mContext.getApplicationContext(), 0, intent,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			// 退出程序
			Settings.init(mContext);
			long now = System.currentTimeMillis();
			if (now - Settings.getRestartTime() > 60 * 60 * 1000) {
				Settings.setRestartTime(now);
				AlarmManager mgr = (AlarmManager) mContext
						.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
						restartIntent); // 1秒钟后重启应用
				
			}
			((BaseApplication) mContext).exitAll();

		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,我们的技术人员将会对其进行处理",
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		if (getTotalMemory() <= 512) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SSYFooder\n");
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		sb.toString();
		final String log = sb.toString();
		new Thread(new Runnable() {

			@Override
			public void run() {
			 FeedbackRequest request = new FeedbackRequest();

				try {
					request.setContent(log);
					request.setName("allen@shanshengyuan.com");
					request.setDk();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String jsonStr = JSONHelper.objToJson(request);
				Log.d(TAG + "request:", jsonStr);
				HttpEntity entity = new ByteArrayEntity(jsonStr.getBytes());
				final String url = NetworkHelper
						.executeUrl(ServiceConstants.SERVICE_FEED_BACK);
				NetworkHelper.asyncHttpClient.post(mContext, url, entity,
						"application/json; charset=utf-8",
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								Log.i(TAG + " Response: =====>", content);
							}

							@Override
							public void onFailure(Throwable error) {
								super.onFailure(error);
							}
						});
			}
		}).start();

		return null;
	}

	public String getAvailMemory() {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(mContext, mi.availMem);// 将获取的内存大小规格化
	}

	public float getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() ;// 获得系统总内存，单位是KB
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory/1024f;// Byte转换为KB或者MB，内存大小规格化
	}
}

package com.shanshengyuan.client.ziguang;

import java.lang.ref.WeakReference;

import com.shanshengyuan.client.activity.WelcomeActivity;

import android.os.Message;
import cn.com.zwan.core.ui.BaseHandler;

public class AppLauncherHandler extends BaseHandler {
	public static final int APPLAUNCHER_SUCCESS=10001;//启动成功的线程
	private WeakReference<WelcomeActivity> mActivity;
	public AppLauncherHandler(WelcomeActivity activity) {
		mActivity = new WeakReference<WelcomeActivity>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case APPLAUNCHER_SUCCESS:
		{
//			Intent intent = new Intent();
//			intent.setClass(mActivity.get(), LoginActivity.class);
//			mActivity.get().startActivity(intent);
//			mActivity.get().finish();//结束本页面
			break;
		}
		default:
			super.handleMessage(msg);
		}
	}

}
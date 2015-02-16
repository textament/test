package com.sststore.client.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.sststore.client.model.user.UserInfo;

public class Settings {

	private static final String APP = "ssy_fooder";
	private static final String USER_INFO = "user_info"; // 用户信息
	private static final String IS_REMIND_UPDATE = "is_remind"; // 是否开启智能推送设置
	private static final String IS_OPEN = "is_open"; // 开关
	private static final String IS_REMIND_SHAKE = "is_shakne"; // 是否再提摇一摇
	private static final String IS_REMIND_SYSTEM = "is_system"; // 是否提醒升级

	private static final String REG_EMAIL = "reg_email"; // 邮箱注册时绑定手机流程后登录时使用
	private static final String REG_PWD = "pwd"; // 邮箱注册时绑定手机流程后登录时使用

	public static final String IS_FIRST_START = "is_first"; // 是否第一次打开app进入欢迎界面

	public static final String AD_URL = "ad_url"; // 首页闪屏广告地址

	public static final String LAST_VERSION_CODE = "version_code"; // 上一次的客户端版本号
	public static final String TASK_SHARE_MSG = "share_task_msg"; // 是否关闭过任务分享提示
	public static final String SERVER_SHARE_MSG = "share_server_msg"; // 是否关闭过服务分享提示
	public static final String WORK_SHARE_MSG = "share_work_msg"; // 是否关闭过服务分享提示
	public static final String SCREEN_TIPS_MSG = "screen_tips_msg"; // 是否弹出过全屏提示

	public static final String MYPWD = "password"; // 密码
	public static final String USERNAME = "username"; // dengluming
	public static final String RESTART_TIME = "restart_time"; // dengluming
	public static final String FUFU_USER = "fufu"; // 呼呼聊天对象
	public static final String IN_WEBIM = "webim"; // 是否在
	public static SharedPreferences preferences;

	public static void init(Context context) {
		if (preferences == null) {
			preferences = context.getSharedPreferences(APP,
					Context.MODE_PRIVATE);
		}
	}

	public static void setAdUrl(String url) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putString(AD_URL, url);
		edit.commit();
	}

	public static String getAdUrl() {
		String url = preferences.getString(AD_URL, null);
		return url;
	}

	public static void setUserName(String name) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putString(USERNAME, name);
		edit.commit();
	}

	public static String getUserName() {
		String name = preferences.getString(USERNAME, null);
		return name;
	}

	public static void setVersionCode(int code) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putInt(LAST_VERSION_CODE, code);
		edit.commit();
	}

	public static int getVersionCode() {
		int code = preferences.getInt(LAST_VERSION_CODE, 0);
		return code;
	}

	public static void setScreenTipsMsg(boolean code) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(SCREEN_TIPS_MSG, code);
		edit.commit();
	}

	public static boolean getScreenTipsMsg() {
		boolean b = preferences.getBoolean(SCREEN_TIPS_MSG, false);
		return b;
	}

	public static void setRestartTime(long time) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putLong(RESTART_TIME, time);
		edit.commit();
	}

	public static long getRestartTime() {
		long time = preferences.getLong(RESTART_TIME, 0);
		return time;
	}

//	public static void setMyPwd(String pwd) {
//		String mpwd = null;
//		try {
//			mpwd = EncryptUtils.getInstance().encrypt(pwd, MYPWD);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		SharedPreferences.Editor edit = preferences.edit();
//		edit.putString(MYPWD, mpwd);
//		edit.commit();
//	}
//
//	public static String getMyPwd() {
//		String pwd = preferences.getString(MYPWD, null);
//		String mpwd = null;
//		mpwd = EncryptUtils.getInstance().decrypt(pwd, MYPWD);
//		return mpwd;
//	}

	public static void setShareTaskMsg(boolean code) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(TASK_SHARE_MSG, code);
		edit.commit();
	}

	public static boolean getShareTaskMsg() {
		boolean b = preferences.getBoolean(TASK_SHARE_MSG, false);
		return b;
	}

	public static void setShareServerMsg(boolean code) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(SERVER_SHARE_MSG, code);
		edit.commit();
	}

	public static boolean getShareServerMsg() {
		boolean b = preferences.getBoolean(SERVER_SHARE_MSG, false);
		return b;
	}

	public static void setShareWorkMsg(boolean code) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(WORK_SHARE_MSG, code);
		edit.commit();
	}

	public static boolean getShareWorkMsg() {
		boolean b = preferences.getBoolean(WORK_SHARE_MSG, false);
		return b;
	}
	public static void setInWebIm(boolean flag) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IN_WEBIM, flag);
		edit.commit();
	}
	
	public static boolean isInWebIm() {
		boolean b = preferences.getBoolean(IN_WEBIM, false);
		return b;
	}

	public static void setFuFuUser(String fufuUser) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putString(FUFU_USER, fufuUser);
		edit.commit();
	}

	public static String getFuFuUser() {
		String fufuUser = preferences.getString(FUFU_USER, "");
		return fufuUser;
	}

	public static void saveBitMap(ImageView imageView) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 将ImageView组件中的图像压缩成Png格式，并将压缩结果保存在ByteArrayOutputStream对象中
		((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(
				CompressFormat.PNG, 100, baos);
		String imageBase64 = new String(Base64.encodeBytes(baos.toByteArray()));
		// 保存由图像字节流转换成的Base64格式字符串
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("productImage", imageBase64);
		editor.commit();
	}

	public static void LoadBitMap(ImageView imageView) {
		String imageBase64 = preferences.getString("productImage", "");
		byte[] base64Bytes;
		try {
			base64Bytes = Base64.decode(imageBase64.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			// 在ImageView组件上显示图像
			imageView.setImageDrawable(Drawable.createFromStream(bais,
					"product_image"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void saveUserInfo(UserInfo user) {
		try {
			if (user == null) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(USER_INFO, null);
				editor.commit();
			} else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(user);

				String user64 = new String(Base64.encodeBytes(baos
						.toByteArray()));
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(USER_INFO, user64);
				editor.commit();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static UserInfo loadUserInfo() {
		UserInfo user = null;
		try {

			String user64 = preferences.getString(USER_INFO, null);
			if (user64 == null) {
				return null;
			}
			byte[] deUser64 = Base64.decode(user64.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(deUser64);
			ObjectInputStream ois = new ObjectInputStream(bais);
			user = (UserInfo) ois.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;

	}


	public static Boolean getIsOpen() {
		return preferences.getBoolean(IS_OPEN, true);
	}

	public static void setIsOpen(boolean isShow) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IS_OPEN, isShow);
		edit.commit();
	}

	public static Boolean getIsSystem() {
		return preferences.getBoolean(IS_REMIND_SYSTEM, false);
	}

	public static void setIsSystem(boolean isShow) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IS_REMIND_SYSTEM, isShow);
		edit.commit();
	}

	public static Boolean getIsRemindUpdate() {
		return preferences.getBoolean(IS_REMIND_UPDATE, true);
	}

	public static void setIsRemindUpdate(boolean isShow) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IS_REMIND_UPDATE, isShow);
		edit.commit();
	}

	public static Boolean getIsRemindShake() {
		return preferences.getBoolean(IS_REMIND_SHAKE, true);
	}

	public static void setIsRemindShake(boolean isShow) {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IS_REMIND_SHAKE, isShow);
		edit.commit();
	}

	public static void setIsFirstStart() {
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean(IS_FIRST_START, true);
		edit.commit();

	}

	/**
	 * return boolean app is first start
	 * 
	 */
	public static boolean getIsFirstStart() {

		if (preferences.getBoolean(IS_FIRST_START, true)) {

			SharedPreferences.Editor edit = preferences.edit();
			edit.putBoolean(IS_FIRST_START, false);
			edit.commit();

			return true;
		} else {
			return false;
		}
	}
}
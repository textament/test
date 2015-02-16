package com.sststore.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sststore.client.activity.OnResultListener;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.util.TALogger;
import com.ta.util.netstate.TANetWorkUtil.netType;

/**
 * @author allen
 */

public class BaseActivity extends Activity implements OnGestureListener,
		OnTouchListener, OnResultListener {

	GestureDetector detector;

	private Animation mShowAnim = null; // 显示提示动画
	private Animation mCloseAnim = null; // 关闭提示动画
	/** 模块的名字 */
	private String moduleName = "";


	private boolean _onDestroy;

	private String _level;

	private static final int DIALOG_ID_PROGRESS_DEFAULT = 0x174980;

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		detector = new GestureDetector(this, this);
	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
		
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		_onDestroy = true;
		super.onDestroy();

	}

	public void handleError(ErrorResponse e, int result) {

		if (e != null) {
			// showDialogMessage(e); // use toast instead.
			// 注册时后台出现的一个BUG，暂时不显示提示消息
			if (result != 10047)
				Toast.makeText(this, e.getMsg(), Toast.LENGTH_LONG).show();
		}
	}



	public void onErrorDialogClosed() {

	}

	// 联系
	protected void contact(final String uid) {
		contact(uid, "5");
	}

	protected void contact(final String uid, String level) {
		_level = level;
		if (uid == null) {
			return;
		}
		
	}

	


	public void closeEditView() {
		// 关闭软键盘
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// protected void back(View v) {
	// this.finish();
	// }

	public static int getVersionCode(Context context) { // 获取版本号(内部识别号)

		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		// if (e2.getX() - e1.getX() > 50) {
		// 为什么是50？ 这个根据你的模拟器大小来定，看看模拟器宽度，e2.getX()-e1.getX()<屏幕宽度就ＯＫ
		// Toast.makeText(getApplicationContext(), "向右滑动",
		// Toast.LENGTH_LONG).show();
		// }

		return false;
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}

	public BaseApplication getBaseApplication() {
		return (BaseApplication) getApplication();
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		// 由于view必须在视图记载之后添加注入
		onAfterSetContentView();
	}

	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		// 由于view必须在视图记载之后添加注入
		onAfterSetContentView();
	}

	public void setContentView(View view) {
		super.setContentView(view);
		// 由于view必须在视图记载之后添加注入
		onAfterSetContentView();
	}

	protected void onAfterSetContentView() {

	}

	/**
	 * 获取模块的名字
	 */
	public String getModuleName() {
		String moduleName = this.moduleName;
		if (moduleName == null || moduleName.equalsIgnoreCase("")) {
			moduleName = getClass().getName().substring(0,
					getClass().getName().length() - 8);
			String arrays[] = moduleName.split("\\.");
			this.moduleName = moduleName = arrays[arrays.length - 1]
					.toLowerCase();
		}
		return moduleName;
	}

	/**
	 * 设置模块的名字
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID_PROGRESS_DEFAULT:
			ProgressDialog dlg = new ProgressDialog(this);
			// dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dlg.setMessage("正在加载...");
			dlg.setCancelable(true);
			return dlg;
		default:
			return super.onCreateDialog(id);

		}
	}

	public final void doCommand(int resId, TARequest request) {
		String commandKey = getString(resId);
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(String commandKey, TARequest request) {
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener) {
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener) {
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress) {
		String commandKey = getString(resId);
		TALogger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress) {
		TALogger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record) {
		String commandKey = getString(resId);
		TALogger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record) {
		TALogger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(int resId, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack) {
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, showProgress, record,
				resetStack);
	}

	public final void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack) {
		if (showProgress) {
			showProgress();
		}
		getBaseApplication().doCommand(commandKey, request, listener, record,
				resetStack);
	}

	/**
	 * 需要自定义进度条，请重写
	 */
	protected void showProgress() {
		showDialog(DIALOG_ID_PROGRESS_DEFAULT);
	}

	/**
	 * 隐藏进度跳，需要重写的请重写
	 */
	protected void hideProgress() {
		try {
			removeDialog(DIALOG_ID_PROGRESS_DEFAULT);
		} catch (IllegalArgumentException iae) {
		}
	}

	/**
	 * 网络连接连接时调用
	 */
	public void onConnect(netType type) {

	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect() {

	}

	@Override
	public void onSuccess(int actionType) {
		

	}

	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub

	}

}

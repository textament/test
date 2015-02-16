/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;

import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;

import com.shanshengyuan.client.utils.StringUtils;
import com.shanshengyuan.client.widgets.ProgressWebView;
import com.shanshengyuan.client.widgets.ProgressWebView.ScrollInterface;
/**
 * @author lihao
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class AdverWebActivity extends BaseActivity implements
		OnResultListener {

	private View mBack;
	AdverWebActivity self = null;
	private ProgressWebView mWebView;

	private String adurl;
	private String url = "";
	Button mShareButton;
	private boolean isShare;
	private String mMessage = "";
	
	 private ProgressBar progressbar;
	 
	 private Bundle b = null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.webview_main_layout);
		initTopBar();
		
		mWebView = (ProgressWebView) findViewById(R.id.webview_main);
		
		  progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
	        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 6, 0, 0));
	        mWebView.addView(progressbar);
	        mWebView.setOnCustomScroolChangeListener(new ScrollInterface() {
				
				@Override
				public void onSChanged(int l, int t, int oldl, int oldt) {
					 LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
				        lp.x = l;
				        lp.y = t;
				        progressbar.setLayoutParams(lp);

				}
			});
		Bundle bundle = getIntent().getExtras();
		adurl = bundle.getString("adurl");
		if (!StringUtils.isEmpty(adurl)) {
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(
					true);
			mWebView.requestFocus();
			mWebView.getSettings().setSupportZoom(true);
			mWebView.getSettings().setUseWideViewPort(true);
			mWebView.getSettings().setBuiltInZoomControls(true); // 支持页面放大缩小按钮
			mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			mWebView.setWebViewClient(new HelloWebViewClient());
			mWebView.setWebChromeClient(new MyWebChromeClient());
		//	mWebView.setDownloadListener(new MyWebViewDownLoadListener());
			mWebView.loadUrl(adurl);
		}

	}
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
		

	}
	
//	private void showLogin() {
//
//		Intent intent = new Intent();
//		intent.setClass(this, LoginActivity.class);
//		intent.putExtra("from", 10);
//		BaseApplication.mComeFrom = 10;
//		startActivityForResult(intent, 0);
//
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0x1234) {
//			if(Settings.loadUserInfo()!=null){
//				mSystemTimeRequest = new SystemTimeRequest();
//				mSystemTimeRequest.setToken(Settings.loadUserInfo().getToken());
//				mUserController.execute(Actions.ACTION_SYSTEM_TIME, mSystemTimeRequest);
//			}
		}
	}

	class MyWebChromeClient extends WebChromeClient {

		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
//			if (message.equals("login")) {
//				showLogin();
//				result.cancel();
//				return true;
//			} else if (message.equals("back")) {
//				AdverWebActivity.this.finish();
//				result.cancel();
//				return true;
//			}
//			if (isShare) {
//				isShare = false;
//				mMessage = message;
//				try {
//					ShareSDK.initSDK(self);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				showShare(false, null);
//				result.cancel();
//				return true;
//			}
			
			if(message.contains("dishId")){
				String[] s = message.split("=");
				Intent intent = new Intent();
				intent.setClass(self, DishDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("dishId", s[1]);
				intent.putExtras(bundle);
				startActivity(intent);
				result.cancel();
				return true;
			}
			return false;
		}
		
		  @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	                progressbar.setVisibility(view.GONE);
	            } else {
	                if (progressbar.getVisibility() == view.GONE)
	                    progressbar.setVisibility(view.VISIBLE);
	                progressbar.setProgress(newProgress);
	            }
	            super.onProgressChanged(view, newProgress);
	        }
	}

//	private void refresh() {
//
//		UserInfo user = UserController.getUser();
//		if (user != null) {
//			String webToken = user.getWebtoken();
//			String user_id = user.getUser_id();
//			String userName =user.getUsername();
//			StringBuffer b = new StringBuffer();
//			b.append(url);
//			b.append("?u=");
//			b.append(user_id);
//			//b.append("&n=");
//			//b.append(userName);
//			b.append("&p=");
//			// b.append("www.zhubajie.com");
//			try {
//				b.append(URLEncoder.encode(adurl.replace("http://", ""),
//						"utf-8"));
//			} catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}
//			b.append("&w=");
//			String t = "";
//			if(StringUtils.isEmpty(BaseApplication.systime))
//			t = System.currentTimeMillis()+"";
//			else{
//				t = BaseApplication.systime;
//			}
//			try {
//				b.append(EncryptUtils.getInstance().encrypturl(
//						Base64.encodeBytes(user_id.getBytes("utf-8")).getBytes("utf-8"),
//						String.valueOf(t).getBytes("utf-8")));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			b.append("&t=");
//			b.append(t + "");
//			Log.d("=------", b.toString());
//			mWebView.loadUrl(b.toString());
//		} else {
//			CookieSyncManager.createInstance(this);
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.setAcceptCookie(true);
//			cookieManager.removeAllCookie();
//			CookieSyncManager.getInstance().sync();
//			mWebView.loadUrl(adurl);
//		}
//
//	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		 @Override
		    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

		        // *** NEVER DO THIS!!! ***
		        // super.onReceivedSslError(view, handler, error);

		        // let's ignore ssl error
		        handler.proceed();
		    }

		
	}
	
	

	private void initTopBar() {
		mBack = findViewById(R.id.back);
		mShareButton = (Button) findViewById(R.id.call_share);
		mShareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isShare = true;
				mWebView.loadUrl("javascript:for_android()");

			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	@Override
	public void onSuccess(int actionType) {
	//	refresh();
		super.onSuccess(actionType);
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
	//	refresh();
		super.onFailed(actionType, result);
	}

}

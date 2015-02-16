/**
 * 
 */
package com.shanshengyuan.client.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.R;

/**
 * @author lihao
 * 
 */
public class MenuButtomView extends RelativeLayout implements OnClickListener {

	private View mView;
	private Context context;
	protected LayoutInflater mInflater = null;

	public static boolean isFrist1 = true;
	public static boolean isFrist2 = true;
	public static boolean isFrist3 = true;
	public static boolean isFrist4 = true;
	public static boolean isFrist5 = true; // 底部tab是否第一次已经加载

	// 菜单栏
	LinearLayout menubg1;
	ImageView selectView1;
	TextView mfont1;

	LinearLayout menubg2;
	ImageView selectView2;
	TextView mfont2;

	LinearLayout menubg3;
	ImageView selectView3;
	TextView mfont3;

	LinearLayout menubg4;
	ImageView selectView4;
	TextView mfont4;

	LinearLayout menubg5;
	ImageView selectView5;
	TextView mfont5;

	Resources resource = null;
	ColorStateList csl = null;

	public MenuButtomView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public MenuButtomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		resource = (Resources) context.getResources();
		csl = (ColorStateList) resource.getColorStateList(R.color.orange);
		mInflater = LayoutInflater.from(context);
		mView = mInflater.inflate(R.layout.menu_buttom, null);
		menubg1 = (LinearLayout) mView.findViewById(R.id.menu_layout_1);
		selectView1 = (ImageView) mView.findViewById(R.id.menu_1);
		mfont1 = (TextView) mView.findViewById(R.id.view_1);
		menubg1.setTag(0);
		selectView1.setTag(0);
		menubg1.setOnClickListener(this);

		menubg2 = (LinearLayout) mView.findViewById(R.id.menu_layout_2);
		selectView2 = (ImageView) mView.findViewById(R.id.menu_2);
		mfont2 = (TextView) mView.findViewById(R.id.view_2);
		menubg2.setTag(1);
		selectView2.setTag(1);
		menubg2.setOnClickListener(this);

		menubg3 = (LinearLayout) mView.findViewById(R.id.menu_layout_3);
		selectView3 = (ImageView) mView.findViewById(R.id.menu_3);
		mfont3 = (TextView) mView.findViewById(R.id.view_3);
		menubg3.setTag(2);
		selectView3.setTag(2);
		menubg3.setOnClickListener(this);

		menubg4 = (LinearLayout) mView.findViewById(R.id.menu_layout_4);
		selectView4 = (ImageView) mView.findViewById(R.id.menu_4);
		mfont4 = (TextView) mView.findViewById(R.id.view_4);
		menubg4.setTag(3);
		selectView4.setTag(3);
		menubg4.setOnClickListener(this);

		menubg5 = (LinearLayout) mView.findViewById(R.id.menu_layout_5);
		selectView5 = (ImageView) mView.findViewById(R.id.menu_5);
		mfont5 = (TextView) mView.findViewById(R.id.view_5);
		menubg5.setTag(4);
		selectView5.setTag(4);
		menubg5.setOnClickListener(this);

		this.addView(mView);
	}

	public void setJmpActivityListener(View.OnClickListener listener) {
		// menubg1.setOnClickListener(listener);
		// menubg2.setOnClickListener(listener);
		// menubg3.setOnClickListener(listener);
		// menubg4.setOnClickListener(listener);
		// menubg5.setOnClickListener(listener);
	}

	public void showClickType(int type) {
		switch (type) {
		case 0:
			//selectView1.setBackgroundResource(R.drawable.down_orange1);
			if (csl != null) {
				mfont1.setTextColor(csl);
			}
			break;
		case 1:
		//	selectView2.setBackgroundResource(R.drawable.down_orange3);
			if (csl != null) {
				mfont2.setTextColor(csl);
			}
			break;
		case 2:
		//	selectView3.setBackgroundResource(R.drawable.down_orange2);
			if (csl != null) {
				mfont3.setTextColor(csl);
			}
			break;
		case 3:
		//	selectView4.setBackgroundResource(R.drawable.down_orange4);
			if (csl != null) {
				mfont4.setTextColor(csl);
			}
			break;
		case 4:
		//	selectView5.setBackgroundResource(R.drawable.down_orange5);
			if (csl != null) {
				mfont5.setTextColor(csl);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 0:
			// 发需求
			if (BaseApplication.isFrist1) {
				Intent intent = new Intent();
		//		intent.setClass(context, MainNewFragment.class);
				context.startActivity(intent);
				((Activity) (context)).overridePendingTransition(0, 0);
				BaseApplication.isFrist1 = false;
				BaseApplication.isFrist2 = true;
				BaseApplication.isFrist3 = true;
				BaseApplication.isFrist4 = true;
				BaseApplication.isFrist5 = true;
				((Activity) (context)).finish();
			}

			break;
		case 1:

			// 需求市场
			if (BaseApplication.isFrist2) {
				Intent intent1 = new Intent();
		//		intent1.setClass(context, TaskMarketFragment.class);
				context.startActivity(intent1);
				((Activity) (context)).overridePendingTransition(0, 0);
				BaseApplication.isFrist1 = true;
				BaseApplication.isFrist2 = false;
				BaseApplication.isFrist3 = true;
				BaseApplication.isFrist4 = true;
				BaseApplication.isFrist5 = true;
				((Activity) (context)).finish();
			}

			break;
		case 2:
			// 精选服务
			if (BaseApplication.isFrist3) {
				Intent intent1 = new Intent();
			//	intent1.setClass(context, ServerHappyFragment.class);
				context.startActivity(intent1);
				((Activity) (context)).overridePendingTransition(0, 0);
				BaseApplication.isFrist1 = true;
				BaseApplication.isFrist2 = true;
				BaseApplication.isFrist3 = false;
				BaseApplication.isFrist4 = true;
				BaseApplication.isFrist5 = true;
				((Activity) (context)).finish();
			}

			break;
		case 3:
			// 我的需求
			//取消点击我的需求直接进入登录ye
			//if (UserController.getUser() != null) {
				if (BaseApplication.isFrist4) {
					Intent intent2 = new Intent();
			//		intent2.setClass(context, MyReleaseServiceFragment.class);
					context.startActivity(intent2);
					((Activity) (context)).overridePendingTransition(0, 0);
					BaseApplication.isFrist1 = true;
					BaseApplication.isFrist2 = true;
					BaseApplication.isFrist3 = true;
					BaseApplication.isFrist4 = false;
					BaseApplication.isFrist5 = true;
					((Activity) (context)).finish();
				}

//			} else {
//				Intent intent = new Intent();
//				intent.setClass(context, LoginActivity.class);
//				intent.putExtra("from", 1);
//				BaseApplication.mComeFrom = 1;
//				((Activity) (context)).startActivityForResult(intent, 6);
//			}
			break;
		case 4:
			// 用户中心
			//取消点击用户中心直接进入登录ye
			//if (UserController.getUser() != null) {
				if (BaseApplication.isFrist5) {
					Intent intent4 = new Intent();
				//	intent4.setClass(context, UserCenterNewFragment.class);
					context.startActivity(intent4);
					((Activity) (context)).overridePendingTransition(0, 0);
					BaseApplication.isFrist1 = true;
					BaseApplication.isFrist2 = true;
					BaseApplication.isFrist3 = true;
					BaseApplication.isFrist4 = true;
					BaseApplication.isFrist5 = false;
					((Activity) (context)).finish();
				}

//			} 
//			else {
//				Intent intent = new Intent();
//				intent.setClass(context, LoginActivity.class);
//				intent.putExtra("from", 6);
//				BaseApplication.mComeFrom = 6;
//				((Activity) (context)).startActivityForResult(intent, 6);
//				// finish();
//			}
			break;
		default:
			break;
		}
	}
}

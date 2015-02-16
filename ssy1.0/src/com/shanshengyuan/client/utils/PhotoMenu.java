/**
 * PhotoMenu.java
 * com.zhubajie.client.views
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-6-19 		lihao
 *
 *  Copyright (c) 2012 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.BaseApplication;

/**
 * ClassName:PhotoMenu Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2012-6-19
 * 
 * @see
 */
public class PhotoMenu extends Dialog {

	private Context context = null;
	private String tempName = null;

	private LinearLayout layout = null;

	public PhotoMenu(Context context) {
		super(context);
		this.context = context;
	}

	public PhotoMenu(Context context, View view) {
		super(context);
		this.context = context;
		this.setContentView(view);
		LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		layout.gravity = Gravity.BOTTOM;
		view.setLayoutParams(layout);

	}

	public void show() {
		if (!this.isShowing()) {
			super.show();
		}
	}

	public void dismiss() {
		if (this.isShowing()) {
			super.dismiss();
		}
	}

	// 动态创建弹出菜单
	public void createPhotoMenu() {

		layout = new LinearLayout(context);
		LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(parentParams);
		layout.setOrientation(LinearLayout.VERTICAL);
		Window window = this.getWindow();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
	//	window.setWindowAnimations(R.style.AnimBottom); // 添加动画

		this.setContentView(layout);

	}

	public View createClickBtn(String text, View.OnClickListener click) {

		TextView textView = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				BaseApplication.WIDTH-40,
				120);
		
		textView.setLayoutParams(lp);
		textView.setTextSize(18f);
		textView.setTextColor(Color.BLACK);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER);
	//	textView.setBackgroundResource(R.drawable.white_yellow_selector);
		textView.setOnClickListener(click);
		
		TextView view = new TextView(context);
		LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(
				BaseApplication.WIDTH-40,
				1);
		view.setLayoutParams(lpp);
		view.setTextAppearance(context, R.style.style_default_line_nobuttom);
		layout.addView(textView);
		layout.addView(view);

		return textView;
	}

}

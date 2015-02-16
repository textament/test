package com.shanshengyuan.client.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.BaseApplication;

public class ListViewPaper extends ViewPager {

	public static final String tag = "ListViewPager";
	private View mView = null;
	
	private ImageView mLeftImage;
	private TextView mLeftView;

	private ImageView mRightImage;
	private TextView mRightView;

	public ListViewPaper(Context context) {
		super(context);
	}

	public ListViewPaper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setLineView(View v) {
		mView = v;
	}
	
	public void setTipScrollColorView(ImageView leftImg,TextView lv,ImageView rightImg,TextView rv){
		mLeftImage = leftImg;
		mLeftView = lv;
		mRightImage = rightImg;
		mRightView = rv;
	}

	@Override
	public void setOffscreenPageLimit(int limit) {
		super.setOffscreenPageLimit(limit);
		Log.i(tag, "setOffscreenPageLimit:" + limit);
	}

	@Override
	protected void onMeasure(int arg0, int arg1) {
		super.onMeasure(arg0, arg1);
	}

	private final int viewWidth = ConvertUtils.dip2px(getContext(), 160);
	private final int viewHeight = ConvertUtils.dip2px(getContext(), 4);
	private final int viewX = (BaseApplication.WIDTH - viewWidth * 2) / 4;
	private final int viewY = ConvertUtils.dip2px(getContext(), 56);

	@Override
	protected void onPageScrolled(int arg0, float arg1, int offsetX) {
		if (mView != null && offsetX > 0) {
			mView.layout(viewX + offsetX / 2, viewY, viewX + viewWidth
					+ offsetX / 2, viewY + viewHeight);
		}
		
		if(arg0==1){
//			mRightImage.setBackgroundResource(R.drawable.release_right_icon);
//			mRightView.setTextColor(getResources().getColor(R.color.orange));
//			
//			mLeftImage.setBackgroundResource(R.drawable.release_left_icon_off);
//			mLeftView.setTextColor(getResources().getColor(R.color.graydk));
		}else{
//			mRightImage.setBackgroundResource(R.drawable.release_right_icon_off);
//			mRightView.setTextColor(getResources().getColor(R.color.graydk));
//			
//			mLeftImage.setBackgroundResource(R.drawable.release_left_icon);
//			mLeftView.setTextColor(getResources().getColor(R.color.orange));
		}
		super.onPageScrolled(arg0, arg1, offsetX);

	}
	
	
}

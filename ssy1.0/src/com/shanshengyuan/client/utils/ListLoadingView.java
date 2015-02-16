/**
 * 
 */
package com.shanshengyuan.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.R;

/**
 * @author LIHAO
 * 
 */
public class ListLoadingView extends LinearLayout {

	LayoutInflater mInflater = null;

	private ImageView mLoading;
	
	private LinearLayout mLoadingLy;
	private LinearLayout mNetWorkLy;
	private TextView mTRes;
	private TextView mTSetting;

	private AnimationDrawable animationDrawable = null;
	
	private Runnable r;
	
	private OnClickListener click;

	public ListLoadingView(Context context) {
		super(context);
		init();
	}

	public ListLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	

	@SuppressLint("NewApi")
	public ListLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
	}

	public void init() {
		mInflater = LayoutInflater.from(getContext());
		View view = mInflater.inflate(R.layout.loading_progress, null);
		mLoading = (ImageView) view.findViewById(R.id.show_img);
		mLoadingLy = (LinearLayout)view.findViewById(R.id.loading_ly);
		mNetWorkLy = (LinearLayout)view.findViewById(R.id.network_ly);
		mTRes  = (TextView)view.findViewById(R.id.network_res);
		mTRes.setId(R.id.network_res);
		mTSetting = (TextView)view.findViewById(R.id.network_set);
		mTSetting.setId(R.id.network_set);
		mLoading.setBackgroundResource(R.anim.bajieloading);
		animationDrawable = (AnimationDrawable) mLoading.getBackground();
		mLoading.post(new Runnable() {
			@Override
			public void run() {
				Log.e("imageView post", "Go");
				animationDrawable.start();
			}
		});
		addView(view);
	}
	
	public void animStopAndViewGown(){
		animationDrawable.stop();
		mLoading.clearAnimation();
		this.setVisibility(View.GONE);
	}
	
	public void setLoadingVisible(){
		mLoadingLy.setVisibility(View.VISIBLE);
	}
	
	public void setLoadingGone(){
		mLoadingLy.setVisibility(View.GONE);
		mLoading.clearAnimation();
		animationDrawable.stop();
	}
	
	public void setNetWorkVisible(){
		mNetWorkLy.setVisibility(View.VISIBLE);
	}
	
	public void setNetWorkGone(){
		mNetWorkLy.setVisibility(View.GONE);
		mLoading.clearAnimation();
		animationDrawable.stop();
	}
	
	public void setNetWorkListener(OnClickListener click){
		mTSetting.setOnClickListener(click);
		mTRes.setOnClickListener(click);
	}
}

package com.shanshengyuan.client.utils;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.model.ad.Adver;
import com.shanshengyuan.client.model.dish.DishType;

public class ServiceDeplyView extends LinearLayout implements OnClickListener {

	private View mView;
	private Context context;
	protected LayoutInflater mInflater = null;

	//服务外层
	private LinearLayout mWaiLy;
	
	
	//前进入口隐藏
	private LinearLayout mShowLy;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;
	public ServiceDeplyView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ServiceDeplyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mInflater.inflate(R.layout.service_deploy, null);
		mWaiLy = (LinearLayout)mView.findViewById(R.id.server_outer_wai);
		mImageMap = new HashMap<String, ImageView>();
	}
	
	public void setAdType(Adver ad){
		//mAdName.setText("");
		//mAdImage.
		//adStr = ad.getJumpurl();
//		ZBJImageCache.getInstance().downloadImage(mImageMap, mAdImage,
//				ad.getPicurl(), false, new ImageCallback() {
//					@Override
//					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
//						if(bitmap!=null)
//						mImageMap.get(imageUrl).setImageBitmap(bitmap);
//
//					}
//				});
	}

	
	public void setServiceBgColor(){
		mWaiLy.setBackgroundColor(getResources().getColor(R.color.alpha));
	}
	
	public void setmOuterLy(){
		mWaiLy.setVisibility(View.GONE);
	}
	public void setmOuterLyVisible(){
		mWaiLy.setVisibility(View.VISIBLE);
	}
	
	public void setHideButtomView(){
		mShowLy.setVisibility(View.GONE);
	}
	
	public void setVisibleView(){
		mShowLy.setVisibility(View.VISIBLE);
	}
	
	public void createServiceView(List<DishType> servicelist){
		if(servicelist.size()!=0){
			int size = servicelist.size()/3;
			if(servicelist.size()%3!=0){
				size = size+1;
			}
			for (int i = 0; i < size; i++) {
				LinearLayout outer = new LinearLayout(context);
				LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				outer.setLayoutParams(lps);
				outer.setBackgroundColor(getResources().getColor(R.color.alpha));
				outer.setOrientation(LinearLayout.HORIZONTAL);
				int newSize = servicelist.size()%3;
				if(newSize==0){
					newSize = 3;
				}
				for (int j = 0; j <newSize; j++) {
					LinearLayout iner = new LinearLayout(context);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							ConvertUtils.dip2px(context, 60),1);
					iner.setWeightSum(1);
					iner.setLayoutParams(params);
					iner.setGravity(Gravity.CENTER);
					//iner.setPadding(5, 0, 5, 0);
					iner.setOrientation(LinearLayout.VERTICAL);
					iner.setId(Integer.parseInt(servicelist.get(j).getId()));
					iner.setOnClickListener(this);
					
					TextView textView = new TextView(context);
					LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					//textView.setSingleLine(true);
					textView.setTextColor(getResources().getColor(R.color.font_main));
					//textView.setEllipsize(TextUtils.TruncateAt.valueOf("MIDDLE")); 
					textView.setLayoutParams(textParams);
					textView.setGravity(Gravity.CENTER|Gravity.TOP);
					textView.setText(servicelist.get(j).getName());
					iner.addView(textView);
					outer.addView(iner);
				}
				mWaiLy.addView(outer);
			}
			
			
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	
}

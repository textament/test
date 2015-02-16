package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYFooderImageCache;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.model.dishDetail.Steps;
import com.shanshengyuan.client.utils.ConvertUtils;

public class DishDetailAdapter extends BaseAdapter {

	private List<Steps> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public DishDetailAdapter(Context context, List<Steps> list) {
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<Steps>();
		notifyDataSetChanged();
	}

	public void addListItems(List<Steps> list) {

		int length = list.size();

		for (int i = 0; i < length; i++) {
			mList.add(list.get(i));

		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();

	}

	public void removeItem(String id) {
		int i = 0;
		for (Steps t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public Steps getItem(int position) {

		return mList.get(position);

	}

	@Override
	public long getItemId(int position) {

		return 0;

	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;

		if (convertView == null) {

			holder = new ViewHolder();

			layout = mInflater.inflate(R.layout.item_dish_detail, null);
			holder.picImg = (ImageView) layout.findViewById(R.id.detail_img);
			holder.num = (TextView) layout.findViewById(R.id.detail_num);
			holder.content = (TextView) layout.findViewById(R.id.detail_content);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final Steps t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(Steps t) {
		int num = Integer.parseInt(t.getsNum())+1;
		holder.num.setText(num+"");
		holder.content.setText(t.getStepDescription());
				String url =  t.getStepImg();
		holder.picImg.setImageResource(R.drawable.defaule_img);
		
		SSYImageCache.getInstance().downloadImage(mImageMap, holder.picImg,
				url, false, new ImageCallback() {

					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						mImageMap.get(imageUrl).setImageBitmap(bitmap);

					}
				});
		
//		DisplayImageOptions op = SSYFooderImageCache.getInstance().initOption();
//		ImageSize mImageSize = new ImageSize(BaseApplication.WIDTH, 300);  
//		
//		ImageLoader.getInstance().loadImage(url, mImageSize, op, new ImageLoadingListener() {
//			
//			@Override
//			public void onLoadingStarted(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//				holder.picImg.setImageBitmap(arg2);  
//			}
//			
//			@Override
//			public void onLoadingCancelled(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	static class ViewHolder {
		TextView num;
		TextView content;
		ImageView picImg;
	}
}

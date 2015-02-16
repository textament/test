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
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.dish.DishesOrder;

public class OrderPageAdapter extends BaseAdapter {

	private List<DishesOrder> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public OrderPageAdapter(Context context, List<DishesOrder> list) {
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<DishesOrder>();
		notifyDataSetChanged();
	}

	public void addListItems(List<DishesOrder> list) {

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
		for (DishesOrder t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public DishesOrder getItem(int position) {

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

			layout = mInflater.inflate(R.layout.item_order_page, null);
			holder.picImg = (ImageView) layout.findViewById(R.id.orderpage_img);
			holder.titleName = (TextView) layout.findViewById(R.id.orderpage_name);
			holder.content = (TextView) layout.findViewById(R.id.orderpage_content);
			holder.amount = (TextView) layout.findViewById(R.id.orderpage_amount);
			holder.dishType = (TextView) layout.findViewById(R.id.orderpage_type);
			holder.todayView = (TextView)layout.findViewById(R.id.today_view);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final DishesOrder t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(DishesOrder t) {

		holder.titleName.setText(t.getDishName());
		holder.content.setText(t.getRemark());
		holder.amount.setText(t.getPrice());
		holder.dishType.setText("数量："+t.getNum());
		holder.picImg.setImageResource(R.drawable.defaule_img);
		String url = t.getImgUrl();
		SSYImageCache.getInstance().downloadImage(mImageMap, holder.picImg,
				url, false, new ImageCallback() {

					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						mImageMap.get(imageUrl).setImageBitmap(bitmap);

					}
				});
		if(t.getRepertory()!=0){
			holder.todayView.setVisibility(View.VISIBLE);
		}else{
			holder.todayView.setVisibility(View.GONE);
		}
		
	}

	static class ViewHolder {
		TextView titleName;
		ImageView picImg;
		TextView content;
		TextView amount;
		TextView dishType;
		TextView todayView;
	}


}

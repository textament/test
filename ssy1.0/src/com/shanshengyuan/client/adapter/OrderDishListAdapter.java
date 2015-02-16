/**
 * 
 */
package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.OrderListAdapter.ViewHolder;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.order.OrderList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author lihao
 *
 */
public class OrderDishListAdapter extends BaseAdapter {

	private List<DishesOrderFinish> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public OrderDishListAdapter(Context context, List<DishesOrderFinish> list) {
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<DishesOrderFinish>();
		notifyDataSetChanged();
	}

	public void addListItems(List<DishesOrderFinish> list) {

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
		for (DishesOrderFinish t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public DishesOrderFinish getItem(int position) {

		return mList.get(position);

	}

	@Override
	public long getItemId(int position) {

		return 0;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View layout = convertView;

		if (convertView == null) {

			holder = new ViewHolder();

			layout = mInflater.inflate(R.layout.item_order_list_sec, null);
			holder.picImg = (ImageView) layout.findViewById(R.id.order_img);
			holder.titleName = (TextView)layout.findViewById(R.id.order_name);
			holder.amount = (TextView)layout.findViewById(R.id.order_amount_single);
			holder.num = (TextView)layout.findViewById(R.id.order_num);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final DishesOrderFinish t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(DishesOrderFinish t) {

		String url = t.getImgUrl();
		holder.picImg.setImageResource(R.drawable.defaule_img);
		SSYImageCache.getInstance().downloadImage(mImageMap, holder.picImg,
				url, false, new ImageCallback() {

					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						mImageMap.get(imageUrl).setImageBitmap(bitmap);

					}
				});
//		
		holder.titleName.setText(t.getName());
		holder.amount.setText(t.getPrice());
		holder.num.setText(context.getString(R.string.order_num,t.getNum()));
	}

	static class ViewHolder {
		TextView titleName;
		ImageView picImg;
		TextView amount;
		TextView num;
		
	}

}

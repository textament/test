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

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.order.OrderList;

public class OrderListAdapter extends BaseAdapter {

	private List<OrderList> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public OrderListAdapter(Context context, List<OrderList> list) {
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<OrderList>();
		notifyDataSetChanged();
	}

	public void addListItems(List<OrderList> list) {

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
		for (OrderList t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public OrderList getItem(int position) {

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

			layout = mInflater.inflate(R.layout.item_order_list, null);
			holder.time = (TextView) layout.findViewById(R.id.order_time);
			holder.state = (TextView) layout.findViewById(R.id.order_state);
			holder.totalNum = (TextView) layout.findViewById(R.id.order_total);
			holder.amount = (TextView) layout.findViewById(R.id.order_amount);
			holder.dishList = (LinearLayout)layout.findViewById(R.id.list_ly);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final OrderList t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(OrderList t) {

		holder.time.setText(context.getString(R.string.order_time, t.getOrderTime()));
//		String state = t.getBusinessType();
//		if(StringUtils.isEmpty(state)){
			holder.state.setText(t.getStateName());
//		}else{
//			if(ORDER_STATE_ONE.equals(t.getBusinessType())){
//				holder.state.setText("待审核");
//			}else if(ORDER_STATE_TWO.equals(t.getBusinessType())){
//				holder.state.setText("派送中");
//			}else if(ORDER_STATE_THR.equals(t.getBusinessType())){
//				holder.state.setText("交易完成");
//			}
//		}
		holder.amount.setText(context.getString(R.string.order_total_amount,t.getTotalPrice()));
		holder.totalNum.setText(context.getString(R.string.order_total,t.getDishList().size()));
//		
	
		List<DishesOrderFinish> dishList = t.getDishList();
		if(dishList!=null){
			holder.dishList.removeAllViews();
			for (int i = 0; i < dishList.size(); i++) {
				LayoutInflater mInflater;
				mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View orderView = mInflater.inflate(R.layout.item_order_list_sec, null);
				ImageView v = (ImageView) orderView.findViewById(R.id.order_img);
				TextView tv = (TextView)orderView.findViewById(R.id.order_name);
				TextView amountView = (TextView)orderView.findViewById(R.id.order_amount_single);
				TextView numView = (TextView)orderView.findViewById(R.id.order_num);
				
				String url =  dishList.get(i).getImgUrl();
				v.setImageResource(R.drawable.defaule_img);
				SSYImageCache.getInstance().downloadImage(mImageMap, v,
						url, false, new ImageCallback() {
		
							@Override
							public void onImageLoaded(Bitmap bitmap, String imageUrl) {
								mImageMap.get(imageUrl).setImageBitmap(bitmap);
		
							}
						});
//				
				tv.setText(dishList.get(i).getName());
				amountView.setText(dishList.get(i).getPrice());
				numView.setText(context.getString(R.string.order_num,dishList.get(i).getNum()));
				holder.dishList.addView(orderView);
			}
		}
	}

	static class ViewHolder {
		TextView time;
		TextView state;
		TextView totalNum;
		TextView amount;
		LinearLayout dishList;
		
	}
	

}

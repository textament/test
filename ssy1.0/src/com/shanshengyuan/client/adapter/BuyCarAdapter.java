/**
 * 
 */
package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.dish.Dishes;

/**
 * @author lihao
 *
 */
public class BuyCarAdapter extends BaseAdapter {
	
	private List<Dishes> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

//	private int mMax;
	private OnClickListener onclick;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public BuyCarAdapter(Context context, List<Dishes> list,OnClickListener onclick) {
		this.onclick = onclick;
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<Dishes>();
		notifyDataSetChanged();
	}

	public void addListItems(List<Dishes> list) {

		int length = list.size();

		for (int i = 0; i < length; i++) {
			mList.add(list.get(i));

		}
		notifyDataSetChanged();
	}
	
	public void reflash(List<Dishes> list){
		
	}
	
	public void getNotifyDataSetChanged(Dishes d){
		
		List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{d.getId()},
				ClientDBHelper.getInstance(context));
		if(buyCarList.size()!=0){
			if(mList.size()!=0){
				for (int i = 0; i < mList.size(); i++) {
					if(mList.get(i).getId().equals(buyCarList.get(0).getShopId())){
						int num = Integer.parseInt(buyCarList.get(0).getShopNum());
						mList.get(i).setNum(num+"");
					}
				}
				notifyDataSetChanged();
			}
			
		}
		
	}
	
	public void updateNumItem(Dishes d){
		
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i).getId().equals(d.getId())){
				int num = Integer.parseInt(d.getNum())-1;
				mList.get(i).setNum(num+"");
			}
		}
		notifyDataSetChanged();

	}
	
	public void updateAddNumItem(Dishes d){
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i).getId().equals(d.getId())){
				int num = Integer.parseInt(d.getNum())+1;
				mList.get(i).setNum(num+"");
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();

	}

	public void removeItem(String id) {
		int i = 0;
		for (Dishes t : mList) {
			if (t.getId().equals(id)) {
				mList.remove(i);
				break;
			}
			i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public Dishes getItem(int position) {

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

			layout = mInflater.inflate(R.layout.item_buy_car, null);
			holder.picImg = (ImageView) layout.findViewById(R.id.buycar_img);
			holder.titleName = (TextView) layout.findViewById(R.id.buycar_name);
			holder.content = (TextView) layout.findViewById(R.id.buycar_content);
			holder.amount = (TextView) layout.findViewById(R.id.buycar_amount);
			holder.dishType = (TextView) layout.findViewById(R.id.buycar_type);
			holder.delView = (TextView)layout.findViewById(R.id.del_buy_car);
			holder.subView = (ImageView)layout.findViewById(R.id.num_subtract);
			holder.numView = (TextView)layout.findViewById(R.id.num_buy_car);
			holder.addView = (ImageView)layout.findViewById(R.id.num_add);
			holder.numLy = (LinearLayout)layout.findViewById(R.id.num_ly);
			holder.numTv = (TextView)layout.findViewById(R.id.num_tv);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final Dishes t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(Dishes t) {

		holder.titleName.setText(t.getName());
		holder.content.setText(t.getRemark());
		holder.amount.setText(t.getPrice());
		holder.dishType.setText(t.getDishTypeName());
		holder.picImg.setImageResource(R.drawable.defaule_img);
		holder.numView.setText(t.getNum());
		String url = t.getImgUrl();
		SSYImageCache.getInstance().downloadImage(mImageMap, holder.picImg,
				url, false, new ImageCallback() {

					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						mImageMap.get(imageUrl).setImageBitmap(bitmap);

					}
				});
		holder.subView.setOnClickListener(onclick);
		holder.subView.setId(99);
		holder.subView.setTag(t);
		
		holder.numLy.setOnClickListener(onclick);
		holder.numLy.setId(100);
		holder.numLy.setTag(t);
		
		holder.numTv.setOnClickListener(onclick);
		holder.numTv.setId(103);
		holder.numTv.setTag(t);
		
		holder.numView.setOnClickListener(onclick);
		holder.numView.setId(104);
		holder.numView.setTag(t);
		
		holder.addView.setFocusable(false);
		holder.addView.setOnClickListener(onclick);
		holder.addView.setId(101);
		holder.addView.setTag(t);
		
		holder.delView.setOnClickListener(onclick);
		holder.delView.setId(102);
		holder.delView.setTag(t);
	
		
	}

	static class ViewHolder {
		TextView titleName;
		ImageView picImg;
		TextView content;
		TextView amount;
		TextView dishType;
		TextView delView;
		ImageView subView;
		TextView numView;
		ImageView addView;
		LinearLayout numLy;
		TextView numTv;
	}

}

/**
 * 
 */
package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYFooderImageCache;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.utils.ConvertUtils;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author lihao
 *
 */
public class MainAdapter extends BaseAdapter {

	private List<Dishes> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;

	private OnClickListener onclick;
	
	private int num = 0;
//	private int mMax;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public MainAdapter(Context context, List<Dishes> list,OnClickListener onclick) {
		this.context = context;
		this.mList = list;
		this.onclick = onclick;
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

	@Override
	public int getCount() {

		return mList.size();

	}

	public void removeItem(String id) {
		int i = 0;
		for (Dishes t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View layout = convertView;

		if (convertView == null) {

			holder = new ViewHolder();

			layout = mInflater.inflate(R.layout.item_main_new, null);
			holder.picImg = (ImageView) layout.findViewById(R.id.main_img);
			holder.titleName = (TextView) layout.findViewById(R.id.main_name1);
			holder.content = (TextView) layout.findViewById(R.id.main_content1);
			holder.goShop = (LinearLayout) layout.findViewById(R.id.main_btn);
			holder.amount = (TextView) layout.findViewById(R.id.main_amount1);
			holder.dishType = (TextView) layout.findViewById(R.id.main_type1);
			holder.price = (TextView) layout.findViewById(R.id.main_price);
			holder.todayView = (TextView) layout.findViewById(R.id.textView1);
			holder.waiLy = (RelativeLayout)layout.findViewById(R.id.relativeLayout1);
			holder.neiLy = (RelativeLayout)layout.findViewById(R.id.relativeLayout2);
			holder.storeNumTv = (TextView)layout.findViewById(R.id.store_num);
			holder.carTv = (TextView)layout.findViewById(R.id.join_buy);
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

	private void updateData(final Dishes t) {
//		if(BaseApplication.WIDTH<=480){
//			holder.goShop.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					ConvertUtils.dip2px(context, 40),1));
//			holder.titleName.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					ConvertUtils.dip2px(context, 40),1));
//		}else{
//		
//			holder.goShop.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					ConvertUtils.dip2px(context, 40),0.6f));
//			holder.titleName.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					ConvertUtils.dip2px(context, 40),0.4f));
//		}
		System.out.println(BaseApplication.WIDTH);
		if(BaseApplication.WIDTH<=480){
//			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lp.setMargins(0, ConvertUtils.dip2px(context, 180), 0, 0);
//			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			holder.goShop.setWidth(ConvertUtils.dip2px(context, 100));
//			holder.goShop.setHeight(ConvertUtils.dip2px(context, 40));
//			holder.goShop.setLayoutParams(lp);
		}else if(BaseApplication.WIDTH>=1080){
//			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lp.setMargins(0, ConvertUtils.dip2px(context, 130), 0, 0);
//			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			holder.goShop.setWidth(ConvertUtils.dip2px(context, 100));
//			holder.goShop.setHeight(ConvertUtils.dip2px(context, 40));
//			holder.goShop.setLayoutParams(lp);
		}
		
	
	
		holder.titleName.setText(t.getName());
	//	holder.content.setText(t.getRemark());
		holder.amount.setText(t.getMarketPrice());
		holder.price.setText(t.getPrice());
	//	holder.dishType.setText(t.getDishTypeName());
	//    holder.picImg.setImageResource(R.drawable.defaule_img);
		String url = t.getImgUrl();
		if(!StringUtils.isEmpty(url)){
//			SSYImageCache.getInstance().downloadImage(mImageMap, holder.picImg,
//					url, false, new ImageCallback() {
//
//						@Override
//						public void onImageLoaded(Bitmap bitmap, String imageUrl) {
//							mImageMap.get(imageUrl).setImageBitmap(bitmap);
//
//						}
//					});
			DisplayImageOptions op = SSYFooderImageCache.getInstance().initOption();
			ImageLoader.getInstance().displayImage(url, holder.picImg, op);
			
		}
		holder.goShop.setOnClickListener(onclick);
		
		//通过库存字段进行不同展示
		if(t.getRepertory()==0){
			holder.storeNumTv.setVisibility(View.GONE);
			holder.waiLy.setBackgroundResource(0);
			holder.waiLy.setVisibility(View.VISIBLE);
			
			holder.neiLy.setBackgroundColor(context.getResources().getColor(R.color.white));
			holder.neiLy.setVisibility(View.VISIBLE);
			holder.todayView.setVisibility(View.GONE);
			holder.goShop.setBackgroundColor(context.getResources().getColor(R.color.jin_ri_ke_song_bg));
		}else{
			holder.carTv.setGravity(Gravity.CENTER);
			if(BaseApplication.WIDTH<=480){
				holder.storeNumTv.setGravity(Gravity.CENTER);
				holder.goShop.setGravity(Gravity.CENTER|Gravity.RIGHT);
				holder.carTv.setTextSize(16);
			}else if(BaseApplication.WIDTH>=1080){
				holder.carTv.setTextSize(18);
				holder.storeNumTv.setGravity(Gravity.CENTER);
			}else{
				holder.carTv.setTextSize(18);
				holder.storeNumTv.setGravity(Gravity.CENTER);
			}
			holder.todayView.setText("今日可送");
			
			holder.storeNumTv.setVisibility(View.VISIBLE);
			holder.storeNumTv.setText("限定:"+t.getRepertory()+"份");
			holder.waiLy.setBackgroundResource(R.drawable.home_1);
			holder.waiLy.setVisibility(View.VISIBLE);
			holder.neiLy.setBackgroundResource(0);
			holder.neiLy.setVisibility(View.VISIBLE);
			holder.goShop.setBackgroundResource(0);
			holder.todayView.setVisibility(View.VISIBLE);
		}
//		
//		holder.goShop.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				t.setX(arg1.getX()+"");
//				t.setY(arg1.getY()+"");
//				return false;
//			}
//		});
		holder.goShop.setTag(t);
		holder.goShop.setId(Integer.parseInt(t.getId()));
		//
	}

	static class ViewHolder {
		TextView titleName;
		ImageView picImg;
		TextView content;
		LinearLayout goShop;
		TextView amount;
		TextView price;
		TextView dishType;
		//新增属性
		TextView todayView;
		//底部内层背景
		RelativeLayout neiLy;
		//底部外层背景
		RelativeLayout waiLy;
		//库存数量
		TextView storeNumTv;
		//加入购物车
		TextView carTv;
	}

}

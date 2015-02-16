package com.shanshengyuan.client.adapter;

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
import com.shanshengyuan.client.activity.MealDetailActivity;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.model.meal.MealDish;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.utils.ClimbListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shanshengyuan.client.R.color;
import static com.shanshengyuan.client.R.id;
import static com.shanshengyuan.client.R.string;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MealDetailAdapter extends BaseAdapter {

    private List<MealDish> mList;
    private LayoutInflater mInflater;

    private Context context;
    private ViewHolder holder = null;

    private View.OnClickListener onclick;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    public MealDetailAdapter(Context context, List<MealDish> list) {
        this.context = context;
        this.mList = list;

        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mImageMap = new HashMap<String, ImageView>();
    }



    public void removeAllListData() {
        mList = new ArrayList<MealDish>();
        notifyDataSetChanged();
    }

    public void updateData(List<MealDish> list){

        notifyDataSetChanged();
    }

    public void addListItems(List<MealDish> list) {

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
        for (MealDish t : mList) {
            mList.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public MealDish getItem(int position) {
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

            layout = mInflater.inflate(R.layout.item_meal_detail, null);
            holder.mealImg = (ImageView)layout.findViewById(id.item_meal_img);
            holder.mealName = (TextView)layout.findViewById(id.item_meal_name);
            holder.mealnum = (TextView)layout.findViewById(id.meal_detail_num);
            layout.setTag(holder);

        } else {
            layout = convertView;
            holder = (ViewHolder) layout.getTag();
        }

        // set data
        final MealDish t = mList.get(position);
        updateData(t);
        return layout;
    }

    private void updateData(MealDish t) {

        holder.mealName.setText(t.getDishName());
        holder.mealnum.setText("数量:"+t.getDishNum());
        String url =  t.getDishImg();
        holder.mealImg.setImageResource(R.drawable.defaule_img);
        SSYImageCache.getInstance().downloadImage(mImageMap, holder.mealImg,
                url, false, new SSYImageCache.ImageCallback() {

                    @Override
                    public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                        mImageMap.get(imageUrl).setImageBitmap(bitmap);

                    }
                });
    }

    private

    static class ViewHolder {
     ImageView mealImg;
     TextView mealName;
     TextView mealnum;

    }
}

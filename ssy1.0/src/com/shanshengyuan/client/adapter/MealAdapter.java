package com.shanshengyuan.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYFooderImageCache;
import com.shanshengyuan.client.model.meal.Meal;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shanshengyuan.client.R.color;
import static com.shanshengyuan.client.R.id;
import static com.shanshengyuan.client.R.string;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MealAdapter extends BaseAdapter {

    private List<Meal> mList;
    private LayoutInflater mInflater;

    private Context context;
    private ViewHolder holder = null;

    private View.OnClickListener onclick;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    public MealAdapter(Context context, List<Meal> list) {
        this.context = context;
        this.mList = list;

        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mImageMap = new HashMap<String, ImageView>();
    }

    public void removeAllListData() {
        mList = new ArrayList<Meal>();
        notifyDataSetChanged();
    }

    public void updateData(List<Meal> list){

        notifyDataSetChanged();
    }

    public void addListItems(List<Meal> list) {

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
        for (Meal t : mList) {
            mList.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public Meal getItem(int position) {
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

            layout = mInflater.inflate(R.layout.item_meal_list, null);
            holder.imageView = (ImageView)layout.findViewById(id.meal_img);
            holder.mealName = (TextView)layout.findViewById(id.meal_name);
            holder.orgPriceTv = (TextView) layout.findViewById(id.meal_price);
            holder.mealPriceTv = (TextView) layout.findViewById(id.meal_amount);
            holder.remarkTv = (TextView) layout.findViewById(id.meal_content);
            layout.setTag(holder);

        } else {
            layout = convertView;
            holder = (ViewHolder) layout.getTag();
        }

        // set data
        final Meal t = mList.get(position);
        updateData(t);
        return layout;
    }

    private void updateData(Meal t) {
        holder.orgPriceTv.setText(t.getOriginalPrice()+"");
        holder.mealPriceTv.setText(t.getMealPrice()+"");
        holder.remarkTv.setText(t.getRemark());
        if(!StringUtils.isEmpty(t.getMealName()))
            holder.mealName.setText(t.getMealName());
    //    holder.mealName.setVisibility(View.GONE);
        if(!StringUtils.isEmpty(t.getImgUrl())){
            DisplayImageOptions op = SSYFooderImageCache.getInstance().initOption();
            ImageLoader.getInstance().displayImage(t.getImgUrl(), holder.imageView, op);
        }

    }

    private

    static class ViewHolder {
      ImageView imageView;
      TextView mealName;
      TextView remarkTv;
      TextView orgPriceTv;
      TextView mealPriceTv;
    }
}

package com.shanshengyuan.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.model.arrviedVolume.arrviedVolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/2/10 0010.
 */
public class ArrviedVolumeUseAdapter extends BaseAdapter {

    private List<arrviedVolume> mList;
    private LayoutInflater mInflater;

    private Context context;
    private ViewHolder holder = null;

    private View.OnClickListener onclick;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    public ArrviedVolumeUseAdapter(Context context, List<arrviedVolume> list, View.OnClickListener onclick) {
        this.context = context;
        this.mList = list;
        this.onclick = onclick;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mImageMap = new HashMap<String, ImageView>();
    }

    public void removeAllListData() {
        mList = new ArrayList<arrviedVolume>();
        notifyDataSetChanged();
    }

    public void updateData(List<arrviedVolume> list){
        mList = new ArrayList<arrviedVolume>();
        int length = list.size();

        for (int i = 0; i < length; i++) {
            mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void addListItems(List<arrviedVolume> list) {

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
        for (arrviedVolume t : mList) {
            mList.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public arrviedVolume getItem(int position) {
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

            layout = mInflater.inflate(R.layout.item_order_page_address_footer, null);

            holder.timeTv = (TextView) layout.findViewById(R.id.time_tv);
            holder.amountTv = (TextView)layout.findViewById(R.id.vol_amount);
            holder.stateImg = (ImageView)layout.findViewById(R.id.check_img);
            holder.checkLy = (LinearLayout)layout.findViewById(R.id.check_ly);
            layout.setTag(holder);

        } else {
            layout = convertView;
            holder = (ViewHolder) layout.getTag();
        }

        // set data
        final arrviedVolume t = mList.get(position);
        updateData(t);
        return layout;
    }

    private void updateData(arrviedVolume t) {

        holder.timeTv.setText("使用期限:"+t.getStartTime()+"~~"+t.getEndTime());
        holder.amountTv.setText(context.getString(R.string.how_yuan,t.getArriveUseTicketPrice()));
        if(t.getChecked()==0){
            holder.stateImg.setImageResource(R.drawable.eceiving_management_choice);
        }else if(t.getChecked()==1){
            holder.stateImg.setImageResource(R.drawable.eceiving_management_choice_s);
        }

        holder.checkLy.setTag(t);
        holder.checkLy.setId(t.getId());
        holder.checkLy.setOnClickListener(onclick);
    }

    private

    static class ViewHolder {
        TextView timeTv;
        TextView amountTv;
        ImageView stateImg;
        LinearLayout checkLy;
    }
}

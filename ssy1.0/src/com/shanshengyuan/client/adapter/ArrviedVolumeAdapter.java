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
import com.shanshengyuan.client.model.redpackage.MyRed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/2/10 0010.
 */
public class ArrviedVolumeAdapter extends BaseAdapter {

    private List<arrviedVolume> mList;
    private LayoutInflater mInflater;

    private Context context;
    private ViewHolder holder = null;

    private View.OnClickListener onclick;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    public ArrviedVolumeAdapter(Context context, List<arrviedVolume> list,View.OnClickListener onclick) {
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

            layout = mInflater.inflate(R.layout.item_arrived_volume, null);
            holder.bgLy = (LinearLayout)layout.findViewById(R.id.vol_bgcolor_ly);
            holder.amountTv = (TextView)layout.findViewById(R.id.arrived_volume_amount);
            holder.timeTv = (TextView) layout.findViewById(R.id.my_arrived_volume_time_tv);
            holder.ticketImg = (ImageView) layout.findViewById(R.id.vol_img);
            holder.remarkTv = (TextView) layout.findViewById(R.id.my_arrived_volume_remark_tv);
            holder.stateImg = (ImageView)layout.findViewById(R.id.state_img);
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
        holder.remarkTv.setText(context.getString(R.string.my_red_remark, t.getRemark()));
        holder.amountTv.setText(context.getString(R.string.how_yuan, t.getArriveUseTicketPrice()));
        //抵用卷逻辑
        /**
         * 1.判断失效state状态
         * 3.判断使用状态isUse
         */
        if(t.getState()==0){
            holder.ticketImg.setImageResource(R.drawable.volume_grad);
            holder.stateImg.setVisibility(View.VISIBLE);
            holder.stateImg.setImageResource(R.drawable.expired);
        }else if(t.getState()==1){
                    if(t.getIsUse()==0){
                        holder.bgLy.setBackgroundColor(context.getResources().getColor(R.color.volume_color_red));
                        holder.ticketImg.setImageResource(R.drawable.volume_red);
                        holder.stateImg.setVisibility(View.GONE);
                    }else if(t.getIsUse()==1){
                        holder.bgLy.setBackgroundColor(context.getResources().getColor(R.color.volume_color_grad));
                        holder.ticketImg.setImageResource(R.drawable.volume_grad);
                        holder.stateImg.setVisibility(View.VISIBLE);
                        holder.stateImg.setImageResource(R.drawable.use);
                    }
        }

    }

    private

    static class ViewHolder {
        LinearLayout bgLy;
        TextView amountTv;
        TextView timeTv;
        TextView remarkTv;
        ImageView ticketImg;
        ImageView stateImg;
    }
}

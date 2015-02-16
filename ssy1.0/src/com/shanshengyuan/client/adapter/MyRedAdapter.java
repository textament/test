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
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shanshengyuan.client.R.*;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MyRedAdapter extends BaseAdapter {

    private List<MyRed> mList;
    private LayoutInflater mInflater;

    private Context context;
    private ViewHolder holder = null;

    private View.OnClickListener onclick;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    public MyRedAdapter(Context context, List<MyRed> list,View.OnClickListener onclick) {
        this.context = context;
        this.mList = list;
        this.onclick = onclick;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mImageMap = new HashMap<String, ImageView>();
    }

    public void removeAllListData() {
        mList = new ArrayList<MyRed>();
        notifyDataSetChanged();
    }

    public void updateData(List<MyRed> list){

        notifyDataSetChanged();
    }

    public void addListItems(List<MyRed> list) {

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
        for (MyRed t : mList) {
            mList.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public MyRed getItem(int position) {
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

            layout = mInflater.inflate(R.layout.item_my_red, null);
            holder.stateColorLy = (LinearLayout)layout.findViewById(id.my_red_left_ly);
            holder.amountTv = (TextView)layout.findViewById(id.my_red_left_amount_tv);
            holder.codeTv = (TextView)layout.findViewById(id.my_red_code_tv);
            holder.timeOrStateTv = (TextView) layout.findViewById(id.my_red_use_time_or_state_tv);
            holder.addressTv = (TextView) layout.findViewById(id.my_red_use_address_tv);
            holder.remarkTv = (TextView) layout.findViewById(id.my_red_explanation_tv);
            layout.setTag(holder);

        } else {
            layout = convertView;
            holder = (ViewHolder) layout.getTag();
        }

        // set data
        final MyRed t = mList.get(position);
        updateData(t);
        return layout;
    }

    private void updateData(MyRed t) {
        holder.amountTv.setText(context.getString(string.how_yuan,t.getRedPacketPrice()));
        holder.addressTv.setText(context.getString(string.use_address, t.getAddress()));
        holder.remarkTv.setText(context.getString(string.my_red_remark, t.getRemark()));

        //红包逻辑
        /**
         * 1.判断红包state状态
         * 2.判断激活状态isActivate
         * 3.判断使用状态isUse
         */

        if(t.getState()==0){
                holder.codeTv.setVisibility(View.GONE);
                holder.timeOrStateTv.setVisibility(View.VISIBLE);
                holder.timeOrStateTv.setText("使用状态：已过期");
                holder.stateColorLy.setBackgroundColor(context.getResources().getColor(color.hongbao_color_gral));
        }else if(t.getState()==1){
             if(t.getIsActivate()==0){
                 holder.codeTv.setVisibility(View.GONE);
                 holder.timeOrStateTv.setVisibility(View.VISIBLE);
                 holder.timeOrStateTv.setText("使用状态：未激活");
                 holder.stateColorLy.setBackgroundColor(context.getResources().getColor(color.hongbao_color_gral));
             }else if(t.getIsActivate()==1){
                      if(t.getIsUse()==0){
                          holder.codeTv.setVisibility(View.VISIBLE);
                          holder.timeOrStateTv.setVisibility(View.VISIBLE);
                          holder.codeTv.setText(context.getString(string.duihuan_num, t.getCodeKey()));
                          holder.timeOrStateTv.setText("使用期限："+t.getStartTime()+"-"+t.getEndTime());
                          holder.stateColorLy.setBackgroundColor(context.getResources().getColor(color.hongbao_color));
                      }else if(t.getIsUse()==1){
                          holder.codeTv.setVisibility(View.GONE);
                          holder.timeOrStateTv.setVisibility(View.VISIBLE);
                          holder.timeOrStateTv.setText("使用状态：已使用");
                          holder.stateColorLy.setBackgroundColor(context.getResources().getColor(color.hongbao_color_gral));
                      }
             }
        }
    }

    private

    static class ViewHolder {
        LinearLayout stateColorLy;
        TextView amountTv;
        TextView codeTv;
        TextView timeOrStateTv;
        TextView addressTv;
        TextView remarkTv;

    }
}

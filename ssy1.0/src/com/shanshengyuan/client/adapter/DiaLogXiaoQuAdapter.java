package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.BuyCarAdapter.ViewHolder;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.utils.StringUtils;

public class DiaLogXiaoQuAdapter extends BaseAdapter {
	
	private List<String> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;
	

	public DiaLogXiaoQuAdapter(Context context, List<String> list) {
		this.context = context;
		this.mList = list;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void removeAllListData() {
		mList = new ArrayList<String>();
		notifyDataSetChanged();
	}
	
	public void updateData(List<StoreDishPoint> list){
	
		notifyDataSetChanged();
	}

	public void addListItems(List<String> list) {

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
		for (String t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public String getItem(int position) {

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

			layout = mInflater.inflate(R.layout.dialog_item, null);
			holder.idView = (TextView)layout.findViewById(R.id.address_id);
			holder.name = (TextView) layout.findViewById(R.id.address_dig_name);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final String t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(String t) {

		holder.name.setText(t);
		holder.name.setTag(t);
	}

	static class ViewHolder {
		TextView name;
		TextView idView;
		
	}
	
}

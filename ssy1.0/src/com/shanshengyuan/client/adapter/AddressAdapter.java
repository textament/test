package com.shanshengyuan.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * 
 * @author lihao
 *
 */
public class AddressAdapter extends BaseAdapter {
	
	private static final String ADDRESS_STATE_ONE = "0";
	private static final String ADDRESS_STATE_TWO = "1";

	private List<Address> mList;
	private LayoutInflater mInflater;

	private Context context;
	private ViewHolder holder = null;
	
	private OnClickListener onclick;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;

	public AddressAdapter(Context context, List<Address> list,OnClickListener onclick) {
		this.context = context;
		this.mList = list;
		this.onclick = onclick;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageMap = new HashMap<String, ImageView>();
	}

	public void removeAllListData() {
		mList = new ArrayList<Address>();
		notifyDataSetChanged();
	}
	
	public void updateData(List<Address> list){
	
		notifyDataSetChanged();
	}

	public void addListItems(List<Address> list) {

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
		for (Address t : mList) {
			//if (t.getTask_id().equals(id)) {
				mList.remove(i);
				//break;
			//}
			//i++;
		}
		notifyDataSetChanged();
	}

	@Override
	public Address getItem(int position) {

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

			layout = mInflater.inflate(R.layout.item_address_new, null);
			holder.addressEdit = (TextView)layout.findViewById(R.id.address_edit);
			holder.addressType = (TextView)layout.findViewById(R.id.address_type);
			holder.name = (TextView) layout.findViewById(R.id.address_name3);
			holder.phone = (TextView) layout.findViewById(R.id.address_phone3);
			holder.addressName = (TextView) layout.findViewById(R.id.address_content3);
		//	holder.pic = (ImageView) layout.findViewById(R.id.address_img);
			layout.setTag(holder);

		} else {
			layout = convertView;
			holder = (ViewHolder) layout.getTag();
		}

		// set data
		final Address t = mList.get(position);
		updateData(t);
		return layout;
	}
	
	private void updateData(Address t) {

		holder.name.setText(context.getString(R.string.address_username, t.getName()));
		
		holder.phone.setText(t.getPhone());
		holder.addressName.setText(t.getAddress());
		
		if(!StringUtils.isEmpty(t.getIsDefault())){
			if(ADDRESS_STATE_ONE.equals(t.getIsDefault())){
				holder.addressType.setBackgroundResource(R.drawable.settlement_label_grey);
			}else if(ADDRESS_STATE_TWO.equals(t.getIsDefault())){
				holder.addressType.setBackgroundResource(R.drawable.settlement_label_red);
			}
		}
		if(t.getType()==0){
			holder.addressType.setText("送货上门");
		}else if(t.getType()==1){
			holder.addressType.setText("去菜点自提");
		}
		
		holder.addressType.setId(Integer.parseInt(t.getId()));
		holder.addressType.setOnClickListener(onclick);
	}

	static class ViewHolder {
		TextView name;
		TextView phone;
		TextView addressName;
		TextView addressType;
		TextView addressEdit;
		//ImageView pic;
		
	}
	

}

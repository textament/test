/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.AddressAdapter;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.AddressEditRequest;
import com.shanshengyuan.client.model.address.AddressListRequest;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;

/**
 * @author lihao
 *
 */
public class AddressActivity extends BaseActivity implements OnResultListener,OnItemClickListener,UpLoadListener,OnClickListener{

	AddressActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView mAddressList;
	private AddressAdapter mAddressAdapter;
	
	List<Address> list;
	List<StoreDishPoint> pointList;
	
	
	ArrayList<StoreDishPoint> listObj = null;
	
	
	//新地址按钮
	private TextView addView;
	
	//地址列表请求\
	private AddressListRequest mAddressListRequest;
	private AddressController mAddressController;
	private AddressEditRequest mAddressEditRequest;
	
	private Address ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		if(mAddressController==null){
			mAddressController = new AddressController(self, self);
		}
		
		initView();
		initTopBar();
		initData();
		//updateUIData();
		setListener();
	}
	
	private void setListener(){
		addView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, AddressEditActivity.class);
				Bundle bundle = new Bundle();
				intent.putExtra("pointList", (Serializable) listObj);
			//	bundle.putParcelableArrayList("pointList", listObj);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		if(BaseApplication.isAddressFlash){
			initData();
		}
		super.onResume();
	}
	
	private void initData(){
//		list = new ArrayList<Address>();
//	
//		for (int i = 0; i < 10; i++) {
//			Address a = new Address();
//			a.setAddress("地址"+i);
//			a.setIsDefault("0");
//			a.setName("取菜人"+i);
//			a.setPhone("153123456789");
//			list.add(a);
//		}
		mAddressListRequest = new AddressListRequest();
		mAddressListRequest.setDk();
		mAddressController.execute(Actions.ACTION_ADDRESS_LIST, mAddressListRequest);
	}
	
	private void updateUIData(){
		list = mAddressController.getmAddressList();

        pointList = mAddressController.getPonitList();


        listObj = new ArrayList<StoreDishPoint>();

        if(pointList!=null){
            for (int i = 0; i < pointList.size(); i++) {
                StoreDishPoint s = new StoreDishPoint();
                s.setStoreId(pointList.get(i).getStoreId());
                s.setAddress(pointList.get(i).getAddress());
                s.setPhone(pointList.get(i).getPhone());
                s.setPointXY(pointList.get(i).getPointXY());
                listObj.add(s);
            }
        }



        if(list!=null){
            if (list.size() == 0) {
                mAddressList.noDataFinishNoScroll();
                mAddressList.setVisibility(View.GONE);
                //	mNoReleaseLayout.setVisibility(View.VISIBLE);
                return;
            } else if (list.size() < 1000) {
                mAddressList.setVisibility(View.VISIBLE);
                //	mNoReleaseLayout.setVisibility(View.GONE);
                mAddressList.setHiddenFooterViewForScroll();

                com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
            } else {
                mAddressList.setVisibility(View.VISIBLE);
                //	mNoReleaseLayout.setVisibility(View.GONE);
                mAddressList.loadedFinish();
                mAddressList.isFirst = true;
            }
            if (mAddressList.getAdapter() == null) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                mAddressAdapter = new AddressAdapter(self, list,this);
                mAddressList.setAdapter(mAddressAdapter);
            } else {
                mAddressAdapter = (AddressAdapter) mAddressList.getAdapter();
                mAddressAdapter.removeAllListData();
                mAddressAdapter.addListItems(list);

            }
        }
		
	}
	
	private void initView(){
		mAddressList = (ClimbListView)findViewById(R.id.address_list);
		mAddressList.initFooterView();
		mAddressList.setUpLoadListener(this);
		mAddressList.setOnItemClickListener(this);
		
		addView = (TextView)findViewById(R.id.add_addresas);
		pointList = new ArrayList<StoreDishPoint>();
		
	}
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ADDRESS_LIST:
			
			updateUIData();
			break;
		case Actions.ACTION_ADDRESS_UPDATE:
			BaseApplication.isAddressFlash = true;
			Toast.makeText(self, "更改默认地址成功！", Toast.LENGTH_SHORT).show();
			if(mAddressAdapter!=null){
				mAddressAdapter = (AddressAdapter) mAddressList.getAdapter();
				for (int i = 0; i < list.size(); i++) {
					if(Integer.parseInt(ad.getId())==Integer.parseInt(list.get(i).getId())){
						list.get(i).setIsDefault("1");
					}else{
						list.get(i).setIsDefault("0");
					}
				}
				
				mAddressAdapter.updateData(list);
			}
			

			break;
		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		super.onFailed(actionType, result);
	}

	@Override
	public void scrollTopAction() {
	}

	@Override
	public void scrollBottomAction() {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Address t = mAddressAdapter.getItem(position-1);
		Intent intent = new Intent();
		intent.setClass(self, AddressEditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("address", t);
		intent.putExtra("pointList", (Serializable) listObj);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				if(v.getId()==Integer.parseInt(list.get(i).getId())){
					mAddressEditRequest = new AddressEditRequest();
					mAddressEditRequest.setId(Integer.parseInt(list.get(i).getId()));
					mAddressEditRequest.setIsDefault("1");
					mAddressEditRequest.setDk();
					mAddressController.execute(Actions.ACTION_ADDRESS_UPDATE, mAddressEditRequest);
					
					ad = list.get(i);
					break;
				}
			}
		}
	}
}

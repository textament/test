/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.DiaLogAdapter;
import com.shanshengyuan.client.adapter.DiaLogStreetAdapter;
import com.shanshengyuan.client.adapter.DiaLogXiaoQuAdapter;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.AddressAddRequest;
import com.shanshengyuan.client.model.address.AddressEditRequest;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author LIHAO
 *
 */
public class AddressEditActivity extends BaseActivity implements
		OnResultListener,OnClickListener,OnItemClickListener {
	AddressEditActivity self = null;
	private static final int MAP_DATA = 1;

	View mBack;
	
	//编辑
	private EditText userTv;
	private EditText callTv;
	//private EditText addressTv;
	
	//地址tab切换
	private TextView gohomeTv;
	private TextView goStoreTv;
	
	//地址添加
	private LinearLayout storeLy;
	private LinearLayout homeLy;
	private TextView addressEd;
	private TextView mapImg;
	private TextView xiaoquEd;
	private TextView streetEd;
	private EditText xiangxiEd;
	
	//确认
	private Button sureBtn;
	
	Bundle b = null;
	Address address =null;
	
	StoreDishPoint sdp = null;
	//编辑地址请求
	private AddressAddRequest mAddressAddRequest;
	private AddressController mAddressController;
	private AddressEditRequest mAddressEditRequest;
	
	ArrayList<StoreDishPoint> listObj = null;
	
	List<StoreDishPoint> list = null;
	
	private AlertDialog dialog;
	
	ListView mlistView = null;
	DiaLogAdapter adapter = null;
	
	List<String> xiaoquList;
	List<String> streetList;
	private ListView xiaoquView;
	private ListView streetListView;
	private AlertDialog xiaoqudialog;
	private AlertDialog streetdialog;
	DiaLogXiaoQuAdapter xiaoquadapter = null;
	DiaLogStreetAdapter streetadapter = null;
	
	//选择页签
	private int choosePage = 1;
	
	private int storeId ;
	//
	private int type = -1;
	
	private LinearLayout editShowLy;
	

	
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		  self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_edit_new);
		b = getIntent().getExtras();
		if(b!=null){
			address = (Address) b.getSerializable("address");
			if(address!=null)
			//type =address.getType();
				type = 1;
		}
		listObj =  (ArrayList<StoreDishPoint>) getIntent().getSerializableExtra("pointList"); 
		list = new ArrayList<StoreDishPoint>();
		if(listObj!=null){
			for (int i = 0; i < listObj.size(); i++) {
				 StoreDishPoint s = new StoreDishPoint();
				 s.setStoreId(listObj.get(i).getStoreId());
				 s.setAddress(listObj.get(i).getAddress());
				 s.setPhone(listObj.get(i).getPhone());
				 s.setPointXY(listObj.get(i).getPointXY());
				 list.add(s);
			}
		}
		
		
		if(mAddressController==null){
			mAddressController = new AddressController(self, self);
		}
		initTopBar();
		getData();
		initView();
		initData();
		setListener();
	}
	  
	  private void setListener(){
		  streetEd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				streetdialog = new AlertDialog.Builder(self).create();
				View streetview = View.inflate(self, R.layout.dialog_street_list, null);
				streetdialog.setView(streetview, 0, 0, 0, 0);
				streetdialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				streetListView = (ListView) streetview.findViewById(R.id.listview_street);
				streetadapter = new DiaLogStreetAdapter(self, streetList);
				streetListView.setAdapter(streetadapter);
				streetListView.setOnItemClickListener(new OnItemClickListener()
		        {

		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                    int position, long id)
		            {
		            	String t = (String) streetListView.getAdapter().getItem(position);
		            	if("请选择".equals(t)){
		            		streetEd.setText(null);
		            	}else{
		            		streetEd.setText(t);
		            	}
		            	streetdialog.hide();
		            }
		            
		        });
				
				streetdialog.show();
			}
		});
		  
		  xiaoquEd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					xiaoqudialog.show();
			}
		});
		  
		  
		  addressEd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		  
		  sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(vilidate()){
					if(address!=null){
						if(StringUtils.isEmpty(address.getId())){
							newAddress();
						}else{
							editAddress();
						}
					}else{
						newAddress();
					}
				}
			}
		});
		  
		  gohomeTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				storeLy.setVisibility(View.GONE);
				homeLy.setVisibility(View.VISIBLE);
				xiangxiEd.setVisibility(View.VISIBLE);
				gohomeTv.setBackgroundResource(R.drawable.settlement_door_to_door_delivery_s);
				goStoreTv.setBackgroundResource(R.drawable.settlement_since_the_mention_s);
				choosePage =0;
				
			}
		});
		  
		  goStoreTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				storeLy.setVisibility(View.VISIBLE);
				homeLy.setVisibility(View.GONE);
				xiangxiEd.setVisibility(View.GONE);
				gohomeTv.setBackgroundResource(R.drawable.settlement_door_to_door_delivery);
				goStoreTv.setBackgroundResource(R.drawable.settlement_since_the_mention);
				choosePage = 1;
			}
		});
		  
		  mapImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(self, MapActivity.class);
				Bundle bundle = new Bundle();
				intent.putExtra("pointList", (Serializable) listObj);
			//	bundle.putParcelableArrayList("pointList", listObj);
				intent.putExtras(bundle);
				startActivityForResult(intent, MAP_DATA);
			}
		});
	  }
	  
	  private void editAddress(){
		  mAddressEditRequest = new AddressEditRequest();
		  mAddressEditRequest.setId(Integer.parseInt(address.getId()));
		  mAddressEditRequest.setName(userTv.getText().toString());
		  mAddressEditRequest.setPhone(callTv.getText().toString());
		  if(choosePage==0){
			  mAddressEditRequest.setType(choosePage);
			  String xiaoqu = xiaoquEd.getText().toString();
			  String street = streetEd.getText().toString();
			  String xiangxi = xiangxiEd.getText().toString();
			  mAddressEditRequest.setAddress(xiaoqu+street+xiangxi);
		  }else if(choosePage==1){
			  mAddressEditRequest.setType(choosePage);
			  String addstr = addressEd.getText().toString();
			  mAddressEditRequest.setAddress(addstr);
			  mAddressEditRequest.setStoreId(storeId);
		  }
		  mAddressEditRequest.setDk();
		  mAddressController.execute(Actions.ACTION_ADDRESS_UPDATE, mAddressEditRequest);
	  }
	  
	  private void newAddress(){
		  mAddressAddRequest = new AddressAddRequest();
		  mAddressAddRequest.setName(userTv.getText().toString());
		  mAddressAddRequest.setPhone(callTv.getText().toString());
		  
		  if(choosePage==0){
			  mAddressAddRequest.setType(choosePage);
			  String xiaoqu = xiaoquEd.getText().toString();
			  String street = streetEd.getText().toString();
			  String xiangxi = xiangxiEd.getText().toString();
			  mAddressAddRequest.setAddress(xiaoqu+street+xiangxi);
		  }else if(choosePage==1){
			  mAddressAddRequest.setType(choosePage);
			  String addstr = addressEd.getText().toString();
			  mAddressAddRequest.setAddress(addstr);
			  mAddressAddRequest.setStoreId(storeId);
		  }
		  mAddressAddRequest.setDk();
		  mAddressController.execute(Actions.ACTION_ADDRESS_ADD, mAddressAddRequest);
	  }
	  
	  private void initView(){
		  userTv = (EditText)findViewById(R.id.username_et);
		  callTv = (EditText)findViewById(R.id.phone_et);
		 // addressTv = (EditText)findViewById(R.id.address_et);
		  sureBtn = (Button)findViewById(R.id.sure);
		  editShowLy = (LinearLayout)findViewById(R.id.address_ed_show);
		  gohomeTv = (TextView)findViewById(R.id.type_2);
		  goStoreTv = (TextView)findViewById(R.id.type_1);
		  storeLy = (LinearLayout)findViewById(R.id.go_type_1);
		  homeLy = (LinearLayout)findViewById(R.id.go_type_2);
		  addressEd = (TextView)findViewById(R.id.address_et1);
		  mapImg = (TextView)findViewById(R.id.go_map);
		  xiaoquEd = (TextView)findViewById(R.id.address_xiaoqu);
		  streetEd = (TextView)findViewById(R.id.address_street);
		  xiangxiEd = (EditText)findViewById(R.id.address_xiangxi);
		  
		  
			dialog = new AlertDialog.Builder(self).create();
			View view = View.inflate(self, R.layout.dialog_list, null);
			dialog.setView(view, 0, 0, 0, 0);
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			mlistView = (ListView) view.findViewById(R.id.listview);
			adapter = new DiaLogAdapter(self,list);
			// adapter.removeAllListData();
			 mlistView.setAdapter(adapter);
			 mlistView.setOnItemClickListener(self);
			 
		    xiaoqudialog = new AlertDialog.Builder(self).create();
			View views = View.inflate(self, R.layout.dialog_xiaoqu_list, null);
			xiaoqudialog.setView(views, 0, 0, 0, 0);
			xiaoqudialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			xiaoquView = (ListView) views.findViewById(R.id.listview_xiaoqu);
			xiaoquadapter = new DiaLogXiaoQuAdapter(self, xiaoquList);
			xiaoquView.setAdapter(xiaoquadapter);
			xiaoquView.setOnItemClickListener(new OnItemClickListener()
	        {

	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id)
	            {
	            	String t = (String) xiaoquView.getAdapter().getItem(position);
	            	if("请选择".equals(t)){
	            		streetList = new ArrayList<String>();
	            		streetList.add("请选择");
	            		xiaoquEd.setText(null);
	            		streetEd.setText(null);
	            	}else if("鲁能新城".equals(t)){
	            		streetList = new ArrayList<String>();
	            		streetList.add("鲁能新城第一街区");
	            		streetList.add("鲁能新城第二街区");
	            		streetList.add("鲁能新城第三街区");
	            		streetList.add("鲁能新城第四街区");
	            		streetList.add("鲁能新城第五街区");
	            		streetList.add("鲁能新城第六街区");
	            		streetList.add("鲁能新城第七街区");
	            		streetList.add("鲁能新城第八街区");
	            		streetList.add("鲁能新城第九街区");
	            		streetList.add("鲁能新城第十街区");
	            		streetList.add("鲁能新城第十一街区");
	            		streetList.add("鲁能新城第十二街区");
	            		xiaoquEd.setText(t);
	            		streetEd.setText(null);
	            	}else if("其他小区".equals(t)){
	            		streetList = new ArrayList<String>();
	            		streetList.add("其他小区");
	            		xiaoquEd.setText(t);
	            		streetEd.setText(null);
	            	}
	            	
	            	xiaoqudialog.hide();
	            }
	            
	        });
			
			
		 if(type==1){
			 editShowLy.setVisibility(View.GONE);
		 }else if(type==0){
			 editShowLy.setVisibility(View.GONE);
			 	storeLy.setVisibility(View.GONE);
				homeLy.setVisibility(View.VISIBLE);
				xiangxiEd.setVisibility(View.VISIBLE);
				gohomeTv.setBackgroundResource(R.drawable.settlement_door_to_door_delivery_s);
				goStoreTv.setBackgroundResource(R.drawable.settlement_since_the_mention_s);
				choosePage =0;
		 }else{
			 editShowLy.setVisibility(View.VISIBLE);
		 }
	  }
	  
	  private void initData(){
		  if(address!=null){
			  userTv.setText(address.getName());
			  callTv.setText(address.getPhone());
			  if(address.getType()==0){
				  xiangxiEd.setText(address.getAddress());
			  }else if(address.getType()==1){
				  addressEd.setText(address.getAddress());
			  }
			 
		  }
	  }
	  
	  private List<String> getData(){
	         
			  xiaoquList = new ArrayList<String>();
			  xiaoquList.add("请选择");
			  xiaoquList.add("鲁能新城");
			  xiaoquList.add("其他小区");
	         
	        return xiaoquList;
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
	  
		private boolean vilidate(){
			boolean result = true;
			//验证姓名
			String user = userTv.getText().toString();
			if(StringUtils.isEmpty(user)){
				Toast.makeText(self, "请输入收货人姓名！", Toast.LENGTH_LONG).show();
				result = false;
				return result;
			}
			
			//电话
			String sUserPhone = callTv.getText().toString();

			if (StringUtils.isEmpty(sUserPhone)) {
				Toast.makeText(self, "请输入收货人手机号码！",
						Toast.LENGTH_LONG).show();
				result = false;
				return result;
			}

			if (!StringUtils.isPhoneNumber(sUserPhone)) {
				Toast.makeText(self, "请输入正确的手机号码！",
						Toast.LENGTH_LONG).show();
				result = false;
				return result;
			}
			
			//地址
			String userAddress = "";
			
			 if(choosePage==0){
				  String xiaoqu = xiaoquEd.getText().toString();
				  String street = streetEd.getText().toString();
				  String xiangxi = xiangxiEd.getText().toString();
				  userAddress = xiaoqu+street+xiangxi;
			  }else if(choosePage==1){
				  String addstr = addressEd.getText().toString();
				  userAddress = addstr;
			  }
			
			if(StringUtils.isEmpty(userAddress)){
				Toast.makeText(self, "请输入取菜地址！", Toast.LENGTH_LONG).show();
				result = false;
				return result;
			}
			
			return result;
		}
	  
	  @Override
	public void onSuccess(int actionType) {
		  switch (actionType) {
		case Actions.ACTION_ADDRESS_ADD:
			BaseApplication.isAddressFlash = true;
			Toast.makeText(self, "新增地址成功！", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case Actions.ACTION_ADDRESS_UPDATE:
			BaseApplication.isAddressFlash = true;
			Toast.makeText(self, "编辑地址成功！", Toast.LENGTH_SHORT).show();
			finish();
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
	public void onClick(View v) {
		dialog.hide();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == MAP_DATA) {
			if (data != null) {
				String adstr = data.getExtras().getString("address");
				storeId = data.getExtras().getInt("id");
				addressEd.setText(adstr);
			}
		}
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StoreDishPoint t = adapter.getItem(position);
		sdp = t;
		storeId = t.getStoreId();
		addressEd.setText(t.getAddress());
		dialog.hide();
	}
}

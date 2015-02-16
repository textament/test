/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.model.map.StoreMapInfo;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author lihao
 *
 */
public class MapActivity extends BaseActivity implements OnResultListener {
	
	MapView mMapView = null;
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		BitmapDescriptor mCurrentMarker;

		BaiduMap mBaiduMap=null;
		boolean  isFirstLoc = true;
	    Marker mMarker = null;
		private InfoWindow mInfoWindow;
		private LayoutInflater mInflater;
		private TextView mSureTv;
		View mBack;
		
		ArrayList<StoreDishPoint> listObj = null;
		
		
		List<StoreMapInfo> list = null;
		
		List<Marker> mList = null;
		
		Marker m = null;
		//106.565121,29.604705 鲁能星城 ，106.568317,29.602968 七区，106.571629,29.603031 八区，106.566448,29.600401 9区
		
		String address;
		String phone;
		int stId;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	        setContentView(R.layout.activity_baidu_map);  
	        
	    	listObj =  (ArrayList<StoreDishPoint>) getIntent().getSerializableExtra("pointList"); 
			
	        
	    	mInflater = (LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        //获取地图控件引用  
	        mMapView = (MapView) findViewById(R.id.bmapView);
	        mBaiduMap = mMapView.getMap();
	        initTopBar();
	        initData();
//	      //卫星地图  
//	        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
	      /// 开启定位图层  
	        mBaiduMap.setMyLocationEnabled(true);  
	        mCurrentMarker= BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);  
	    	mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
	    			LocationMode.NORMAL, true, mCurrentMarker));
	    	//设置初始化缩放大小
	    	MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
			mBaiduMap.setMapStatus(msu);
			
//	    	//定义Maker坐标点  
//			LatLng point = new LatLng(29.564013, 106.584052);
//	    	//构建Marker图标  
//	    	BitmapDescriptor bitmap = BitmapDescriptorFactory  
//	    	    .fromResource(R.drawable.icon_gcoding);  
//	    	//构建MarkerOption，用于在地图上添加Marker  
//	    	OverlayOptions options = new MarkerOptions().position(point).icon(bitmap);  
//	    	//在地图上添加Marker，并显示  
//	    	mMarker = (Marker)(mBaiduMap.addOverlay(options));
	    	
	    	initPoint();
	    	
	    	mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				public boolean onMarkerClick(final Marker marker) {
						
						for (int j = 0; j < mList.size(); j++) {
							
							if (marker == mList.get(j)) {
								BitmapDescriptor bd = BitmapDescriptorFactory
										.fromResource(R.drawable.map_s);
								m = marker;
								marker.setIcon(bd);
								
								LinearLayout v  = new LinearLayout(getApplicationContext());
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								v.setLayoutParams(lp);
								View vv =  mInflater.inflate(R.layout.item_map, null);
								
								ImageView img = (ImageView) vv.findViewById(R.id.close_view);
								TextView tv = (TextView)vv.findViewById(R.id.store_name);
								TextView num = (TextView)vv.findViewById(R.id.store_phone);
								address = list.get(j).getAddress();
								phone = list.get(j).getPhone();
								stId = Integer.parseInt(list.get(j).getStoreId());
								tv.setText(list.get(j).getAddress());
								num.setText(list.get(j).getPhone());
								v.addView(vv);
//								img.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										mBaiduMap.hideInfoWindow();
//									}
//								});
								
								LatLng ll = marker.getPosition();
								mInfoWindow = new InfoWindow(v, ll, -85);
								mBaiduMap.showInfoWindow(mInfoWindow);
							}else{
								BitmapDescriptor bds = BitmapDescriptorFactory
										.fromResource(R.drawable.map);
								mList.get(j).setIcon(bds);
							}
						}
//					mInflater = (LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					LinearLayout v  = new LinearLayout(getApplicationContext());
//					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//					v.setLayoutParams(lp);
//					View vv =  mInflater.inflate(R.layout.item_order_list_sec, null);
//					
//					ImageView img = (ImageView) vv.findViewById(R.id.close_view);
//					v.addView(vv);
//					if (marker == mMarker) {
//						img.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								mBaiduMap.hideInfoWindow();
//							}
//						});
//						
//						v.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								showToast("dianjile");
//							}
//						});
//						LatLng ll = marker.getPosition();
//						mInfoWindow = new InfoWindow(v, ll, -47);
//						mBaiduMap.showInfoWindow(mInfoWindow);
//					}
					return true;
				}
			});
	    	
	    	mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					return false;
				}
				
				@Override
				public void onMapClick(LatLng arg0) {
				
					mBaiduMap.hideInfoWindow();
					for (int j = 0; j < mList.size(); j++) {
						
						if (m == mList.get(j)) {
							BitmapDescriptor bd = BitmapDescriptorFactory
									.fromResource(R.drawable.map);
							
							m.setIcon(bd);
							break;
						}
					}
					m = null;
					address = "";
					phone = "";
				}
			});
	    	
			// 定位初始化
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
//			
	}
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
		mSureTv = (TextView)findViewById(R.id.address_sure);
		mSureTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtils.isEmpty(address)){
					showToast("请选择取菜点");
				}else{
					Intent ine = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("address", address);
					bundle.putInt("id", stId);
					ine.putExtras(bundle);
					setResult(1, ine);
					finish();
				}
			}
		});
	}
	
	private void initPoint(){
		mList = new ArrayList<Marker>();
		for (int i = 0; i < list.size(); i++) {
			//定义Maker坐标点  
			if(!StringUtils.isEmpty(list.get(i).getPointXY())){
				String points[] = list.get(i).getPointXY().split(",");
				LatLng point = new LatLng(Double.parseDouble(points[1]),Double.parseDouble(points[0]));
		    	//构建Marker图标  
		    	BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    	    .fromResource(R.drawable.map);  
		    	//构建MarkerOption，用于在地图上添加Marker  
		    	OverlayOptions options = new MarkerOptions().position(point).icon(bitmap);  
		    	//在地图上添加Marker，并显示  
		    	Marker	mMarker = (Marker)(mBaiduMap.addOverlay(options));
		    	mList.add(mMarker);
			}
			
		}
//		//定义Maker坐标点  
//		LatLng point = new LatLng(29.564013, 106.584052);
//    	//构建Marker图标  
//    	BitmapDescriptor bitmap = BitmapDescriptorFactory  
//    	    .fromResource(R.drawable.icon_gcoding);  
//    	//构建MarkerOption，用于在地图上添加Marker  
//    	OverlayOptions options = new MarkerOptions().position(point).icon(bitmap);  
//    	//在地图上添加Marker，并显示  
//    	mMarker = (Marker)(mBaiduMap.addOverlay(options));
	}
	
	private void initData(){
		list = new ArrayList<StoreMapInfo>();
		if(listObj!=null){
			for (int i = 0; i < listObj.size(); i++) {
				StoreMapInfo s = new StoreMapInfo();
				 s.setStoreId(listObj.get(i).getStoreId()+"");
				 s.setAddress(listObj.get(i).getAddress());
				 s.setPhone(listObj.get(i).getPhone());
				 s.setPointXY(listObj.get(i).getPointXY());
				 list.add(s);
			}
		}
		
//			StoreMapInfo s = new StoreMapInfo();
//			s.setStoreId("1");
//			s.setPhone("15312345678");
//			s.setAddress("鲁能星城");
//			s.setPointXY("106.565121,29.604705");
//			
//			StoreMapInfo ss = new StoreMapInfo();
//			ss.setStoreId("2");
//			ss.setPhone("15312345678");
//			ss.setAddress("鲁能星城2222222222222222七区");
//			ss.setPointXY("106.568317,29.602968");
//			
//			StoreMapInfo sss = new StoreMapInfo();
//			sss.setStoreId("3");
//			sss.setPhone("15312345678");
//			sss.setAddress("鲁能星城1111111111111111111111111八区");
//			sss.setPointXY("106.571629,29.603031");
//			
//			StoreMapInfo ssss = new StoreMapInfo();
//			ssss.setStoreId("4");
//			ssss.setPhone("15312345678");
//			ssss.setAddress("鲁能星城asdasdasdsadasdasdasdas九区");
//			ssss.setPointXY("106.566448,29.600401");
//			
//			list.add(s);
//			list.add(ss);
//			list.add(sss);
//			list.add(ssss);
			
	}
	
//	/**
//	 * 定位SDK监听函数
//	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(0).latitude(29.604755)
					.longitude(106.566366).build();
			
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				//106.584052,29.564013   106.566366,29.604755
				LatLng ll = new LatLng(29.604755,
						106.566366);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	@Override
	public void onSuccess(int actionType) {
		// TODO Auto-generated method stub
		super.onSuccess(actionType);
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		super.onFailed(actionType, result);
	}
}

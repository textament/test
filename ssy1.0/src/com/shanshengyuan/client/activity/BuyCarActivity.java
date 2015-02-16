package com.shanshengyuan.client.activity;

import java.text.DecimalFormat;
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
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.BuyCarAdapter;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.statistics.DishStatisticsRequest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;


/**
 * 
 * @author lihao
 *
 */
public class BuyCarActivity extends BaseActivity implements OnResultListener ,UpLoadListener,OnItemClickListener,OnClickListener{

	BuyCarActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView mbuycarList;
	private BuyCarAdapter mbuycarAdapter;
	
	List<Dishes> list;
	
	private TextView buyResultView;
	
	//总价
	private TextView buyTotal;
	
	private float totalAmount = 0;
	
	Dishes dd;
	
	//菜品点击统计
	private DishStatisticsRequest mDishStatisticsRequest;
	
	private DishController mDishController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		BaseApplication.mInstance.addActivity(this);
		setContentView(R.layout.activity_buy_car);
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		initView();
		initTopBar();
		initData();
		updateUIData();
		setListener();
	}
	
	
	private void setListener(){
		buyResultView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				List<BuyCar> buyCarList = BuyCar.getBuyCar(null, null,
						ClientDBHelper.getInstance(self));
				if(buyCarList!=null){
					if(buyCarList.size()!=0){
						Intent intent = new Intent();
						intent.setClass(self, OrderPageActivity.class);
						startActivity(intent);
					}else{
						showToast("你还没有选择菜哦");
					}
				}else{
					showToast("你还没有选择菜哦");

				}
				
			}
		});
	}
	
	private void initData(){
		list = new ArrayList<Dishes>();
		List<BuyCar> buyCarList = BuyCar.getBuyCar(null, null,
				ClientDBHelper.getInstance(this));
		if(buyCarList!=null){
			if(buyCarList.size()!=0){
				for (int i = 0; i < buyCarList.size(); i++) {
					float total = Float.parseFloat(buyCarList.get(i).getShopPrice())*Integer.parseInt(buyCarList.get(i).getShopNum());
					totalAmount = totalAmount+total;
				}
			}
		}
		DecimalFormat fnum = new DecimalFormat("##0.0"); 
		buyTotal.setText("￥"+fnum.format(totalAmount));
		for (int i = 0; i < buyCarList.size(); i++) {
			BuyCar b = buyCarList.get(i);
			Dishes d = new Dishes();
			d.setDishTypeName(b.getShopDishType());
			d.setId(b.getShopId());
			d.setImgUrl(b.getShopImg());
			d.setName(b.getShopName());
			d.setNum(b.getShopNum());
			d.setPrice(b.getShopPrice());
			d.setRemark(b.getShopContent());
			list.add(d);
		}
		
	}
	
	@Override
	protected void onResume() {
		totalAmount = 0;
		List<BuyCar> buyCarList = BuyCar.getBuyCar(null, null,
				ClientDBHelper.getInstance(this));
		if(buyCarList!=null){
			if(buyCarList.size()!=0){
				for (int i = 0; i < buyCarList.size(); i++) {
					float total = Float.parseFloat(buyCarList.get(i).getShopPrice())*Integer.parseInt(buyCarList.get(i).getShopNum());
					totalAmount = totalAmount+total;
				}
			}
		}
		DecimalFormat fnum = new DecimalFormat("##0.0"); 
		buyTotal.setText("￥"+fnum.format(totalAmount));
		if(mbuycarAdapter!=null){
			if(dd!=null){
				mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();
				mbuycarAdapter.getNotifyDataSetChanged(dd);
			}
		
		}
		
		if(totalAmount==0){
			if(mbuycarAdapter!=null){
				mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();
				mbuycarAdapter.removeAllListData();
			}
		}
		
		super.onResume();
	}
	
	private float totalPrice(){
		float totalAmount = 0;
		List<BuyCar> buyCarList = BuyCar.getBuyCar(null, null,
				ClientDBHelper.getInstance(this));
		if(buyCarList!=null){
			if(buyCarList.size()!=0){
				for (int i = 0; i < buyCarList.size(); i++) {
					float total = Float.parseFloat(buyCarList.get(i).getShopPrice())*Integer.parseInt(buyCarList.get(i).getShopNum());
					totalAmount = totalAmount+total;
				}
			}
		}
		
		DecimalFormat fnum = new DecimalFormat("##0.0"); 
		return Float.parseFloat(fnum.format(totalAmount));
	}
	
	private void updateUIData(){
		if (list.size() == 0) {
			mbuycarList.noDataFinishNoScroll();
			mbuycarList.setVisibility(View.GONE);
		//	mNoReleaseLayout.setVisibility(View.VISIBLE);
			return;
		} else if (list.size() < 1000) {
			mbuycarList.setVisibility(View.VISIBLE);
		//	mNoReleaseLayout.setVisibility(View.GONE);
			mbuycarList.setHiddenFooterViewForScroll();

			com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
		} else {
			mbuycarList.setVisibility(View.VISIBLE);
		//	mNoReleaseLayout.setVisibility(View.GONE);
			mbuycarList.loadedFinish();
			mbuycarList.isFirst = true;
		}
		if (mbuycarList.getAdapter() == null) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			mbuycarAdapter = new BuyCarAdapter(self, list,this);
			mbuycarList.setAdapter(mbuycarAdapter);
		} else {
			mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();

			mbuycarAdapter.addListItems(list);

		}
	}
	
	private void initView(){
		mbuycarList = (ClimbListView)findViewById(R.id.buy_list);
		mbuycarList.initFooterView();
		mbuycarList.setUpLoadListener(this);
		mbuycarList.setOnItemClickListener(this);
		
		buyResultView = (TextView)findViewById(R.id.buy_result);
		buyTotal = (TextView)findViewById(R.id.buy_total);
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
	public void onClick(View v) {
			//减法
			if(v.getId()==99){
				mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();
				for (int i = 0; i < list.size(); i++) {
					Dishes d = (Dishes)v.getTag();
					if(d.getId().equals(list.get(i).getId())){
						if(Integer.parseInt(d.getNum())<=1){
							Toast.makeText(self, "菜品数量不能在少了！", Toast.LENGTH_SHORT).show();
						}else{
							mbuycarAdapter.updateNumItem(list.get(i));
							//更新数据  计算总价
							List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{d.getId()},
									ClientDBHelper.getInstance(this));
							BuyCar b = buyCarList.get(0);
							int t= Integer.parseInt(b.getShopNum())-1;
							b.setShopNum(t+"");
							BuyCar.updateBuyCar(b, ClientDBHelper.getInstance(this));
							buyTotal.setText("￥"+totalPrice());
						}
					}
				}
				//Toast.makeText(self, "减法", Toast.LENGTH_SHORT).show();
			}
			
			if(v.getId()==101){
				mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();
				for (int i = 0; i < list.size(); i++) {
					Dishes d = (Dishes)v.getTag();
					if(d.getId().equals(list.get(i).getId())){
						if(Integer.parseInt(d.getNum())>=99){
							Toast.makeText(self, "菜品数量不能在多了！", Toast.LENGTH_SHORT).show();
						}else{
							mbuycarAdapter.updateAddNumItem(list.get(i));
							
							//更新数据  计算总价
							List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{d.getId()},
									ClientDBHelper.getInstance(this));
							BuyCar b = buyCarList.get(0);
							int t= Integer.parseInt(b.getShopNum())+1;
							b.setShopNum(t+"");
							BuyCar.updateBuyCar(b, ClientDBHelper.getInstance(this));
							buyTotal.setText("￥"+totalPrice());
						}
					}
				}
			//	Toast.makeText(self, "加法", Toast.LENGTH_SHORT).show();
			}
			
			if(v.getId()==102){
				mbuycarAdapter = (BuyCarAdapter) mbuycarList.getAdapter();
				for (int i = 0; i < list.size(); i++) {
					Dishes d = (Dishes)v.getTag();
					if(d.getId().equals(list.get(i).getId())){
						mbuycarAdapter.removeItem(d.getId());
						//更新数据  计算总价
						List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{d.getId()},
								ClientDBHelper.getInstance(this));
						BuyCar b = buyCarList.get(0);
						BuyCar.deleteBuyCar(b.getShopId(),  ClientDBHelper.getInstance(this));
						buyTotal.setText("￥"+totalPrice());
					}
				}
				
				
				
				//Toast.makeText(self, "移除", Toast.LENGTH_SHORT).show();
			}
			
			if(v.getId()==100||v.getId()==103||v.getId()==104){
				
			}
			
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
//		showToast(arg1+"");
//		Dishes t = mbuycarAdapter.getItem(position-1);
//		dd = t;
//		Intent intent = new Intent();
//		intent.setClass(self, DishDetailActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("dishId", t.getId());
//		bundle.putSerializable("dishes", t);
//		intent.putExtras(bundle);
//		startActivity(intent);
		
//		mDishStatisticsRequest = new DishStatisticsRequest();
//		mDishStatisticsRequest.setSource(2);
//		mDishStatisticsRequest.setDishId(Integer.parseInt(t.getId()));
//		mDishStatisticsRequest.setHasProcessDialog("0");
//		mDishStatisticsRequest.setDk();
//		mDishController.execute(Actions.ACTION_DISH_SC, mDishStatisticsRequest);
	}


	@Override
	public void scrollTopAction() {
	}


	@Override
	public void scrollBottomAction() {
	}


	
}

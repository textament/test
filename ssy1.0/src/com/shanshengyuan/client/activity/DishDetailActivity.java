package com.shanshengyuan.client.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.DishDetailAdapter;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.dishDetail.Batching;
import com.shanshengyuan.client.model.dishDetail.DishDetail;
import com.shanshengyuan.client.model.dishDetail.DishDetailRequest;
import com.shanshengyuan.client.model.dishDetail.Steps;
import com.shanshengyuan.client.model.dishDetail.UserBatching;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;
import com.shanshengyuan.client.utils.CollapsibleTextView;
import com.shanshengyuan.client.utils.ConvertUtils;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * 
 * @author lihao
 *
 */
public class DishDetailActivity extends BaseActivity implements
		OnResultListener,OnItemClickListener,UpLoadListener {
	
	DishDetailActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView morderpageList;
	private DishDetailAdapter morderpageAdapter;
	
	private View mHeadView;
	private LayoutInflater mInflater;
	
	DishDetail dd;
	
	List<Steps> list;
	
	private List<Batching> batchList;
	private List<UserBatching> userList;

	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;
	
	//中部配料
	private LinearLayout batLy;
	//健康食话
	private CollapsibleTextView contentTv;
	
	//图片
	private ImageView mImg;
	//菜品名称
	private TextView name;
	//金额
	private TextView amount;
	//膳食家提供文案
	private TextView showTv;
	
	//菜品详情请求
	private DishDetailRequest mDishDetailRequest;
	private DishController mDishController;
	
	//加入购物车
	private Button joinCarBtn;
	//立即购买
	private Button buyBtn;
	//价格
	private TextView mPrice;
	//温馨提示
	private LinearLayout wenLy;
	private TextView wenTv;

    private LinearLayout showButtomLy;
	
	Bundle b = null;
	String dId;
    int isShowButtom = 0;
	Dishes d ;
	DishesOrderFinish dof;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		BaseApplication.mInstance.addActivity(this);
		setContentView(R.layout.activity_dish_detail);
		mImageMap = new HashMap<String, ImageView>();
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		
		b = getIntent().getExtras();
		if(b!=null){
			dId = b.getString("dishId");
			dof = (DishesOrderFinish) b.getSerializable("dishfinish");
			d = (Dishes) b.getSerializable("dishes");
            isShowButtom = b.getInt("isShowButtom");
			if(dof!=null){
				d = new Dishes();
				d.setId(dof.getDishId());
				d.setImgUrl(dof.getImgUrl());
				d.setDishTypeName(dof.getDishTypeName());
				d.setName(dof.getName());
				d.setNum(dof.getNum());
				d.setPrice(dof.getPrice());
				d.setRemark(dof.getRemark());
			}
		}
		
		initView();
		initTopBar();
		initData();
		//updateUIData();
		setListener();
	}
	
	@Override
	protected void onDestroy() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}
	
	private void CreateBatching(Batching b){
		
		LinearLayout ly = new LinearLayout(self);
		ly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		ly.setPadding(ConvertUtils.dip2px(self, 20), 0, ConvertUtils.dip2px(self, 20), 0);
		ly.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView tv = new TextView(self);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT,0.3f));
		tv.setGravity(Gravity.LEFT|Gravity.CENTER);
		tv.setPadding(ConvertUtils.dip2px(self, 10), 0, 0, 0);
		tv.setText(b.getBurdeningName());
		tv.setTextColor(getResources().getColor(R.color.graydk));
		tv.setTextSize(16);
		
		TextView heavyTv = new TextView(self);
		heavyTv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT,0.7f));
		heavyTv.setGravity(Gravity.RIGHT|Gravity.CENTER);
		heavyTv.setPadding(0, 0, ConvertUtils.dip2px(self, 10), 0);
		heavyTv.setText(b.getWeight());
		heavyTv.setTextColor(getResources().getColor(R.color.graydk));
		heavyTv.setTextSize(16);
		
		ly.addView(tv);
		ly.addView(heavyTv);
		batLy.addView(ly);
	}
	
	private void CreateUserBatching(UserBatching ub){
		LinearLayout ly = new LinearLayout(self);
		ly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		ly.setPadding(ConvertUtils.dip2px(self, 20), 0, ConvertUtils.dip2px(self, 20), 0);
		ly.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView tv = new TextView(self);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT,0.3f));
		tv.setGravity(Gravity.LEFT|Gravity.CENTER);
		tv.setPadding(ConvertUtils.dip2px(self, 10), 0, 0, 0);
		tv.setText(ub.getBurdeningName());
		tv.setTextColor(getResources().getColor(R.color.graydk));
		tv.setTextSize(16);
		
		TextView heavyTv = new TextView(self);
		heavyTv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT,0.7f));
		heavyTv.setGravity(Gravity.RIGHT|Gravity.CENTER);
		heavyTv.setPadding(0, 0, ConvertUtils.dip2px(self, 10), 0);
		heavyTv.setText(ub.getWeight());
		heavyTv.setTextColor(getResources().getColor(R.color.graydk));
		heavyTv.setTextSize(16);
		
		ly.addView(tv);
		ly.addView(heavyTv);
		batLy.addView(ly);
	}
	
	private void setListener(){
		buyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{dId},
//						ClientDBHelper.getInstance(self));
//					if(buyCarList!=null){
//						if(buyCarList.size()!=0){
//							updateBuyCar(buyCarList.get(0));
//							Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
//						}else{
//							createBuyCar(d);
//							Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
//						}
//					}else{
//						createBuyCar(d);
//						Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
//					}
					Intent intent = new Intent();
					Bundle b = new Bundle();
					intent.setClass(self, OrderPageActivity.class);
					b.putString("didStr", "id="+ dId+"|num=1");
					intent.putExtras(b);
					startActivity(intent);
			}
		});
		
		joinCarBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断菜品是否加入购入车，有就更新，没有就新增
				List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{dId},
						ClientDBHelper.getInstance(self));
					if(buyCarList!=null){
						if(buyCarList.size()!=0){
							updateBuyCar(buyCarList.get(0));
							Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
						}else{
							createBuyCar(d);
							Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
						}
					}else{
						createBuyCar(d);
						Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();
					}
			}
		});
	}
	
	private void updateBuyCar(BuyCar b){
		int t= Integer.parseInt(b.getShopNum())+1;
		b.setShopNum(t+"");
		BuyCar.updateBuyCar(b, ClientDBHelper.getInstance(this));
	}
	
	private void createBuyCar(Dishes d){


		BuyCar b = new BuyCar();
		b.setShopId(d.getId());
		b.setShopImg(d.getImgUrl());
		b.setShopContent(d.getRemark());
		b.setShopDishType(d.getDishTypeName());
		b.setShopName(d.getName());
		b.setShopNum("1");
		b.setShopPrice(d.getPrice());
		BuyCar.insertBuyCar(b,
				ClientDBHelper.getInstance(this));
	}
	
	private void initData(){
		mDishDetailRequest = new DishDetailRequest();
		mDishDetailRequest.setDishId(Integer.parseInt(dId));
		mDishDetailRequest.setDk();
		mDishController.execute(Actions.ACTION_DISH_DETAIL, mDishDetailRequest);
		
		
//		list = new ArrayList<Steps>();
//	
//		for (int i = 0; i < 5; i++) {
//			Steps s = new Steps();
//			s.setContent("内容步骤"+i);
//		//	s.setsImg(sImg)
//			s.setsNum(i+"");
//			list.add(s);
//		}
//		
//		batchList = new ArrayList<Batching>();
//		
//		for (int j = 0; j < 3; j++) {
//			Batching b = new Batching();
//			b.setName("配料"+j);
//			b.setHeavy("10g");
//			batchList.add(b);
//			CreateBatching(b);
//		}
//		
//		userList = new ArrayList<UserBatching>();
//		
//		for (int z = 0; z < 1; z++) {
//			UserBatching b = new UserBatching();
//			b.setName("配料"+z);
//			b.setHeavy("20g");
//			userList.add(b);
//			
//		}
		
//		if(userList.size()!=0){
//			TextView userTv = new TextView(self);
//			userTv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//					LayoutParams.WRAP_CONTENT,1));
//			userTv.setGravity(Gravity.LEFT|Gravity.CENTER);
//			userTv.setPadding(ConvertUtils.dip2px(self, 4), ConvertUtils.dip2px(self, 6), 0, ConvertUtils.dip2px(self, 6));
//			userTv.setText("（用户自备）");
//			userTv.setTextColor(getResources().getColor(R.color.graydk));
//			userTv.setTextSize(18);
//			batLy.addView(userTv);
//		}
//		
//		
//		for (int m = 0; m < userList.size(); m++) {
//			CreateUserBatching(userList.get(m));
//		}
//		
	}
	
	private void updateUIData(){
		dd = mDishController.getmDishDetail();
		
		String url = dd.getImgBig();
		
		if(!StringUtils.isEmpty(url)){
			SSYImageCache.getInstance().downloadImage(mImageMap, mImg,
					url, false, new ImageCallback() {

						@Override
						public void onImageLoaded(Bitmap bitmap, String imageUrl) {
							mImageMap.get(imageUrl).setImageBitmap(bitmap);

						}
					});
			
		}else{
			mImg.setImageResource(R.drawable.defaule_img);
		}
		name.setText(dd.getName());
		amount.setText("￥"+dd.getPrice());
		mPrice.setText("￥"+dd.getPrice());
		if(StringUtils.isEmpty(dd.getWarmPrompt())){
			wenLy.setVisibility(View.GONE);
		}else{
			wenLy.setVisibility(View.VISIBLE);
			wenTv.setText(dd.getWarmPrompt());
		}
		
		contentTv.setDesc(dd.getHealthTalk(), BufferType.NORMAL);
		
		batchList = dd.getBurdeningSSY();
		
		if(batchList.size()==0){
			showTv.setVisibility(View.GONE);
		}
		
		for (int j = 0; j < batchList.size(); j++) {
			CreateBatching(batchList.get(j));
		}
		
		userList = dd.getBurdeningUser();
		
		if(userList.size()!=0){
			TextView userTv = new TextView(self);
			userTv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT,1));
			userTv.setGravity(Gravity.LEFT|Gravity.CENTER);
			userTv.setPadding(ConvertUtils.dip2px(self, 4), ConvertUtils.dip2px(self, 6), 0, ConvertUtils.dip2px(self, 6));
			userTv.setText("（用户自备）");
			userTv.setTextColor(getResources().getColor(R.color.tigong_font_color));
			userTv.setTextSize(18);
			batLy.addView(userTv);
		}
		
		for (int m = 0; m < userList.size(); m++) {
			CreateUserBatching(userList.get(m));
		}
		
		list = dd.getStep();
		
		for (int i = 0; i < list.size(); i++) {
			Steps s = list.get(i);
			s.setsNum(i+"");
		}
		
		if (list.size() == 0) {
			morderpageList.noDataFinishNoScroll();
			//morderpageList.setVisibility(View.GONE);
		//	mNoReleaseLayout.setVisibility(View.VISIBLE);
			//return;
		} else if (list.size() < 10) {
			morderpageList.setVisibility(View.VISIBLE);
		//	mNoReleaseLayout.setVisibility(View.GONE);
			morderpageList.setHiddenFooterViewForScroll();

			com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
		} else {
			morderpageList.setVisibility(View.VISIBLE);
		//	mNoReleaseLayout.setVisibility(View.GONE);
			morderpageList.loadedFinish();
			morderpageList.isFirst = true;
		}
		if (morderpageList.getAdapter() == null) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			morderpageAdapter = new DishDetailAdapter(self, list);
			morderpageList.setAdapter(morderpageAdapter);
		} else {
			morderpageAdapter = (DishDetailAdapter) morderpageList.getAdapter();
			morderpageAdapter.addListItems(list);

		}
	}
	
	private void initView(){
		mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.dish_detail_top, null);
		morderpageList = (ClimbListView)findViewById(R.id.detail_list);
		morderpageList.addHeaderView(mHeadView);
		morderpageList.initFooterView();
		morderpageList.setUpLoadListener(this);
		morderpageList.setOnItemClickListener(this);

        showButtomLy = (LinearLayout)findViewById(R.id.show_buttom);
		batLy = (LinearLayout)mHeadView.findViewById(R.id.detail_veg);
		mImg = (ImageView)mHeadView.findViewById(R.id.detail_img);
		name = (TextView)mHeadView.findViewById(R.id.detail_name);
		amount = (TextView)mHeadView.findViewById(R.id.detail_amount);
		contentTv = (CollapsibleTextView)mHeadView.findViewById(R.id.detail_content_tv);
		showTv = (TextView)mHeadView.findViewById(R.id.ti_show);
		wenLy = (LinearLayout)mHeadView.findViewById(R.id.wenxin_ly);
		wenTv = (TextView)mHeadView.findViewById(R.id.wen_show);
		
		joinCarBtn = (Button)findViewById(R.id.join_car);
        if(d==null){
            joinCarBtn.setVisibility(View.GONE);
        }
		buyBtn = (Button)findViewById(R.id.detail_result);
		mPrice = (TextView)findViewById(R.id.detail_total);

        if(isShowButtom==1){
            showButtomLy.setVisibility(View.GONE);
        }else if(isShowButtom==0){
            showButtomLy.setVisibility(View.VISIBLE);
        }
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
		case Actions.ACTION_DISH_DETAIL:
			updateUIData();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void scrollTopAction() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void scrollBottomAction() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}

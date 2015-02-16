package com.shanshengyuan.client.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.MainAdapter;
import com.shanshengyuan.client.controller.ADController;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.ad.Adver;
import com.shanshengyuan.client.model.ad.AdverRequest;
import com.shanshengyuan.client.model.dish.DishListRequest;
import com.shanshengyuan.client.model.dish.DishType;
import com.shanshengyuan.client.model.dish.DishTypeRequest;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.redpackage.MyNum;
import com.shanshengyuan.client.model.redpackage.MyNumRequest;
import com.shanshengyuan.client.model.statistics.DishStatisticsRequest;
import com.shanshengyuan.client.model.version.NewVersionResquest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;
import com.shanshengyuan.client.utils.ConvertUtils;
import com.shanshengyuan.client.utils.PullToRefreshListView.OnRefreshListener;
import com.shanshengyuan.client.utils.StringUtils;
import com.shanshengyuan.client.widgets.NewBannerLayout;
import com.shanshengyuan.client.ziguang.AccountInfo;
import com.shanshengyuan.client.ziguang.LoginfoParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zwan.phone.ZwanphoneManager;

@SuppressLint("NewApi")
public class MainNewActivity extends BaseActivity implements OnResultListener,
		OnClickListener, OnItemClickListener, UpLoadListener,OnKeyListener {

	private MainNewActivity self = null;

	private boolean mIsAnimStoped = true;
	private boolean mIsFlipperOut = true;
	// 用户中心
	private ImageView userImg;
	// 订单
	private TextView orderView;

	// 圆点
	private ImageView pointImg;

	private View mHeadView;
    private View mHeadView2;
    private LayoutInflater mInflater;
	// 广告位
	private LinearLayout barLy;
	private NewBannerLayout mBannerLayout = null; // 运营区广告

	// 列表
	private ClimbListView mDataView;
	private MainAdapter adapter;

	// 底部价格
	private TextView amountView;

	// 分类
	private TextView typeView;
	private View mMaskView; //背景色
	private View mCategoryView; //分类层
	private View mProgress;
	private LinearLayout typeLy; //分类列表

	private LinearLayout goShopView;

	private Animation mFlipperOutAnim;
	private Animation mFlipperInAnim;
	private AnimationListener mFlipperListener;



    //顶部栏显示隐藏
    private Animation mShowAnim;
    private Animation mHideAnim;

    private LinearLayout showTopLy;
    private TextView showTopTv;

    private Boolean isHide = true;
    private Boolean isShow = true;


	// 广告请求
	private AdverRequest mAdverRequest;
	private ADController mADController;
	List<Adver> list = null;

	// 菜品列表请求
	private DishListRequest mDishListRequest;
	private DishController mDishController;

	
	//分类
	private DishTypeRequest mDishTypeRequest;
	private List<DishType> dishTypeList = null;
	
	float totalAmount = 0;
	
	private long mExitTime = 0;
	
	ImageView dd ;
	
	private ImageView callView;
	
	//菜品点击统计
	private DishStatisticsRequest mDishStatisticsRequest;
	
	//版本更新
	private NewVersionResquest mNewVersionResquest;
	private AddressController mAddressController;

    //搜索
    private EditText mSearchEd;
    //菜品分类id
    private String dishId="0";

    //无数据展示
    private ImageView noDataImg;

    //是否第一次查询
    private Boolean isFirstData = true;

    //红包抵用卷数量
    private MyNumRequest myNumRequest;
    private ImageView newImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (mADController == null) {
			mADController = new ADController(self, self);
		}
		if (mDishController == null) {
			mDishController = new DishController(self, self);
		}
		if(mAddressController == null){
			mAddressController = new AddressController(self, self);
		}

		initView();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				initNetAccount();
			}
		}).start();
		initData();
		setListener();
		initCatViewAnim();
		// updateUI();
	}
	
	//测试打电话
		private void kcall()
		{
			String to="558888@115.29.188.171";
			String displayName=null;
			try {
				ZwanphoneManager.getInstance().newOutgoingCall(to, displayName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	private void initNetAccount()
	{
		try {
			String xmlpath="http://ent.zwan.com.cn/assets/xml/getFlashUser.xml";
			Map<String, Object> pass=new HashMap<String, Object>();
			pass.put("companyemail", "123@163.com");
			pass.put("appkey", "SAhvWrDPMZRnUOwuBixmbpNAkeKaVBIl");
			pass.put("companypbx", "558888");
			String xml=com.shanshengyuan.client.ziguang.HttpUtil.sendTbXmlPostMessage(xmlpath, pass);
			AccountInfo accinfo=LoginfoParser.parserAccount(xml);
			if(accinfo!=null)
			{
				String uname=accinfo.getUsrAcc();
				String upwd=accinfo.getUsrPwd();
				System.out.println("----------uname="+uname+",upwd="+upwd);
				String domain="115.29.188.171";
				try {
					ZwanphoneManager.getInstance().accountRegistion(uname, upwd, domain);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
	}

	private void initData() {
		mAdverRequest = new AdverRequest();
		mAdverRequest.setHasProcessDialog("0");
		mAdverRequest.setDk();
		mADController.execute(Actions.ACTION_AD, mAdverRequest);
		
		mDishListRequest = new DishListRequest();
		mDishListRequest.setPage(1);
		mDishListRequest.setPageSize(10);
		mDishListRequest.setDk();
		mDishController.execute(Actions.ACTION_DISHES_LIST, mDishListRequest);
		
		mDishTypeRequest = new DishTypeRequest();
		mDishTypeRequest.setHasProcessDialog("0");
		mDishTypeRequest.setDk();
		mDishController.execute(Actions.ACTION_DISH_TYPE, mDishTypeRequest);
		
	}

    private void searchData(){
        BaseApplication.isReflash = true;
        isFirstData = true;
        dishList = new ArrayList<Dishes>();
        mDishListRequest = new DishListRequest();
        mDishListRequest.setPage(1);
        mDishListRequest.setPageSize(10);
      // mDishListRequest.setHasProcessDialog("0");
//        if(!StringUtils.isEmpty(dishId)){
//            if(!dishId.equals("0"))
//                mDishListRequest.setDishTypeId(Integer.parseInt(dishId));
//        }

        if(!StringUtils.isEmpty(mSearchEd.getText().toString())){
            mDishListRequest.setSearchDishName(mSearchEd.getText().toString());
        }
        mDishListRequest.setDk();
        mDishController.execute(Actions.ACTION_DISHES_LIST, mDishListRequest);
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
		if(totalAmount!=0){
			amountView.setText("(￥"+fnum.format(totalAmount)+")去购物车结算");
		}else{
			amountView.setText("请加入购物车");
		}
        //只检测一次
		if(BaseApplication.isVersionFrist){
            mNewVersionResquest = new NewVersionResquest();
            mNewVersionResquest.setHasProcessDialog("0");
            mNewVersionResquest.setDk();
            mAddressController.execute(Actions.ACTION_NEW_VERSION, mNewVersionResquest);
        }
        BaseApplication.isVersionFrist = false;

        myNumRequest = new MyNumRequest();
        myNumRequest.setHasProcessDialog("0");
        myNumRequest.setDk();
        mAddressController.execute(Actions.ACTION_RED_AR_NUM,myNumRequest);
		
		super.onResume();
	}
	
	private void reflash(String dishTypeId){
		BaseApplication.isReflash = true;
        isFirstData = true;
		mDishListRequest = new DishListRequest();
		mDishListRequest.setPage(1);
		mDishListRequest.setPageSize(10);
        dishId = dishTypeId;
		if(!dishTypeId.equals("0"))
			mDishListRequest.setDishTypeId(Integer.parseInt(dishTypeId));

        if(!StringUtils.isEmpty(mSearchEd.getText().toString())){
            mDishListRequest.setSearchDishName(mSearchEd.getText().toString());
        }
		mDishListRequest.setDk();
	
		mDishController.execute(Actions.ACTION_DISHES_LIST, mDishListRequest);
	}

	private void updateUI() {

		if (mDishListRequest == null) {
			mDishListRequest = new DishListRequest();
			mDishListRequest.setHasProcessDialog("0");
		} else {
			mDishListRequest.setHasProcessDialog("0");
		}
		mDishListRequest.setPage(mDishListRequest.getPage());
		mDishListRequest.setPageSize(mDishListRequest.getPageSize());
        if(!dishId.equals("0"))
            mDishListRequest.setDishTypeId(Integer.parseInt(dishId));

        if(!StringUtils.isEmpty(mSearchEd.getText().toString())){
            mDishListRequest.setSearchDishName(mSearchEd.getText().toString());
        }
		mDishListRequest.setDk();
		mDishController.execute(Actions.ACTION_DISHES_LIST, mDishListRequest);

	}

	private void setListener() {

		
		callView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (!mWifi.isConnected()) {
					Uri uri = Uri.parse("tel:400-999-9227");
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}else{
					kcall();
					Intent intent = new Intent();
					intent.setClass(self, CallActivity.class);
					startActivity(intent);
				}
				
				
			}
		});

		goShopView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, BuyCarActivity.class);
				startActivity(intent);
			}
		});

		userImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, UserCenterActivity.class);
				startActivity(intent);


			}
		});

        mSearchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
             //   Log.e("actionId",actionId+"");
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        searchData();
                    mSearchEd.setText("");
              //      isFirstData = false;
                  }

                         //   mSearchEd.setText("");

                return false;
            }
        });

		orderView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent();
//				intent.setClass(self, OrderListActivity.class);
//				startActivity(intent);
                Intent intent = new Intent();
                intent.setClass(self, MealActivity.class);
                startActivity(intent);
//                if(StringUtils.isEmpty(mSearchEd.getText().toString())){
//                    showToast("请输入美食名称!");
//                    return;
//                }
//
//                searchData();

			}
		});

		typeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCatListView();
			}
		});

		mMaskView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeCatListView();
			}
		});

	}

	private void closeCatListView() {
		if (mIsAnimStoped && !mIsFlipperOut) {
			mCategoryView.startAnimation(mFlipperOutAnim);
		}
	}

	private void showCatListView() {
		if (mIsAnimStoped && mIsFlipperOut) {
			mCategoryView.setVisibility(View.VISIBLE);
			typeLy.setVisibility(View.VISIBLE);
			mCategoryView.startAnimation(mFlipperInAnim);
		}
	}
	
	

	private void initView() {
        newImg = (ImageView)findViewById(R.id.new_img);
        noDataImg = (ImageView)findViewById(R.id.no_data);
        mSearchEd = (EditText)findViewById(R.id.search_edit);
        mSearchEd.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        showTopLy = (LinearLayout)findViewById(R.id.show_top_ly);
        showTopTv = (TextView)findViewById(R.id.show_top_tv);
		callView = (ImageView)findViewById(R.id.call_kehu);
		goShopView = (LinearLayout) findViewById(R.id.go_shopping_ly);
		userImg = (ImageView) findViewById(R.id.main_user_center);
		orderView = (TextView) findViewById(R.id.main_order_list);
		pointImg = (ImageView) findViewById(R.id.new_img);
		dd = (ImageView)findViewById(R.id.ant_img);
		mDataView = (ClimbListView) findViewById(R.id.list);
		mDataView.initFooterView();
		mDataView.setUpLoadListener(this);
		mDataView.setOnItemClickListener(this);
		dishListShop = new ArrayList<Dishes>();
		mDataView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (adapter != null) {
					adapter.removeAllListData();

                    searchData();
				}
			}
		});

		mInflater = (LayoutInflater) self
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.activity_main_top, null);
		mDataView.addHeaderView(mHeadView);
		mBannerLayout = new NewBannerLayout(self);
		mBannerLayout.init();
		barLy = (LinearLayout) findViewById(R.id.banner_ly);
		mBannerLayout.setOnClickListener(this);
		barLy.addView(mBannerLayout);

		amountView = (TextView) findViewById(R.id.go_shopping_car);
		
		typeView = (TextView) findViewById(R.id.dish_type);
		typeLy = (LinearLayout) findViewById(R.id.cat_list);

		mMaskView = findViewById(R.id.map_mask);
		mCategoryView = findViewById(R.id.cat_ly);
		mProgress = findViewById(R.id.progress_bar);
		//sv = (ServiceDeplyView)findViewById(R.id.service_deply);
	
	}

	private void initCatViewAnim() {
		mProgress.setVisibility(View.GONE);
		mFlipperListener = new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animation.hashCode() == mFlipperOutAnim.hashCode()) {
					mIsFlipperOut = true;
					mCategoryView.setVisibility(View.GONE);
				} else if (animation.hashCode() == mFlipperInAnim.hashCode()) {
					mIsFlipperOut = false;
					mMaskView.setVisibility(View.VISIBLE);
				}else if (animation.hashCode() == mHideAnim.hashCode()){
                    isHide = false;
                    isShow = true;

          //          mTopLy.setVisibility(View.GONE);
//                    showTopLy.setVisibility(View.GONE);
//                    showTopTv.setText("顶部隐藏");
                }else if(animation.hashCode() == mShowAnim.hashCode()){
                    isHide = true;
                    isShow = false;

//                    showTopLy.setVisibility(View.VISIBLE);
//                    showTopTv.setText("顶部显示");

                }
				mIsAnimStoped = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
				mIsAnimStoped = false;
				if (animation.hashCode() == mFlipperInAnim.hashCode()) {
					mCategoryView.setVisibility(View.VISIBLE);
				} else if (animation.hashCode() == mFlipperOutAnim.hashCode()) {
					mMaskView.setVisibility(View.GONE);
				}else if (animation.hashCode() == mHideAnim.hashCode()){
                    isHide = false;

                   // mTopLy.setVisibility(View.GONE);
//                    showTopLy.setVisibility(View.GONE);
//                    showTopTv.setText("顶部隐藏");
                }else if(animation.hashCode() == mShowAnim.hashCode()){
                    isShow = false;
                //    mTopLy.setVisibility(View.INVISIBLE);
//                    showTopLy.setVisibility(View.VISIBLE);
//                    showTopTv.setText("顶部显示");
                }
			}

		};
		mFlipperOutAnim = AnimationUtils
				.loadAnimation(this, R.anim.flipper_out);
		mFlipperOutAnim.setAnimationListener(mFlipperListener);
		mFlipperInAnim = AnimationUtils.loadAnimation(this, R.anim.flipper_in);
		mFlipperInAnim.setAnimationListener(mFlipperListener);

        mHideAnim = AnimationUtils.loadAnimation(this,R.anim.show_in);
        mHideAnim.setAnimationListener(mFlipperListener);
        mShowAnim = AnimationUtils.loadAnimation(this,R.anim.show_out);
        mShowAnim.setAnimationListener(mFlipperListener);

	}
	
	private void updateDishType(){
		dishTypeList = new ArrayList<DishType>();
        DishType dd = new DishType();
        dd.setId("0");
        dd.setName("全部");
        dishTypeList.add(dd);
		
		List<DishType> l = mDishController.getmDishTypeList();
		if(l!=null){
			for (int i = 0; i < l.size(); i++) {
				dishTypeList.add(l.get(i));
			}
		}
	
		createServiceView(dishTypeList);
//		if(dishTypeList!=null){
//			if(dishTypeList.size()!=0){
//				sv.setmOuterLyVisible();
//				sv.createServiceView(dishTypeList);
//			}else{
//				sv.setVisibility(View.GONE);
//			}
//		}else{
//			sv.setVisibility(View.GONE);
//		}
		
	}
	
	
	
	public void createServiceView(List<DishType> servicelist){
		if(servicelist.size()!=0){
			int size = servicelist.size()/2;
			if(servicelist.size()%2!=0){
				size = size+1;
			}
			for (int i = 0; i < size; i++) {
				LinearLayout outer = new LinearLayout(self);
				LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				outer.setLayoutParams(lps);
				//outer.setBackgroundColor(getResources().getColor(R.color.alpha));
				outer.setBackgroundResource(R.drawable.home_category_box_s);
				outer.setOrientation(LinearLayout.HORIZONTAL);
				int newSize = (i+1)*2;
				int initSize = i*2;
//				if((i+1)==size){
//					initSize = servicelist.size()-servicelist.size()%2;
//					newSize = servicelist.size();
//				}
				for ( int j = initSize; j <newSize; j++) {
					LinearLayout iner = new LinearLayout(self);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							ConvertUtils.dip2px(self, 80),1);
				//	iner.setBackgroundResource(R.drawable.home_category_box);
					iner.setWeightSum(1);
					iner.setLayoutParams(params);
					iner.setGravity(Gravity.CENTER);
					//iner.setPadding(5, 0, 5, 0);
					iner.setOrientation(LinearLayout.VERTICAL);
					final String ids = servicelist.get(j).getId();
					iner.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							dishListShop = new ArrayList<Dishes>();
										reflash(ids);
										closeCatListView();
							}
					});
					
					TextView textView = new TextView(self);
					LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					//textView.setSingleLine(true);
					textView.setTextColor(getResources().getColor(R.color.fenlei_font));
					textView.setTextSize(ConvertUtils.dip2px(self, 10));
					//textView.setEllipsize(TextUtils.TruncateAt.valueOf("MIDDLE")); 
					textView.setLayoutParams(textParams);
					textView.setGravity(Gravity.CENTER|Gravity.TOP);
					textView.setText(servicelist.get(j).getName());
					iner.addView(textView);
					outer.addView(iner);
				}
				typeLy.addView(outer);
			}
			
			
		}
		
	}
	
	 List<Dishes> dishList = null;
	
	 List<Dishes> dishListShop = null;

	private void updateDishList() {

		dishList = mDishController.getmDishList();

		for (int i = 0; i < dishList.size(); i++) {
			dishListShop.add(dishList.get(i));
		}
		
		if (dishList != null) {
			if (dishList.size() == 0) {
				mDataView.noDataFinishNoScroll();
				mDataView.onRefreshComplete();
               //    noDataImg.setVisibility(View.VISIBLE);

				//mDataView.setVisibility(View.GONE);
				// mNoReleaseLayout.setVisibility(View.VISIBLE);
				//return;
			} else if (dishList.size() < 10) {
				mDataView.setVisibility(View.VISIBLE);
				// mNoReleaseLayout.setVisibility(View.GONE);
				mDataView.setHiddenFooterViewForScroll();
				com.shanshengyuan.client.utils.Log.e("listsize",
						dishList.size() + "");
			} else {
				mDataView.setVisibility(View.VISIBLE);
				// mNoReleaseLayout.setVisibility(View.GONE);
				mDataView.loadedFinish();
				mDataView.isFirst = true;
			}
		} else {
			dishList = new ArrayList<Dishes>();
		}
		if (mDataView.getAdapter() == null) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			adapter = new MainAdapter(self, dishList,this);
          //  adapter.removeAllListData();
			mDataView.setAdapter(adapter);
		} else {
			adapter = (MainAdapter) mDataView.getAdapter();
			 if(BaseApplication.isReflash){
				 adapter.removeAllListData();
				 BaseApplication.isReflash = false;
			 }
			adapter.addListItems(dishList);

		}
		mDataView.onRefreshComplete();
	}

	private void updateAdver() {
		list = new ArrayList<Adver>();
		if (mADController.getmAdList() == null) {
			barLy.setVisibility(View.GONE);
			// mBannerLayout.setLayoutParams(new AbsListView.LayoutParams(0,
			// 0));
		} else {
			list = mADController.getmAdList();
			if (list.size() != 0) {
				barLy.setVisibility(View.VISIBLE);
			} else {
				barLy.setVisibility(View.GONE);
			}
		}
		mBannerLayout.setInit(list, this);
	}

	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_AD:
			updateAdver();
			break;
		case Actions.ACTION_DISHES_LIST:
			mDishListRequest.next();
			updateDishList();
			
			break;
		case Actions.ACTION_DISH_TYPE:
			updateDishType();
			break;
		case Actions.ACTION_NEW_VERSION:
			int status = mAddressController.getUpdateState();
			String url = mAddressController.getUrlStr();
			String message = mAddressController.getMessage();
			if ("1".equals(status+"")) { // 判断本地是否需要显示
				Intent intent = new Intent();
				intent.setClass(self, SystemVersionActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);

				Bundle bundle = new Bundle();
				bundle.putString("status",status+"");
				bundle.putString("version", "");
				bundle.putString("message", message);
				bundle.putString("url", url);
				bundle.putBoolean("is_show", false);

				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
            case Actions.ACTION_RED_AR_NUM:
                MyNum num = mAddressController.getMyNum();
                if(num.getArTotalNum()!=0||num.getRedTotalNum()!=0){
                    newImg.setVisibility(View.VISIBLE);
                }else{
                    newImg.setVisibility(View.INVISIBLE);
                }
            break;
		default:
			break;
		}

	}

	@Override
	public void onFailed(int actionType, BaseResponse result) {
		if(actionType==Actions.ACTION_DISHES_LIST){
			
		}
		super.onFailed(actionType, result);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == -1) {
            //前面还有一张图片。所以减1
			Adver ad = list.get(mBannerLayout.getViewPaperCurrentItem()-1);
			String url = ad.getUrl();
			Log.e("url========================", url);
			if (url.contains("package.html")) {
                Intent intent = new Intent();
                intent.setClass(self, MealActivity.class);
                startActivity(intent);
			} else if (url.contains("http")) {
				Intent intent = new Intent();
				intent.setClass(self, AdverWebActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("adurl", ad.getUrl());
				intent.putExtras(bundle);
				startActivity(intent);

			}else {
                Dishes dish = null;
                for (int i=0;i<dishList.size();i++){
                    if(ad.getUrl().equals(dishList.get(i).getId())){
                        dish = dishList.get(i);
                        break;
                    }
                }

				Intent intent = new Intent();
				intent.setClass(self, DishDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("dishId", ad.getUrl());
                bundle.putSerializable("dishes", dish);
				intent.putExtras(bundle);
				startActivity(intent);
				
//				mDishStatisticsRequest = new DishStatisticsRequest();
//				mDishStatisticsRequest.setSource(0);
//				mDishStatisticsRequest.setDishId(Integer.parseInt(ad.getUrl()));
//				mDishStatisticsRequest.setHasProcessDialog("0");
//				mDishStatisticsRequest.setDk();
//				mDishController.execute(Actions.ACTION_DISH_SC, mDishStatisticsRequest);
			}
		}
		
		if(dishListShop!=null){
			for (int i = 0; i < dishListShop.size(); i++) {
				if(dishListShop.get(i).getId().equals(v.getId()+"") ){
					Dishes d = (Dishes) v.getTag();
					
					//判断菜品是否加入购入车，有就更新，没有就新增
					List<BuyCar> buyCarList = BuyCar.getBuyCar(" shopId = ? ", new String[]{d.getId()},
							ClientDBHelper.getInstance(this));
						if(buyCarList!=null){
							if(buyCarList.size()!=0){
								updateBuyCar(buyCarList.get(0));
								String pri = "";
								if(!d.getId().equals(buyCarList.get(0).getId()+"")){
									pri = dishListShop.get(i).getPrice();
									buyCarList.get(0).setShopPrice(pri);
									updateBuyCarPrice(buyCarList.get(0));
								}else{
									pri = buyCarList.get(0).getShopPrice();
								}
								float total = Float.parseFloat(pri);
								totalAmount = totalAmount+total;
								DecimalFormat fnum = new DecimalFormat("##0.0"); 
								amountView.setText("(￥"+fnum.format(totalAmount)+")去购物车结算");
								Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();

								break;
							}else{
								createBuyCar(dishListShop.get(i));
								float total = Float.parseFloat(dishListShop.get(i).getPrice());
								totalAmount = totalAmount+ total;
								DecimalFormat fnum = new DecimalFormat("##0.0"); 
								amountView.setText("(￥"+fnum.format(totalAmount)+")去购物车结算");
								Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();

								break;
							}
						}else{
							createBuyCar(dishListShop.get(i));
							float total = Float.parseFloat(dishListShop.get(i).getPrice());
							totalAmount = totalAmount+ total;
							DecimalFormat fnum = new DecimalFormat("##0.0"); 
							amountView.setText("(￥"+fnum.format(totalAmount)+")去购物车结算");
							Toast.makeText(self, "恭喜，已加入购物车", Toast.LENGTH_SHORT).show();

							break;
						}
				}
			}
		}
		
	}

	private void updateBuyCar(BuyCar b){
		int t= Integer.parseInt(b.getShopNum())+1;
		b.setShopNum(t+"");
		BuyCar.updateBuyCar(b, ClientDBHelper.getInstance(this));
	}
	
	private void updateBuyCarPrice(BuyCar b){
		String t= b.getShopPrice();
		b.setShopPrice(t);
		BuyCar.updateBuyCarPrice(b, ClientDBHelper.getInstance(this));
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

	@Override
	public void scrollTopAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrollBottomAction() {
		if (mDataView.getAdapter() != null) {
			if (mDishListRequest != null
					&& mDataView.getAdapter().getCount() > 0) {
				mDataView.loadingFinish();
				updateUI();

			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Dishes t = adapter.getItem(position-2);
		Intent intent = new Intent();
		intent.setClass(self, DishDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("dishId", t.getId());
		bundle.putSerializable("dishes", t);
		intent.putExtras(bundle);
		startActivity(intent);
		
//		mDishStatisticsRequest = new DishStatisticsRequest();
//		mDishStatisticsRequest.setSource(1);
//		mDishStatisticsRequest.setDishId(Integer.parseInt(t.getId()));
//		mDishStatisticsRequest.setHasProcessDialog("0");
//		mDishStatisticsRequest.setDk();
//		mDishController.execute(Actions.ACTION_DISH_SC, mDishStatisticsRequest);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!mIsFlipperOut){
               closeCatListView();
            }else{
                exit();
            }

			return false;
		}
	
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			// PushManager.stopWork(this);
			// ZBJImageCache.getInstance().clearAll();
			// PushManager.activityStoped(this);
			finish();
			System.exit(0);
		}
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}

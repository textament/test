/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.Config;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.OrderResultAdapter;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.order.OrderDetail;
import com.shanshengyuan.client.model.order.OrderDetailRequest;
import com.shanshengyuan.client.model.statistics.DishStatisticsRequest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;
import com.shanshengyuan.client.utils.StringUtils;
import com.shanshengyuan.client.utils.alipay.Keys;
import com.shanshengyuan.client.utils.alipay.Result;
import com.shanshengyuan.client.utils.alipay.Rsa;

/**
 * @author Administrator
 *
 */
public class OrderDishListActivity extends BaseActivity implements
		OnResultListener,OnItemClickListener,UpLoadListener,OnClickListener {

	OrderDishListActivity self = null;
	
	View mBack;
	
	// 列表
	private ClimbListView morderpageList;
	private OrderResultAdapter morderpageAdapter;
	
	private View mHeadView;
	private LayoutInflater mInflater;
	
	List<DishesOrderFinish> list;
	
	// 图片缓存
	private HashMap<String, ImageView> mImageMap = null;
	
	Bundle b = null;
	  
	//支付是否成功展示
	private TextView isPayTv;
	//订单号
	private TextView orderNoTv;
	//支付状态
	private TextView payStateTv;
	//订单状态
	private TextView orderStateTv;
	//取菜时间
	private TextView vegTimeTv;
	//下单时间
	private TextView orderTimeTv;
	//商铺电话
	private TextView storePhoneTv;
	//二维码地址
	private ImageView twoImg;
	
	//金额
	private TextView amountTv;
	//取菜人
	private TextView nameTv;
	//电话
	private TextView phoneTv;
	//取菜地址
	private TextView addressTv;
	//配送信息
	private TextView sendTv;
	private View line;
	String orderId;
	
	private TextView infoTv;
	
	private OrderDetailRequest mOrderDetailRequest;
	private DishController mDishController;
	
	private OrderDetail orderDetail;
	
	//菜品点击统计
	private DishStatisticsRequest mDishStatisticsRequest;
	//是否清空购物车
	private int clearBuyCar=0;
	//是否支付成功
	private int payType = 1;
	//是否显示顶部支付提示
	private int isShowPay = 1;
	//0.货到付款 1.在线支付
	private int isAliPay = 0;
	//是否显示
	private int show = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_dish_list);
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		b = getIntent().getExtras();
		if(b!=null){
			orderId = b.getString("orderId");
			clearBuyCar = b.getInt("is_clear");
			payType = b.getInt("payType");
			isShowPay = b.getInt("isShowPay");
		}
		
		mImageMap = new HashMap<String, ImageView>();
		
		if(clearBuyCar==1){
			//购物完成，清空购物车信息
			BuyCar.deleteAll(ClientDBHelper.getInstance(this));
		}
		
		
		initView();
		initTopBar();
		initData();
	//	updateUIData();
		setListener();
	}
	
	
	private void setListener(){
	
		isPayTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(orderDetail.getAlipayState()==0){
				if(orderStateTv.getText().equals("订单取消")){
					showToast("订单已取消");
				}else{
					try {
						String info = getNewOrderInfo(orderDetail);
						String sign = Rsa.sign(info, Keys.PRIVATE);
						sign = URLEncoder.encode(sign);
						info += "&sign=\"" + sign + "\"&" + getSignType();
						Log.i("ExternalPartner", "start pay");
						// start the pay.
						Log.i("self", "info = " + info);

						final String orderInfo = info;
						new Thread() {
							public void run() {
								AliPay alipay = new AliPay(self, mHandler);
								
								//设置为沙箱模式，不设置默认为线上环境
								//alipay.setSandBox(true);

								String result = alipay.pay(orderInfo);

								Log.i("self", "result = " + result);
								Message msg = new Message();
								msg.what = 1;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						}.start();

					} catch (Exception ex) {
						ex.printStackTrace();
						Toast.makeText(self, R.string.remote_call_failed,
								Toast.LENGTH_SHORT).show();
					}
				}
				}
			}
				
		});
	}
	
	
	private String getNewOrderInfo(OrderDetail order) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(order.getOrderNo());
		sb.append("\"&subject=\"");
		sb.append("菜品订单");
		sb.append("\"&body=\"");
		sb.append("菜品商品详情");
		sb.append("\"&total_fee=\"");
		sb.append(order.getTotalPrice());
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(Config.GATEWAY_URL+"/alipay/alipay_appNotify"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d("self", "outTradeNo: " + key);
		return key;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case 1:
				String status = result.getResultStatus();
				/**
				 * 对签名进行验签
				 * 
				 */
				if(result.isSignOk()==false){
					Toast.makeText(self, "未支付成功",
							Toast.LENGTH_SHORT).show();
					showRedFont();
	//				isPayTv.setText("支付宝未完成支付,点击重新支付");
//					Intent intent = new Intent();
//					intent.setClass(self, OrderDishListActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("orderId", orderFinish.getId()+"");
//					bundle.putInt("payType", 2);
//					bundle.putInt("isShowPay", 1);
//					bundle.putInt("is_clear", 1);
//					intent.putExtras(bundle);
//					startActivity(intent);
//					finish();
				//	BaseApplication.mInstance.exit();
				}else{
					if("支付成功(9000)".equals(status)){
						Toast.makeText(self, "支付宝支付成功", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent();
//						intent.setClass(self, OrderDishListActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("orderId", orderFinish.getId()+"");
//						bundle.putInt("payType", 1);
//						bundle.putInt("isShowPay", 1);
//						bundle.putInt("is_clear", 1);
//						intent.putExtras(bundle);
//						startActivity(intent);
//						finish();
					//	BaseApplication.mInstance.exit();
						isPayTv.setText("恭喜你，支付成功");
							payStateTv.setTextColor(getResources().getColor(R.color.green));
							payStateTv.setText("已支付");
					}else{
						Toast.makeText(self, status, Toast.LENGTH_SHORT).show();
					}
				}
				
				break;
			default:
				break;
			}
		};
	};

	
	private void initData(){
		mOrderDetailRequest = new OrderDetailRequest();
		mOrderDetailRequest.setOrderId(Integer.parseInt(orderId));
		mOrderDetailRequest.setDk();
		mDishController.execute(Actions.ACTION_ORDER_DETAIL, mOrderDetailRequest);
		
	}
	
	private void updateUIData(){
		orderDetail = mDishController.getmOrderDetail();
		isAliPay = orderDetail.getAlipayType();
		//infoTv.setText(orderDetail.getExplain());
		//amountTv.setText("￥"+StringUtils.mathPrice(orderDetail.getTotalPrice()));
		amountTv.setText("￥"+orderDetail.getTotalPrice());
		orderNoTv.setText(getString(R.string.store_num,orderDetail.getOrderNo()));
		
		if(isAliPay==1){
			show = 1;
			isPayTv.setVisibility(View.VISIBLE);
		}else{
			show = 0;
			isPayTv.setVisibility(View.GONE);
		}
		
		if(orderDetail.getAlipayState()==1){
			isPayTv.setText("恭喜你，支付成功");
		}else if(orderDetail.getAlipayState()==0){
			showRedFont();
		//	isPayTv.setText("支付宝未完成支付,点击重新支付");
		}
		
		if(orderDetail.getAlipayState()==0){
			payStateTv.setTextColor(getResources().getColor(R.color.red));
			payStateTv.setText("未支付");
		}else if(orderDetail.getAlipayState()==1){
			payStateTv.setTextColor(getResources().getColor(R.color.green));
			payStateTv.setText("已支付");
		}
		
		orderStateTv.setTextColor(getResources().getColor(R.color.red));
		orderStateTv.setText(orderDetail.getStateName());
		
		vegTimeTv.setTextColor(getResources().getColor(R.color.red));
		vegTimeTv.setText(orderDetail.getSendTime());
		
		orderTimeTv.setText(getString(R.string.ordering_time,orderDetail.getOrderTime()));
		if(StringUtils.isEmpty(orderDetail.getStorePhone())){
			storePhoneTv.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}else{
			storePhoneTv.setText(getString(R.string.store_phone,orderDetail.getStorePhone()));
		}
	
		
		String url = orderDetail.getTwoDimensionCode();
		
		if(!StringUtils.isEmpty(url)){
			SSYImageCache.getInstance().downloadImage(mImageMap, twoImg,
					url, false, new ImageCallback() {

						@Override
						public void onImageLoaded(Bitmap bitmap, String imageUrl) {
							mImageMap.get(imageUrl).setImageBitmap(bitmap);

						}
					});
			
		}else{
			twoImg.setImageResource(R.drawable.defaule_img);
		}
		
		if(orderDetail.getDistributionType()==0){
			sendTv.setText(getString(R.string.send_info,"送货上门"));
		}else if(orderDetail.getDistributionType()==1){
			sendTv.setText(getString(R.string.send_info,"取菜点自提"));
		}
		
		nameTv.setText(getString(R.string.address_username,orderDetail.getUserName()));
		phoneTv.setText("电话:"+orderDetail.getUserPhone());
		addressTv.setText("收货地址:"+orderDetail.getUserAddress());
		list = orderDetail.getDishList();
		
		if (list.size() == 0) {
			morderpageList.noDataFinishNoScroll();
			morderpageList.setVisibility(View.GONE);
		//	mNoReleaseLayout.setVisibility(View.VISIBLE);
			return;
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
		
		morderpageList.setHiddenFooterViewForScroll();
		if (morderpageList.getAdapter() == null) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			morderpageAdapter = new OrderResultAdapter(self, list);
			morderpageList.setAdapter(morderpageAdapter);
		} else {
			morderpageAdapter = (OrderResultAdapter) morderpageList.getAdapter();
			morderpageAdapter.addListItems(list);

		}
	}
	
	private void initView(){
		mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.order_result_info, null);
		morderpageList = (ClimbListView)findViewById(R.id.order_dish_list);
		
		morderpageList.initFooterView();
		morderpageList.setUpLoadListener(this);
		morderpageList.setOnItemClickListener(this);
		morderpageList.addHeaderView(mHeadView);
		
		amountTv = (TextView)mHeadView.findViewById(R.id.order_price);
		isPayTv = (TextView)mHeadView.findViewById(R.id.pay_types);
		if(isShowPay==1){
			show = 1;
			isPayTv.setVisibility(View.VISIBLE);
			if(payType==1){
				isPayTv.setText("恭喜你，支付成功");
			}else if(payType==2){
				showRedFont();
			}
		}else if(isShowPay==0){
			show = 0;
			isPayTv.setVisibility(View.GONE);
		}
		
		
		line = (View)mHeadView.findViewById(R.id.line_store_phone);
		orderNoTv = (TextView)mHeadView.findViewById(R.id.order_finish_num);
		payStateTv = (TextView)mHeadView.findViewById(R.id.order_pay_state);
		orderStateTv = (TextView)mHeadView.findViewById(R.id.order_states);
		vegTimeTv = (TextView)mHeadView.findViewById(R.id.order_finish_time);
		orderTimeTv = (TextView)mHeadView.findViewById(R.id.ordering_time);
		storePhoneTv = (TextView)mHeadView.findViewById(R.id.order_store_phone);
		twoImg = (ImageView)mHeadView.findViewById(R.id.two_ma);
		nameTv = (TextView)mHeadView.findViewById(R.id.order_info_user);
		phoneTv = (TextView)mHeadView.findViewById(R.id.order_info_phone);
		addressTv = (TextView)mHeadView.findViewById(R.id.order_info_content);
		sendTv = (TextView)mHeadView.findViewById(R.id.address_type_order_send);
	}
	
	private void showRedFont(){
		String aa = "支付宝未完成支付,点击重新支付";
		SpannableStringBuilder builder = new SpannableStringBuilder(aa); 
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
		builder.setSpan(redSpan, 11, aa.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		isPayTv.setText(builder);
	}
	
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				if(clearBuyCar==1)
				BaseApplication.mInstance.exit();
			}
		});
	}
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ORDER_DETAIL:
			updateUIData();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		int num = 0;
			num = position - 2;

		if(num>=0){
			DishesOrderFinish t = morderpageAdapter.getItem(num);
			Intent intent = new Intent();
			intent.setClass(self, DishDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("dishId", t.getDishId());
            bundle.putInt("isShowButtom",1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	
		
//		mDishStatisticsRequest = new DishStatisticsRequest();
//		mDishStatisticsRequest.setSource(2);
//		mDishStatisticsRequest.setDishId(Integer.parseInt(t.getDishId()));
//		mDishStatisticsRequest.setHasProcessDialog("0");
//		mDishStatisticsRequest.setDk();
//		mDishController.execute(Actions.ACTION_DISH_SC, mDishStatisticsRequest);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(clearBuyCar==1){
				
				BaseApplication.mInstance.exit();
			}
			finish();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}

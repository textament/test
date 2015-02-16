/**
 * 
 */
package com.shanshengyuan.client.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseApplication;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.Config;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.ArrviedVolumeUseAdapter;
import com.shanshengyuan.client.adapter.DiaLogAdapter;
import com.shanshengyuan.client.adapter.DiaLogStreetAdapter;
import com.shanshengyuan.client.adapter.DiaLogXiaoQuAdapter;
import com.shanshengyuan.client.adapter.OrderPageAdapter;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.AddressListRequest;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.model.address.VerificationCodeRequest;
import com.shanshengyuan.client.model.arrviedVolume.ArrviedVolumeRequest;
import com.shanshengyuan.client.model.arrviedVolume.arrviedVolume;
import com.shanshengyuan.client.model.dish.DishesOrder;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;
import com.shanshengyuan.client.model.order.Order;
import com.shanshengyuan.client.model.order.OrderFinish;
import com.shanshengyuan.client.model.order.OrderFinishRequest;
import com.shanshengyuan.client.model.order.OrderRequest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.ClimbListView.UpLoadListener;
import com.shanshengyuan.client.utils.ConvertUtils;
import com.shanshengyuan.client.utils.StringUtils;
import com.shanshengyuan.client.utils.alipay.Keys;
import com.shanshengyuan.client.utils.alipay.Result;
import com.shanshengyuan.client.utils.alipay.Rsa;
import com.shanshengyuan.client.widgets.DatePicker;
import com.shanshengyuan.client.widgets.TimePicker;

/**
 * @author lihao
 *
 */
public class OrderPageActivity extends BaseActivity implements
		OnResultListener,OnItemClickListener,OnClickListener,UpLoadListener,OnKeyListener{
	private static final int MAP_DATA = 1;
	OrderPageActivity self = null;
	
	// 倒计时
	private static final int READY_TIMES = 60000;
	// 时间间隔
	private static final int GAP_TIMES = 1000;
	CountDownTimer c;
	
	View mBack;
	
	// 列表
	private ClimbListView morderpageList;
	private OrderPageAdapter morderpageAdapter;
	
	private View mHeadView;
	private LayoutInflater mInflater;
	
	List<DishesOrder> list;
	//取菜时间
	private TextView  timeEd;
	private AlertDialog dialog;
	private Button sure;
	private TextView resultView;
	//总价
	private TextView totalPriceTv;
	
	DatePicker datePicker;
	TimePicker timePicker;
	Calendar mCalendar;
	
	//没有用户地址
	private LinearLayout isShowAddressLy;
	//有用户地址
	private LinearLayout haveAddressLy;
//	private TextView addUserTv;
//	private TextView addPhoTv;
//	private TextView addCon;
	//编辑信息
	private EditText userNameEdit;
	private EditText userPhoneEdit;
	private EditText userAddressEdit;
	
	//说明
	private TextView explainTv;
	//付款方式
	private TextView payTypeTv;
	
	
	
	//地址信息
	private AddressListRequest mAddressListRequest;
	private AddressController mAddressController;
	//结算信息
	private OrderRequest mOrderRequest;
	private DishController mDishController;
	//下订单
	private OrderFinishRequest mOrderFinishRequest;
	//验证请求
	private VerificationCodeRequest mVerificationCodeRequest;
	
	
	private Order order;
	
	private List<String> timeList=null;
	
	private String[]timeStr = null;
	
	private OrderFinish orderFinish;
	//地址id
	private int addId = 0;
	String dishIds = "";
	private boolean flagData = false;
	//判断是否又地址，从而不验证
	private boolean isAddress = true;
	private String didStr;
	Bundle b=null;
	String addressId;
	String username;
	String phone;
	String address;
    int mealId = 0;
    int useTicked = 0;
	int addType=-1;
	int isYanz = -1;
	
	//有地址跳转其他地址
	private TextView mJmpOtherAddressTv;
	private TextView mAddressTypeTv;
	private TextView mUserNameTv;
	private TextView mPhoneTv;
	private TextView mAddressTv;
	
	//支付形式
	private RelativeLayout aliPayLy;
	private ImageView aliImg;
	private RelativeLayout gohomeLy;
	private ImageView homeImg;
	private int payType = 1;
	
	
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
	private Button yanzBtn;
	private LinearLayout yanzShowLy;
	private EditText yanzEd;
	//顶部
	private LinearLayout topShowLy;
	
	StoreDishPoint sdp = null;
	
	List<StoreDishPoint> sdlist = null;
	
	ArrayList<StoreDishPoint> listObj = null;
	
	private AlertDialog addressdialog;
	
	ListView mlistView = null;
	DiaLogAdapter adapter = null;
	
	List<String> xiaoquList;
	List<String> streetList;
	private ListView xiaoquView;
	private ListView streetListView;
    private AlertDialog redDialog;
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
	//验证码校验值
	private int noteId=-1;
	//配送费
	private TextView sendAmountTv;
	//是否今日配送
	boolean isToday = false;
	//明日下单时间字符串
	String tomrrowStr;


    //底部抵扣卷
   private LinearLayout volumeShowLy;
   private TextView mShowHideTv;
   private ListView mVolList;
   private View footerView;
   private ArrviedVolumeUseAdapter arrviedVolumeUseAdapter;

    private ArrviedVolumeRequest arrviedVolumeRequest;
    private List<arrviedVolume> arrviedVolumeList;
    private boolean isOpen = false;
    float totalamount= 0;
    float volamount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		self = this;
		super.onCreate(savedInstanceState);
		BaseApplication.mInstance.addActivity(this);
		setContentView(R.layout.activity_order_page);
		if(mAddressController==null){
			mAddressController = new AddressController(self, self);
		}
		if(mDishController==null){
			mDishController = new DishController(self, self);
		}
		b = getIntent().getExtras();
		if(b!=null){
			didStr = b.getString("didStr");
            int id = b.getInt("mealid");
            if(id!=0)
              mealId = b.getInt("mealid");
		}
		getData();
		initView();
		initTopBar();
		
		initData();
		setListener();
	}
	
	protected void dialog() {
		new AlertDialog.Builder(self).setTitle("提示")
				.setMessage("输入数据将被清除，是否退出？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	
	private void setListener(){
		
		aliPayLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					payType=1;
					aliImg.setVisibility(View.VISIBLE);
					homeImg.setVisibility(View.GONE);
					
					if(isToday){
						float price = Float.parseFloat(mathPrice(order.getTotalPrice()))+order.getTheSameDaySendCost();

                        float total = price - volamount ;
                        if(total<0){
                            total = 0;
                        }
						totalPriceTv.setText("￥"+total);
                        totalamount =Float.parseFloat(mathPrice(order.getTotalPrice()))+order.getTheSameDaySendCost();

					}else{
                        float total = Float.parseFloat(mathPrice(order.getTotalPrice())) - volamount;
                        if(total<0){
                            total = 0;
                        }
						totalPriceTv.setText("￥"+total);
                        totalamount =  Float.parseFloat(mathPrice(order.getTotalPrice())) ;
					}
					
					
			}
		});
		
		gohomeLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				payType=0;
				aliImg.setVisibility(View.GONE);
				homeImg.setVisibility(View.VISIBLE);
				
				if(isToday){
					float price = Float.parseFloat(order.getTotalPrice())+order.getTheSameDaySendCost();
                    float total = price - volamount ;
                    if(total<0){
                        total = 0;
                    }

					totalPriceTv.setText("￥"+total+"");
                    totalamount =Float.parseFloat(order.getTotalPrice())+order.getTheSameDaySendCost();
                  //  totalamount = price;
				}else{
                    float total = Float.parseFloat(order.getTotalPrice()) - volamount ;
                    if(total<0){
                        total = 0;
                    }
					totalPriceTv.setText("￥"+total);
                    totalamount =  Float.parseFloat(order.getTotalPrice());
                   // totalamount = Float.parseFloat(order.getTotalPrice());
				}
				
				
			}
		});
		
		yanzBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

//				
//				if(StringUtils.isPhoneNumber(yanzEd.getText().toString())){
//					showToast("请输入正确的手机号");
//					return;
//				}
				//电话
				String sUserPhone = "";
				if(isAddress){
					sUserPhone = userPhoneEdit.getText().toString();
				}else{
					sUserPhone = phone;
				}
				

				if (!StringUtils.isPhoneNumber(sUserPhone)) {
					Toast.makeText(self, "请输入手机号码！",
							Toast.LENGTH_LONG).show();
					return ;
				}

				if (StringUtils.isPhoneNumber(sUserPhone)==false) {
					Toast.makeText(self, "请输入正确的手机号码！",
							Toast.LENGTH_LONG).show();
					return ;
				}
				
				mVerificationCodeRequest = new VerificationCodeRequest();
				mVerificationCodeRequest.setPhone(sUserPhone);
				mVerificationCodeRequest.setDk();
				mAddressController.execute(Actions.ACTION_VER_CODE, mVerificationCodeRequest);
				
			}
		});
		
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
					addressdialog = new AlertDialog.Builder(self).create();
					View addressview = View.inflate(self, R.layout.dialog_list, null);
					addressdialog.setView(addressview, 0, 0, 0, 0);
					addressdialog.getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					mlistView = (ListView) addressview.findViewById(R.id.listview);
					if(sdlist==null){
						sdlist = new ArrayList<StoreDishPoint>();
					}
					adapter = new DiaLogAdapter(self,sdlist);
					// adapter.removeAllListData();
					 mlistView.setAdapter(adapter);
					 mlistView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							StoreDishPoint t = (StoreDishPoint) mlistView.getAdapter().getItem(position);
							sdp = t;
							storeId = t.getStoreId();
							addressEd.setText(t.getAddress());
							addressdialog.hide();
						}
					});
					 addressdialog.show();
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
		
		  gohomeTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					storeLy.setVisibility(View.GONE);
					homeLy.setVisibility(View.VISIBLE);
					xiangxiEd.setVisibility(View.VISIBLE);
					gohomeTv.setBackgroundResource(R.drawable.settlement_door_to_door_delivery_s);
					goStoreTv.setBackgroundResource(R.drawable.settlement_since_the_mention_s);
					choosePage =0;
					payType=1;
					aliImg.setVisibility(View.VISIBLE);
					homeImg.setVisibility(View.GONE);
					gohomeLy.setVisibility(View.VISIBLE);
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
					payType=1;
					aliImg.setVisibility(View.VISIBLE);
					homeImg.setVisibility(View.GONE);
					gohomeLy.setVisibility(View.GONE);
				}
			});
		
		

//		payTypeTv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				try {
//					String info = getNewOrderInfo(order);
//					String sign = Rsa.sign(info, Keys.PRIVATE);
//					sign = URLEncoder.encode(sign);
//					info += "&sign=\"" + sign + "\"&" + getSignType();
//					Log.i("ExternalPartner", "start pay");
//					// start the pay.
//					Log.i("self", "info = " + info);
//
//					final String orderInfo = info;
//					new Thread() {
//						public void run() {
//							AliPay alipay = new AliPay(self, mHandler);
//							
//							//设置为沙箱模式，不设置默认为线上环境
//							//alipay.setSandBox(true);
//
//							String result = alipay.pay(orderInfo);
//
//							Log.i("self", "result = " + result);
//							Message msg = new Message();
//							msg.what = 1;
//							msg.obj = result;
//							mHandler.sendMessage(msg);
//						}
//					}.start();
//
//				} catch (Exception ex) {
//					ex.printStackTrace();
//					Toast.makeText(self, R.string.remote_call_failed,
//							Toast.LENGTH_SHORT).show();
//				}
//				
//			}
//		});
		
		haveAddressLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(self, AddressActivity.class);
				startActivity(intent);
			}
		});
		
		
		resultView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					resultOrder();
			}
		});
		
		timeEd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			//	dialog.show();
				new AlertDialog.Builder(self)
				.setTitle("请选择时间")
				.setItems(timeStr,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						timeEd.setText(timeStr[arg1]);
						if(timeStr[arg1].contains("今日")){
							isToday =true;
							sendAmountTv.setVisibility(View.VISIBLE);
                            float price = Float.parseFloat(mathPrice(order.getTotalPrice()))+order.getTheSameDaySendCost();
							sendAmountTv.setText("("+order.getTheSameDaySendName()+":"+order.getTheSameDaySendCost()+")");
							if(payType==1){
                                float total = price - volamount ;
                                if(total<0){
                                    total = 0;
                                }
								totalPriceTv.setText("￥"+total);
                                totalamount = Float.parseFloat(mathPrice(order.getTotalPrice()))+order.getTheSameDaySendCost();
                               // totalamount = Float.parseFloat(mathPrice(price+""));
							}else if(payType==0){
                                float total =  Float.parseFloat(order.getTotalPrice()) - volamount ;
                                if(total<0){
                                    total = 0;
                                }
								totalPriceTv.setText("￥"+total+"");
                                totalamount =  Float.parseFloat(order.getTotalPrice());
							}
						
						}else{
							isToday =false;
							sendAmountTv.setVisibility(View.GONE);
							if(payType==1){
                                float total = Float.parseFloat(mathPrice(order.getTotalPrice())) - volamount ;
                                if(total<0){
                                    total = 0;
                                }
								totalPriceTv.setText("￥"+total);
                                totalamount = Float.parseFloat(mathPrice(order.getTotalPrice()+""));
							}else if(payType==0){
                                float total = Float.parseFloat(order.getTotalPrice()) - volamount ;
                                if(total<0){
                                    total = 0;
                                }
								totalPriceTv.setText("￥"+total+"");
                                totalamount = Float.parseFloat(order.getTotalPrice()+"");
							}
						}
					}
				}).show();
			}
		});
		
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mCalendar.set(Calendar.YEAR, datePicker.getYear());
				mCalendar.set(Calendar.MONTH, datePicker.getMonth());
				mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
				mCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHourOfDay());
				mCalendar.set(Calendar.MINUTE, timePicker.getMinute());
				mCalendar.set(Calendar.SECOND, 0);
				timeEd.setText(mCalendar.getTime().toLocaleString());
				dialog.dismiss();
			}
		});
	}
	
	@Override
	protected void onResume() {
		if(BaseApplication.isAddressFlash){
			initData();
			BaseApplication.isAddressFlash = false;
		}
		super.onResume();
	}
	
	private String getNewOrderInfo(Order order) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderFinish.getOrderNo());
		sb.append("\"&subject=\"");
		sb.append("菜品订单");
		sb.append("\"&body=\"");
		sb.append("菜品商品详情");
		sb.append("\"&total_fee=\"");
		sb.append(orderFinish.getTotalPrice());
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
					Intent intent = new Intent();
					intent.setClass(self, OrderDishListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("orderId", orderFinish.getId()+"");
					bundle.putInt("payType", 2);
					bundle.putInt("isShowPay", 1);
					bundle.putInt("is_clear", 1);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				//	BaseApplication.mInstance.exit();
				}else{
					if("支付成功(9000)".equals(status)){
						Toast.makeText(self, "支付宝支付成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.setClass(self, OrderDishListActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("orderId", orderFinish.getId()+"");
						bundle.putInt("payType", 1);
						bundle.putInt("isShowPay", 1);
						bundle.putInt("is_clear", 1);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					//	BaseApplication.mInstance.exit();
						
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
        arrviedVolumeRequest = new ArrviedVolumeRequest();
        arrviedVolumeRequest.setDk();
        mAddressController.execute(Actions.ACTION_ARR_VOL_ISUSE,arrviedVolumeRequest);

//		list = new ArrayList<Dishes>();
		mAddressListRequest = new AddressListRequest();
		mAddressListRequest.setDk();
		mAddressController.execute(Actions.ACTION_ADDRESS_LIST, mAddressListRequest);
		if(StringUtils.isEmpty(didStr)){
			String s = "";
			List<BuyCar> buyCarList = BuyCar.getBuyCar(null, null,
					ClientDBHelper.getInstance(this));
			if(buyCarList!=null){
				if(buyCarList.size()!=0){
					for (int i = 0; i < buyCarList.size(); i++) {
						s += "id="+ buyCarList.get(i).getShopId()+"|num="+ buyCarList.get(i).getShopNum()+"@";
					}
				}
			}
			dishIds = s.substring(0, s.lastIndexOf("@"));
		}else{
			dishIds = didStr;
		}
	
		mOrderRequest = new OrderRequest();
		mOrderRequest.setDishes(dishIds);

        if(mealId!=0){
            mOrderRequest.setMealId(mealId);
        }

		mOrderRequest.setDk();
		mDishController.execute(Actions.ACTION_DISH_ORDER, mOrderRequest);
//		for (int i = 0; i < 10; i++) {
//			Dishes d = new Dishes();
//			d.setAmount(String.valueOf((1+i)));
//			d.setContent("这是测试内容"+i);
//			d.setPic_url("");
//			d.setTitleName("菜品"+i);
//			d.setType("菜品类型"+i);
//			d.setNum("1");
//			list.add(d);
//		}
		
	}
	
	private void updateAddressData(){
		List<Address> addressList = mAddressController.getmAddressList();
		
		if(addressList!=null){
			if(addressList.size()!=0){
				for (int i = 0; i < addressList.size(); i++) {
					if("1".equals(addressList.get(i).getIsDefault())){
						username = addressList.get(i).getName();
						phone = addressList.get(i).getPhone();
						address = addressList.get(i).getAddress();
						addressId = addressList.get(i).getId();
						storeId = addressList.get(i).getStoreId();
						mUserNameTv.setText(getString(R.string.address_username,addressList.get(i).getName()));
						mPhoneTv.setText(getString(R.string.address_phone, addressList.get(i).getPhone()));
						mAddressTv.setText(getString(R.string.address_order,addressList.get(i).getAddress()));
						addType = addressList.get(i).getType();
						isYanz = addressList.get(i).getIsNoteVerify();
//						if(0==addressList.get(i).getType()){
//							mAddressTypeTv.setText("送货上门");
//							choosePage = 0;
//							gohomeLy.setVisibility(View.VISIBLE);
//						}else if(1==addressList.get(i).getType()){
							mAddressTypeTv.setText("取菜点自提");
							choosePage = 1;
					//		gohomeLy.setVisibility(View.GONE);
//						}
						break;
					}
				}
				isAddress = false;
				isShowAddressLy.setVisibility(View.GONE);
				haveAddressLy.setVisibility(View.VISIBLE);
				topShowLy.setVisibility(View.GONE);
			}else{
				//isYanz = 0;
				isYanz = 1;
				isAddress = true;
				isShowAddressLy.setVisibility(View.VISIBLE);
				haveAddressLy.setVisibility(View.GONE);
				topShowLy.setVisibility(View.VISIBLE);
			}
		}else{
			//isYanz = 0;
			isYanz = 1;
//			if(choosePage==1){
//				gohomeLy.setVisibility(View.GONE);
//			}else{
//				
//			}
			isAddress = true;
			isShowAddressLy.setVisibility(View.VISIBLE);
			haveAddressLy.setVisibility(View.GONE);
			topShowLy.setVisibility(View.VISIBLE);
		}
		
		if(isYanz==0){
			yanzShowLy.setVisibility(View.VISIBLE);
		}else if(isYanz==1){
			yanzShowLy.setVisibility(View.GONE);
		}
	}
	
	private void updateAddress(){
		  
	}

    private void isShowHongBao(){
        orderFinish = mDishController.getmOrderFinish();
        if(orderFinish.getIsSupportRedPacket()==0){
            updateFinish();

        }else if(orderFinish.getIsSupportRedPacket()==1){
            redDialog = new AlertDialog.Builder(self).setCancelable(false).create();
            View views = View.inflate(self, R.layout.dialog_ssy, null);
            redDialog.setView(views, 0, 0, 0, 0);
            redDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Button sureBtn = (Button) views.findViewById(R.id.sure_btn);
            sureBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    redDialog.hide();
                    updateFinish();
                }
            });
            redDialog.show();
        }
    }
	
	private void updateFinish(){
        orderFinish = mDishController.getmOrderFinish();

//		Intent intent = new Intent();
//		intent.setClass(self, OrderResultActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("orderFinish", orderFinish);
//		intent.putExtras(bundle);
//		startActivity(intent);




		
		if(payType==1){
			showToast("订单生成成功，跳转支付页面。。。");
			//支付宝支付
			try {
				String info = getNewOrderInfo(order);
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
		}else if(payType==0){

			showToast("订单生成成功");
			Intent intent = new Intent();
			intent.setClass(self, OrderDishListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("orderId", orderFinish.getId()+"");
			bundle.putInt("payType", 0);
			bundle.putInt("isShowPay", 0);
			bundle.putInt("is_clear", 1);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		//	BaseApplication.mInstance.exit();
		}
	}
	
	private void validateData(){
		String user = userNameEdit.getText().toString();
		String sUserPhone = userPhoneEdit.getText().toString();
	//	String userAddress = userAddressEdit.getText().toString();
		String time = timeEd.getText().toString();
		if(!StringUtils.isEmpty(user)||!StringUtils.isEmpty(sUserPhone)){
			flagData = true;
		}else{
			flagData = false;
		}
	}
	
	private boolean vilidate(){
		boolean result = true;
		//验证姓名
		String user = userNameEdit.getText().toString();
		if(StringUtils.isEmpty(user)){
			Toast.makeText(self, "请输入取菜人姓名！", Toast.LENGTH_LONG).show();
			result = false;
			return result;
		}
		
		//电话
		String sUserPhone = userPhoneEdit.getText().toString();

		if (StringUtils.isEmpty(sUserPhone)) {
			Toast.makeText(self, "请输入手机号码！",
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
		
		//验证码
		if(isYanz==0){
			if(StringUtils.isEmpty(yanzEd.getText().toString())){
				showToast("请输入验证码");
				result = false;
				return result;
			}
		}
		
		
		
		//取菜时间
		String time = timeEd.getText().toString();
		if(time.contains("取菜时间")){
			Toast.makeText(self, "请选择取菜时间！", Toast.LENGTH_LONG).show();
			result = false;
			return result;
		}
		
		return result;
	}
	
	private void updateUIData(){
		
		order = mDishController.getmOrder();
		if(order!=null){
			//用户地址信息
			sdlist = order.getStoreDishPointList();
			if(sdlist!=null){
				listObj = new ArrayList<StoreDishPoint>();
				for (int i = 0; i < sdlist.size(); i++) {
						 StoreDishPoint s = new StoreDishPoint();
						 s.setStoreId(sdlist.get(i).getStoreId());
						 s.setAddress(sdlist.get(i).getAddress());
						 s.setPhone(sdlist.get(i).getPhone());
						 s.setPointXY(sdlist.get(i).getPointXY());
						 listObj.add(s);
				}
			}
			
			
			//取菜时间
			timeList = order.getFetchDishTimeList();
			if(timeList!=null){
				timeStr = new String[timeList.size()];
				for (int i = 0; i < timeList.size(); i++) {
					timeStr[i] = timeList.get(i);
					if(timeList.get(i).contains("明日")){
						tomrrowStr = timeList.get(i);
					}
				}
			}
			
			//文字说明
			explainTv.setText("说明:"+order.getExplain());

			totalPriceTv.setText("￥"+mathPrice(order.getTotalPrice()));
            totalamount = Float.parseFloat(mathPrice(order.getTotalPrice()));
			list = new ArrayList<DishesOrder>();
			list = order.getDishList();
			
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
			if (morderpageList.getAdapter() == null) {
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				morderpageAdapter = new OrderPageAdapter(self, list);
				morderpageList.setAdapter(morderpageAdapter);
			} else {
				morderpageAdapter = (OrderPageAdapter) morderpageList.getAdapter();
				morderpageAdapter.removeAllListData();
				morderpageAdapter.addListItems(list);

			}
		}

	}
	
	private String mathPrice(String price){
		 float aa = (float) ((Float.parseFloat(price))*0.98);
		 int   scale   =   1;//设置位数  
		 int   roundingMode   =  0;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
		 BigDecimal   bd   =   new   BigDecimal((double)aa);  
		 bd   =   bd.setScale(scale,BigDecimal.ROUND_HALF_UP);
		 aa   =   bd.floatValue(); 
		 return aa+"";
	}
	
	  private List<String> getData(){
	         
		  xiaoquList = new ArrayList<String>();
		  xiaoquList.add("请选择");
		  xiaoquList.add("鲁能新城");
		  xiaoquList.add("其他小区");
         
        return xiaoquList;
    }
	
	private void initView(){
		mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeadView = mInflater.inflate(R.layout.order_page_address, null);
		morderpageList = (ClimbListView)findViewById(R.id.order_page_list);
	
		morderpageList.initFooterView();
		morderpageList.setUpLoadListener(this);
		morderpageList.setOnItemClickListener(this);
		morderpageList.addHeaderView(mHeadView);
		
		timeEd = (TextView)mHeadView.findViewById(R.id.order_time_et);
		mCalendar = Calendar.getInstance();
		
		resultView = (TextView)findViewById(R.id.order_page);
		totalPriceTv = (TextView)findViewById(R.id.result_total);
		sendAmountTv = (TextView)findViewById(R.id.result_send_amount);
		isShowAddressLy = (LinearLayout)mHeadView.findViewById(R.id.is_address_show_ly);
		haveAddressLy = (LinearLayout)mHeadView.findViewById(R.id.is_address_show);
		userNameEdit = (EditText)mHeadView.findViewById(R.id.order_username_et);
		userPhoneEdit = (EditText)mHeadView.findViewById(R.id.order_phone_et);
		//userAddressEdit = (EditText)mHeadView.findViewById(R.id.order_address_et);
		explainTv = (TextView)mHeadView.findViewById(R.id.explain_tv);
		//payTypeTv = (TextView)mHeadView.findViewById(R.id.pay_type_tv);
		//地址
		storeLy = (LinearLayout)mHeadView.findViewById(R.id.go_type_order_1);
		homeLy = (LinearLayout)mHeadView.findViewById(R.id.go_type_order_2);
		gohomeTv = (TextView)mHeadView.findViewById(R.id.type_order_2);
		goStoreTv = (TextView)mHeadView.findViewById(R.id.type_order_1);
		addressEd = (TextView)mHeadView.findViewById(R.id.order_et1);
		mapImg = (TextView)mHeadView.findViewById(R.id.go_order_map);
		xiaoquEd = (TextView)mHeadView.findViewById(R.id.order_xiaoqu);
		streetEd = (TextView)mHeadView.findViewById(R.id.order_street);
		xiangxiEd = (EditText)mHeadView.findViewById(R.id.order_xiangxi);
		
		//验证
		yanzBtn = (Button)mHeadView.findViewById(R.id.order_yanz);
		yanzShowLy = (LinearLayout)mHeadView.findViewById(R.id.is_yanz_show);
		yanzEd = (EditText)mHeadView.findViewById(R.id.order_yanz_ed);
		
		//支付形式
		aliPayLy = (RelativeLayout)mHeadView.findViewById(R.id.pay_one);
		aliImg = (ImageView)mHeadView.findViewById(R.id.pay_order_1);
		gohomeLy = (RelativeLayout)mHeadView.findViewById(R.id.pay_two);
		homeImg = (ImageView)mHeadView.findViewById(R.id.pay_order_2);
		
		//有地址内容
		mJmpOtherAddressTv = (TextView)mHeadView.findViewById(R.id.other_get_order);
		mAddressTypeTv = (TextView)mHeadView.findViewById(R.id.address_type_order);
		mUserNameTv = (TextView)mHeadView.findViewById(R.id.address_name_order);
		mPhoneTv = (TextView)mHeadView.findViewById(R.id.address_phone_order);
		mAddressTv = (TextView)mHeadView.findViewById(R.id.address_content_order);
		
		topShowLy = (LinearLayout)mHeadView.findViewById(R.id.order_ed_show);
	
		
		
		dialog = new AlertDialog.Builder(self).create();
		View view = View.inflate(self, R.layout.dialog_contact, null);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		sure = (Button) view.findViewById(R.id.get_time_btn);
		datePicker = (DatePicker)view.findViewById(R.id.datePicker);
		timePicker = (TimePicker)view.findViewById(R.id.timePicker);
		
		
		 
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
	}
	
	//验证
	private void countDown() {
		c = new CountDownTimer(READY_TIMES, GAP_TIMES) {
			@Override
			public void onTick(long millisUntilFinished) {
				yanzBtn.setText((millisUntilFinished / GAP_TIMES) + "秒");
				yanzBtn.setEnabled(false);
			}

			@Override
			public void onFinish() {
				yanzBtn.setText("获取验证码");
				yanzBtn.setEnabled(true);
			}
		};
		c.start();
	}
	
	
	private void initTopBar() {
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAddress)
					validateData();
				if (flagData) {
					dialog();
				} else {
					finish();
					
				}
			}
		});
	}
	
	@Override
	public void onSuccess(int actionType) {
		switch (actionType) {
		case Actions.ACTION_ADDRESS_LIST:
			updateAddressData();
			break;
		case Actions.ACTION_DISH_ORDER:
			updateUIData();
			break;
		case Actions.ACTION_DISH_ORDER_FINISH:
            isShowHongBao();
			break;
		case Actions.ACTION_VER_CODE:
			showToast("验证码已发送");
			noteId = mAddressController.getmVerificationCode().getNoteId();
			countDown();
			break;
        case Actions.ACTION_ARR_VOL_ISUSE:
            arrviedVolumeList = mAddressController.getArrviedVolumes();
            if(arrviedVolumeList!=null){
                if(arrviedVolumeList.size()!=0){
                   footerView =  mInflater.inflate(R.layout.order_page_address_footer, null);
                    volumeShowLy = (LinearLayout)footerView.findViewById(R.id.vol_show_ly);
                    mShowHideTv = (TextView)footerView.findViewById(R.id.vol_btn);
                    mVolList = (ListView)footerView.findViewById(R.id.listView);
                    mVolList.setVisibility(View.VISIBLE);
                    int height = 50;
                    for (int i = 0;i<arrviedVolumeList.size();i++){
                        arrviedVolumeList.get(i).setChecked(0);
                    }
                    height = height*(arrviedVolumeList.size()+1);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(self,height) );

                    mVolList.setLayoutParams(layoutParams);

                    morderpageList.addFooterView(footerView);
                    arrviedVolumeUseAdapter = new ArrviedVolumeUseAdapter(self, arrviedVolumeList,OrderPageActivity.this);
                    mVolList.setAdapter(arrviedVolumeUseAdapter);

//                    mShowHideTv.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(isOpen){
//                                mShowHideTv.setBackgroundResource(R.drawable.close);
//                                mVolList.setVisibility(View.GONE);
//                                volamount = 0;
//                                float total = totalamount - volamount ;
//                                    totalPriceTv.setText("￥"+total);
//
//                                useTicked = -1;
//                                isOpen = false;
//                            }else if(isOpen==false){
//                                mShowHideTv.setBackgroundResource(R.drawable.open);
//                                mVolList.setVisibility(View.VISIBLE);
//                                float total =totalamount - volamount;
//                                    totalPriceTv.setText("￥"+total);
//
//                                for (int i=0;i<arrviedVolumeList.size();i++){
//                                    arrviedVolumeList.get(i).setChecked(0);
//                                }
//                                if(arrviedVolumeUseAdapter!=null){
//                                    arrviedVolumeUseAdapter = (ArrviedVolumeUseAdapter) mVolList.getAdapter();
//                                    arrviedVolumeUseAdapter.updateData(arrviedVolumeList);
//                                }
//
//                                useTicked = -1;
//                                isOpen = true;
//                            }
//                        }
//                    });
                }
            }
            break;
		default:
			break;
		}
	}
	
	@Override
	public void onFailed(int actionType, BaseResponse result) {
		if(actionType==Actions.ACTION_DISH_ORDER_FINISH){
			int failNum = result.getFailType();
			if(failNum==1){
			 	new AlertDialog.Builder(self) 
			 	.setTitle("消息提示")
			 	.setMessage(result.getMsg())
			 	.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(!StringUtils.isEmpty(tomrrowStr)){
							timeEd.setText(tomrrowStr);
							resultOrder();
							
						}
					}
				})
			 	.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
			 	.show();
			}
		}
		super.onFailed(actionType, result);
	}
	
	private void resultOrder(){
		if(isAddress){
			boolean flag = vilidate();
			if(flag){
				mOrderFinishRequest = new OrderFinishRequest();
				if(addId!=0){
					mOrderFinishRequest.setId(addId);
				}
				mOrderFinishRequest.setName(userNameEdit.getText().toString());
				mOrderFinishRequest.setPhone(userPhoneEdit.getText().toString());
				
				 if(choosePage==0){
					  String xiaoqu = xiaoquEd.getText().toString();
					  String street = streetEd.getText().toString();
					  String xiangxi = xiangxiEd.getText().toString();
					  mOrderFinishRequest.setAddress(xiaoqu+street+xiangxi);
				  }else if(choosePage==1){
					  String addstr = addressEd.getText().toString();
					  mOrderFinishRequest.setAddress(addstr);
					  mOrderFinishRequest.setStoreId(storeId);
				  }
				
				//验证码
					if(isYanz==0){
							mOrderFinishRequest.setVerify(yanzEd.getText().toString());
							mOrderFinishRequest.setNoteId(noteId);
					}
					
				mOrderFinishRequest.setDistributionType(choosePage);
				mOrderFinishRequest.setAlipayType(payType);
				mOrderFinishRequest.setFetchDishTime(timeEd.getText().toString());
				mOrderFinishRequest.setDishes(dishIds);
                if(mealId!=0){
                    mOrderFinishRequest.setMealId(mealId);
                }
                if(useTicked!=0){
                    mOrderFinishRequest.setUserArriveUseTicketId(useTicked);
                }
				mOrderFinishRequest.setDk();
				mDishController.execute(Actions.ACTION_DISH_ORDER_FINISH, mOrderFinishRequest);
			}
		}else{
			
			//取菜时间
			String time = timeEd.getText().toString();
			if(time.contains("取菜时间")){
				Toast.makeText(self, "请选择取菜时间！", Toast.LENGTH_LONG).show();
				return;
			}
			
			mOrderFinishRequest = new OrderFinishRequest();
			if(addId!=0){
				mOrderFinishRequest.setId(addId);
			}
			
			//验证码
				if(isYanz==0){
						mOrderFinishRequest.setVerify(yanzEd.getText().toString());
						mOrderFinishRequest.setNoteId(noteId);
				}
			mOrderFinishRequest.setStoreId(storeId);	
			mOrderFinishRequest.setDistributionType(choosePage);
			mOrderFinishRequest.setAlipayType(payType);
			mOrderFinishRequest.setId(Integer.parseInt(addressId));
			mOrderFinishRequest.setName(username);
			mOrderFinishRequest.setPhone(phone);
			mOrderFinishRequest.setAddress(address);
			mOrderFinishRequest.setFetchDishTime(timeEd.getText().toString());
			mOrderFinishRequest.setDishes(dishIds);

            if(mealId!=0){
                mOrderFinishRequest.setMealId(mealId);
            }

            if(useTicked!=0){
                mOrderFinishRequest.setUserArriveUseTicketId(useTicked);
            }
			mOrderFinishRequest.setDk();
			mDishController.execute(Actions.ACTION_DISH_ORDER_FINISH, mOrderFinishRequest);
		}
	}

	@Override
	public void scrollTopAction() {
		
	}

	@Override
	public void scrollBottomAction() {
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == MAP_DATA) {
			if (data != null) {
				String adstr = data.getExtras().getString("address");
				storeId = data.getExtras().getInt("id");
				addressEd.setText(adstr);
			}
		}	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(isAddress)
				validateData();
			if (flagData) {
				dialog();
			} else {
				finish();
				
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

    @Override
    public void onClick(View view) {
            int id  = view.getId();
            for (int i=0;i<arrviedVolumeList.size();i++){
                if(arrviedVolumeList.get(i).getId()==id){
                    if(arrviedVolumeList.get(i).getChecked()==0){
                        arrviedVolumeList.get(i).setChecked(1);
                        volamount = arrviedVolumeList.get(i).getArriveUseTicketPrice();
                        float total =totalamount - volamount ;
                        if(total<0){
                            total = 0;
                        }

                            totalPriceTv.setText("￥"+total);


                        useTicked =id;
                    }else if(arrviedVolumeList.get(i).getChecked()==1){
                        arrviedVolumeList.get(i).setChecked(0);
                        volamount = 0;

                        float total = totalamount-volamount;
                        if(total<0){
                            total=0;
                        }

                            totalPriceTv.setText("￥"+total);

                        useTicked = 0;
                    }
                }else{
                    arrviedVolumeList.get(i).setChecked(0);
                }
            }

        if(arrviedVolumeUseAdapter!=null){
            arrviedVolumeUseAdapter = (ArrviedVolumeUseAdapter) mVolList.getAdapter();
            arrviedVolumeUseAdapter.updateData(arrviedVolumeList);
        }
    }
}

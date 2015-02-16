package com.shanshengyuan.client.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.MealDetailAdapter;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.controller.DishController;

import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.db.ClientDBHelper;
import com.shanshengyuan.client.model.meal.Meal;
import com.shanshengyuan.client.model.meal.MealDetail;
import com.shanshengyuan.client.model.meal.MealDetailRequest;
import com.shanshengyuan.client.model.meal.MealDish;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.CollapsibleTextView;

import com.shanshengyuan.client.utils.StringUtils;


import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealDetailActivity extends BaseActivity implements OnResultListener,ClimbListView.UpLoadListener,AdapterView.OnItemClickListener {

    MealDetailActivity self = null;

    View mBack;

    // 列表
    private ClimbListView morderpageList;
    private MealDetailAdapter mealDetailAdapter;

    private View mHeadView;
    private LayoutInflater mInflater;

    MealDetail mealDetail;

    List<MealDish> mealDishList;

    Bundle bundle;
    int mid=0; //套餐id
    int showButtom = 1;//是否先菜品详情底部
    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    private MealDetailRequest mealDetailRequest;
    private DishController dishController;

    //视图
     TextView totalTv;
     Button mealBtn;
    //top
    private ImageView mealImg;
    private TextView mealName;
    private TextView mealAmount;
    private CollapsibleTextView healthTv;
    private TextView wenTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        if(dishController==null){
            dishController = new DishController(self,self);
        }
        mImageMap = new HashMap<String, ImageView>();
        bundle = getIntent().getExtras();

        mid =  bundle.getInt("mealId");
        initTopBar();
        initView();
        initData();
        setListener();
    }

    private void setListener(){
        mealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                intent.setClass(self, OrderPageActivity.class);

                String s= "";
                if(mealDishList!=null){
                    if(mealDishList.size()!=0){
                        for (int i = 0; i < mealDishList.size(); i++) {
                            s += "id="+ mealDishList.get(i).getDishId()+"|num="+ mealDishList.get(i).getDishNum()+"@";
                        }
                    }
                }


                b.putString("didStr", s);
                b.putInt("mealid",mid);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        mealDetailRequest = new MealDetailRequest();
        mealDetailRequest.setId(mid);
        mealDetailRequest.setDk();
        dishController.execute(Actions.ACTION_MEAL_DETAIL,mealDetailRequest);

    }

    private void initView(){
        mInflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = mInflater.inflate(R.layout.meal_detail_top, null);
        morderpageList = (ClimbListView)findViewById(R.id.meal_detail_list);
        morderpageList.addHeaderView(mHeadView);
        morderpageList.initFooterView();
        morderpageList.setUpLoadListener(this);
        morderpageList.setOnItemClickListener(this);
        totalTv = (TextView)findViewById(R.id.meal_detail_total);
        mealBtn = (Button)findViewById(R.id.meal_detail_result);

        mealImg = (ImageView) mHeadView.findViewById(R.id.meal_detail_img);
        mealName = (TextView) mHeadView.findViewById(R.id.meal_detail_name);
        mealAmount = (TextView)mHeadView.findViewById(R.id.meal_detail_amount);
        healthTv = (CollapsibleTextView) mHeadView.findViewById(R.id.meal_detail_content_tv);
        wenTv = (TextView)mHeadView.findViewById(R.id.meal_wen_show);

    }

    private void initTopBar() {
        mBack = (ImageView) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void updateUIData(){
        mealDetail = dishController.getMealDetail();

        String url = mealDetail.getImgUrl();

        if(!StringUtils.isEmpty(url)){
            SSYImageCache.getInstance().downloadImage(mImageMap, mealImg,
                    url, false, new SSYImageCache.ImageCallback() {

                        @Override
                        public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                            mImageMap.get(imageUrl).setImageBitmap(bitmap);

                        }
                    });

        }else{
            mealImg.setImageResource(R.drawable.defaule_img);
        }
        mealName.setText(mealDetail.getMealName());
        mealAmount.setText("￥"+mealDetail.getMealPrice());
        totalTv.setText("￥"+mealDetail.getMealPrice());
        if(StringUtils.isEmpty(mealDetail.getWarmPrompt())){
            wenTv.setText("健康、营养、美食、幸福、善食家");
        }else{
            wenTv.setText(mealDetail.getWarmPrompt());
        }

        healthTv.setDesc(mealDetail.getHealthTalk(), TextView.BufferType.NORMAL);

        mealDishList = mealDetail.getDishList();

        if (mealDishList.size() == 0) {
            morderpageList.noDataFinishNoScroll();
            //morderpageList.setVisibility(View.GONE);
            //	mNoReleaseLayout.setVisibility(View.VISIBLE);
            //return;
        } else if (mealDishList.size() < 10) {
            morderpageList.setVisibility(View.VISIBLE);
            //	mNoReleaseLayout.setVisibility(View.GONE);
            morderpageList.setHiddenFooterViewForScroll();

      //      com.shanshengyuan.client.utils.Log.e("listsize", list.size() + "");
        } else {
            morderpageList.setVisibility(View.VISIBLE);
            //	mNoReleaseLayout.setVisibility(View.GONE);
            morderpageList.loadedFinish();
            morderpageList.isFirst = true;
        }
        if (morderpageList.getAdapter() == null) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            mealDetailAdapter = new MealDetailAdapter(self, mealDishList);
            morderpageList.setAdapter(mealDetailAdapter);
        } else {
            mealDetailAdapter = (MealDetailAdapter) morderpageList.getAdapter();
            mealDetailAdapter.addListItems(mealDishList);
        }
    }

    @Override
    public void onSuccess(int actionType) {
       switch (actionType){
           case Actions.ACTION_MEAL_DETAIL:
               updateUIData();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MealDish t = mealDetailAdapter.getItem(i-2);
        Intent intent = new Intent();
        intent.setClass(self, DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dishId", t.getDishId()+"");
        bundle.putInt("isShowButtom",showButtom);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void scrollTopAction() {

    }

    @Override
    public void scrollBottomAction() {

    }
}

package com.shanshengyuan.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.MealAdapter;

import com.shanshengyuan.client.controller.DishController;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.meal.Meal;
import com.shanshengyuan.client.model.meal.MealRequest;

import com.shanshengyuan.client.utils.ClimbListView;

import java.util.List;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealActivity extends BaseActivity implements OnResultListener,ClimbListView.UpLoadListener, AdapterView.OnItemClickListener {

    private MealActivity self = null;

    //后退
    private ImageView mBack;

    private ClimbListView climbListView;
    private MealAdapter mealAdapter = null;
    private List<Meal> mealList;
    private TextView nodataTv;

    private MealRequest mealRequest;
    private DishController dishController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        if(dishController==null){
            dishController = new DishController(self,self);
        }
        initTopBar();
        initView();
        initData();
    }

    private void initData(){
        mealRequest = new MealRequest();
        mealRequest.setDk();
        dishController.execute(Actions.ACTION_MEAL_LIST,mealRequest);
    }

    private void initView(){
        nodataTv = (TextView)findViewById(R.id.meal_no_data_tv);

        climbListView = (ClimbListView)findViewById(R.id.meal_new_list);
        climbListView.initFooterView();
        climbListView.setUpLoadListener(this);
        climbListView.setOnItemClickListener(this);
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

    private void updateData(){
        mealList = dishController.getMealList();


        if(mealList!=null){
            if (mealList.size() == 0) {
                climbListView.noDataFinishNoScroll();
                climbListView.setVisibility(View.GONE);
                nodataTv.setVisibility(View.VISIBLE);
                return;
            } else if (mealList.size() < 1000) {
                climbListView.setVisibility(View.VISIBLE);
                climbListView.setHiddenFooterViewForScroll();
            } else {
                climbListView.setVisibility(View.VISIBLE);
                climbListView.loadedFinish();
                climbListView.isFirst = true;
            }
            if (climbListView.getAdapter() == null) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                mealAdapter = new MealAdapter(self, mealList);
                climbListView.setAdapter(mealAdapter);
            } else {
                mealAdapter = (MealAdapter) climbListView.getAdapter();
                mealAdapter.removeAllListData();
                mealAdapter.addListItems(mealList);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Meal t = mealAdapter.getItem(i-1);
        Intent intent = new Intent();
        intent.setClass(self, MealDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mealId", t.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int actionType) {
        switch (actionType){
            case Actions.ACTION_MEAL_LIST:
                updateData();
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
}

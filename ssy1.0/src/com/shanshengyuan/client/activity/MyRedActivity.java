package com.shanshengyuan.client.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;

import com.shanshengyuan.client.adapter.MyRedAdapter;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.model.redpackage.MyredRequest;
import com.shanshengyuan.client.utils.ClimbListView;
import com.shanshengyuan.client.utils.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MyRedActivity extends BaseActivity implements  OnResultListener,
        View.OnClickListener, AdapterView.OnItemClickListener, ClimbListView.UpLoadListener,View.OnKeyListener,PullToRefreshListView.HideShowListener {

    private MyRedActivity self = null;
    //后退
    private ImageView mBack;

    private ClimbListView climbListView;
    private MyRedAdapter myRedAdapter;
    private List<MyRed> myRedList;

    private MyredRequest myredRequest;
    private AddressController addressController;

    private View mHeadView;
    private LayoutInflater mInflater;
    private TextView nodataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red);

        if(addressController==null){
            addressController = new AddressController(self,self);
        }
        initTopBar();
        initViews();
        initData();
    }

    private void initData(){
        myredRequest = new MyredRequest();
        myredRequest.setDk();
        addressController.execute(Actions.ACTION_MY_RED,myredRequest);
    }

    private void initViews(){
        nodataTv = (TextView)findViewById(R.id.my_red_no_data_tv);

        climbListView = (ClimbListView)findViewById(R.id.my_red_list);
        climbListView.initFooterView();
        climbListView.setUpLoadListener(this);
        climbListView.setOnItemClickListener(this);

        mInflater = (LayoutInflater) self
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeadView = mInflater.inflate(R.layout.activity_my_red_top, null);
        climbListView.addHeaderView(mHeadView);
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
        myRedList = addressController.getMyRedLists();


        if(myRedList!=null){
            if (myRedList.size() == 0) {
                climbListView.noDataFinishNoScroll();
                climbListView.setVisibility(View.GONE);
                nodataTv.setVisibility(View.VISIBLE);
                return;
            } else if (myRedList.size() < 1000) {
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
                myRedAdapter = new MyRedAdapter(self, myRedList,this);
                climbListView.setAdapter(myRedAdapter);
            } else {
                myRedAdapter = (MyRedAdapter) climbListView.getAdapter();
                myRedAdapter.removeAllListData();
                myRedAdapter.addListItems(myRedList);
            }
        }
    }

    @Override
    public void onSuccess(int actionType) {
        switch (actionType) {
            case Actions.ACTION_MY_RED:
                updateData();
            break;
            default:
                break;
        }


    }

    @Override
    public void onFailed(int actionType, BaseResponse result) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void showAction() {

    }

    @Override
    public void hideAction() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void scrollTopAction() {

    }

    @Override
    public void scrollBottomAction() {

    }
}

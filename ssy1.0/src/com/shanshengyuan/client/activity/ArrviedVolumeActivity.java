package com.shanshengyuan.client.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.shanshengyuan.client.Actions;
import com.shanshengyuan.client.BaseActivity;
import com.shanshengyuan.client.BaseResponse;
import com.shanshengyuan.client.R;
import com.shanshengyuan.client.adapter.ArrviedVolumeAdapter;
import com.shanshengyuan.client.adapter.MyRedAdapter;
import com.shanshengyuan.client.controller.AddressController;
import com.shanshengyuan.client.model.arrviedVolume.ArrviedVolumeRequest;
import com.shanshengyuan.client.model.arrviedVolume.arrviedVolume;
import com.shanshengyuan.client.model.redpackage.MyRed;
import com.shanshengyuan.client.model.redpackage.MyredRequest;
import com.shanshengyuan.client.utils.ClimbListView;

import java.util.List;

/**
 * Created by Administrator on 2015/2/10 0010.
 */
public class ArrviedVolumeActivity extends BaseActivity implements  OnResultListener,
        View.OnClickListener, AdapterView.OnItemClickListener, ClimbListView.UpLoadListener,View.OnKeyListener {

    ArrviedVolumeActivity self = null;

    //后退
    private ImageView mBack;

    private ClimbListView climbListView;
    private ArrviedVolumeAdapter arrviedVolumeAdapter;
    private List<arrviedVolume> arrviedVolumeList;

    private ArrviedVolumeRequest arrviedVolumeRequest;
    private AddressController addressController;

    public ArrviedVolumeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrived_volume);
        if(addressController==null){
            addressController = new AddressController(self,self);
        }
        initTopBar();
        initView();
        initData();
    }

    private void initData(){
        arrviedVolumeRequest = new ArrviedVolumeRequest();
        arrviedVolumeRequest.setDk();
        addressController.execute(Actions.ACTION_ARR_VOL,arrviedVolumeRequest);
    }

    private void initView(){
        climbListView = (ClimbListView)findViewById(R.id.my_arrived_volume_list);
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
        arrviedVolumeList = addressController.getArrviedVolumes();

        if(arrviedVolumeList!=null){
            if (arrviedVolumeList.size() == 0) {
                climbListView.noDataFinishNoScroll();
                climbListView.setVisibility(View.GONE);
                return;
            } else if (arrviedVolumeList.size() < 1000) {
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
                arrviedVolumeAdapter = new ArrviedVolumeAdapter(self, arrviedVolumeList,this);
                climbListView.setAdapter(arrviedVolumeAdapter);
            } else {
                arrviedVolumeAdapter = (ArrviedVolumeAdapter) climbListView.getAdapter();
                arrviedVolumeAdapter.removeAllListData();
                arrviedVolumeAdapter.addListItems(arrviedVolumeList);
            }
        }
    }

    @Override
    public void onSuccess(int actionType) {
       switch (actionType){
           case Actions.ACTION_ARR_VOL:
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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

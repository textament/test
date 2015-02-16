package com.shanshengyuan.client.model.redpackage;

import com.shanshengyuan.client.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MyredResponse extends BaseResponse {

    private List<MyRed> data;

    public List<MyRed> getData() {
        return data;
    }

    public void setData(List<MyRed> data) {
        this.data = data;
    }
}

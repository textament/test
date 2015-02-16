package com.shanshengyuan.client.model.arrviedVolume;

import com.shanshengyuan.client.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2015/2/10 0010.
 */
public class ArrviedVolumeResponse extends BaseResponse {

    private List<arrviedVolume> data;

    public List<arrviedVolume> getData() {
        return data;
    }

    public void setData(List<arrviedVolume> data) {
        this.data = data;
    }
}

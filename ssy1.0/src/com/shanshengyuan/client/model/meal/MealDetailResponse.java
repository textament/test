package com.shanshengyuan.client.model.meal;

import com.shanshengyuan.client.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealDetailResponse extends BaseResponse {

    MealDetail data;

    public MealDetail getData() {
        return data;
    }

    public void setData(MealDetail data) {
        this.data = data;
    }
}

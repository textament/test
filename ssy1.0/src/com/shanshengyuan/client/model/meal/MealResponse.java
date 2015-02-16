package com.shanshengyuan.client.model.meal;

import com.shanshengyuan.client.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealResponse extends BaseResponse {

    List<Meal> data;

    public List<Meal> getData() {

        return data;
    }

    public void setData(List<Meal> data) {
        this.data = data;
    }
}

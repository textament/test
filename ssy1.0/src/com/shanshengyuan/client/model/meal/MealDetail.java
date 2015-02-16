package com.shanshengyuan.client.model.meal;

import java.util.List;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealDetail {

    private int id;
    private String mealName;
    private String imgUrl;
    private float mealPrice;
    private float originalPrice;
    private String remark;
    private String healthTalk;
    private String warmPrompt;
    private List<MealDish> dishList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(float mealPrice) {
        this.mealPrice = mealPrice;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHealthTalk() {
        return healthTalk;
    }

    public void setHealthTalk(String healthTalk) {
        this.healthTalk = healthTalk;
    }

    public String getWarmPrompt() {
        return warmPrompt;
    }

    public void setWarmPrompt(String warmPrompt) {
        this.warmPrompt = warmPrompt;
    }

    public List<MealDish> getDishList() {
        return dishList;
    }

    public void setDishList(List<MealDish> dishList) {
        this.dishList = dishList;
    }
}

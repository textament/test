package com.shanshengyuan.client.model.meal;

/**
 * Created by Administrator on 2015/2/11 0011.
 */
public class MealDish {

    private int dishId;
    private String dishName;
    private String dishImg;
    private int dishNum;

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishImg() {
        return dishImg;
    }

    public void setDishImg(String dishImg) {
        this.dishImg = dishImg;
    }

    public int getDishNum() {
        return dishNum;
    }

    public void setDishNum(int dishNum) {
        this.dishNum = dishNum;
    }
}

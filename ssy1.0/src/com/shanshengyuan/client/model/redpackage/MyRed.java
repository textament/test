package com.shanshengyuan.client.model.redpackage;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/2/7 0007.
 */
public class MyRed {

    private String startTime;
    private String endTime;
    private String address;
    private String remark;
    private String codeKey;
    private int isActivate;//是否激活 0.未激活 1.已激活
    private int isUse; //是否使用 0.未使用 1.已使用
    private String redPacketPrice;
    private int state;//红包状态 0.失效 1.有效
    private String imgUrl;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public int getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public String getRedPacketPrice() {
        return redPacketPrice;
    }

    public void setRedPacketPrice(String redPacketPrice) {
        this.redPacketPrice = redPacketPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

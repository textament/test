package com.shanshengyuan.client.model.arrviedVolume;

/**
 * Created by Administrator on 2015/2/10 0010.
 */
public class arrviedVolume {

    private int id;
    private String startTime;
    private String endTime;
    private String remark;
    private int isUse;//是否使用 0.未使用 1.已使用
    private float arriveUseTicketPrice;
    private int state;//抵用券状态 0.失效 1.有效

    private int checked;

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public float getArriveUseTicketPrice() {
        return arriveUseTicketPrice;
    }

    public void setArriveUseTicketPrice(float arriveUseTicketPrice) {
        this.arriveUseTicketPrice = arriveUseTicketPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

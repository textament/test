package com.shanshengyuan.client.model.order;

import java.io.Serializable;
import java.util.List;

import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.address.StoreDishPoint;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.dish.DishesOrder;

/**
 * 
 * @author lihao
 *
 */
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//地址列表
	private List<Address> addressList;
	//时间
	private List<String> fetchDishTimeList;
	//文字说明
	private String explain;
	//付款方式文字
	private String payWayName;
	//总价
	private String totalPrice;
	//菜品列表
	private List<DishesOrder> dishList;
	//取菜点列表
	private List<StoreDishPoint> storeDishPointList;
	
	private float theSameDaySendCost;
	
	private String theSameDaySendName;
	
	
	
	public String getTheSameDaySendName() {
		return theSameDaySendName;
	}
	public void setTheSameDaySendName(String theSameDaySendName) {
		this.theSameDaySendName = theSameDaySendName;
	}
	public float getTheSameDaySendCost() {
		return theSameDaySendCost;
	}
	public void setTheSameDaySendCost(float theSameDaySendCost) {
		this.theSameDaySendCost = theSameDaySendCost;
	}
	public List<StoreDishPoint> getStoreDishPointList() {
		return storeDishPointList;
	}
	public void setStoreDishPointList(List<StoreDishPoint> storeDishPointList) {
		this.storeDishPointList = storeDishPointList;
	}
	public List<Address> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}
	public List<String> getFetchDishTimeList() {
		return fetchDishTimeList;
	}
	public void setFetchDishTimeList(List<String> fetchDishTimeList) {
		this.fetchDishTimeList = fetchDishTimeList;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getPayWayName() {
		return payWayName;
	}
	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<DishesOrder> getDishList() {
		return dishList;
	}
	public void setDishList(List<DishesOrder> dishList) {
		this.dishList = dishList;
	}
	
	
	
}

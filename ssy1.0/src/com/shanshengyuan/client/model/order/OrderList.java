package com.shanshengyuan.client.model.order;

import java.io.Serializable;
import java.util.List;

import com.shanshengyuan.client.model.dish.DishesOrderFinish;

public class OrderList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String orderTime;
	private String stateName;
	private String totalPrice;
	private List<DishesOrderFinish> dishList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<DishesOrderFinish> getDishList() {
		return dishList;
	}
	public void setDishList(List<DishesOrderFinish> dishList) {
		this.dishList = dishList;
	}
	
	
}

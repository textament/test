/**
 * 
 */
package com.sststore.client.model.order;

import java.io.Serializable;
import java.util.List;

import com.sststore.client.model.dish.DishesOrderFinish;

/**
 * @author Administrator
 *
 */
public class OrderDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//时间
	private List<String> fetchDishTimeList;
	//文字说明
	private String explain;
	//付款方式文字
	private String payWayName;
	//总价
	private String totalPrice;
	//菜品列表
	private List<DishesOrderFinish> dishList;
	
	//送货时间
	private String sendTime;
	//订单状态
	private String stateName;
	//用户信息
	private String userName;
	private String userPhone;
	private String userAddress;
	//支付状态
	private String alipayState;
	
	Integer id;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAlipayState() {
		return alipayState;
	}
	public void setAlipayState(String alipayState) {
		this.alipayState = alipayState;
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
	public List<DishesOrderFinish> getDishList() {
		return dishList;
	}
	public void setDishList(List<DishesOrderFinish> dishList) {
		this.dishList = dishList;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
}

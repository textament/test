package com.shanshengyuan.client.model.order;

import java.io.Serializable;
import java.util.List;

import com.shanshengyuan.client.model.address.Address;
import com.shanshengyuan.client.model.dish.Dishes;
import com.shanshengyuan.client.model.dish.DishesOrder;
import com.shanshengyuan.client.model.dish.DishesOrderFinish;

/**
 * 
 * @author lihao
 *
 */
public class OrderFinish implements Serializable{
	
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
	private List<DishesOrderFinish> dishList;
	
	private int id;
	
	//送货时间
	private String sendTime;
	//订单状态
	private String stateName;
	//用户信息
	private String userName;
	private String userPhone;
	private String userAddress;
	
	private String orderNo;//订单号
	private int alipayState;//支付状态  0.未支付  1.已支付
	private String orderTime;//下单时间
	private String twoDimensionCode;//二维码地址
	private int distributionType;//0.送货上门 1.取菜点自提
	private int alipayType;//0.货到付款 1.在线支付

    private int isSupportRedPacket;//是否获取红包 0.未获取 1.已获取

    public int getIsSupportRedPacket() {
        return isSupportRedPacket;
    }

    public void setIsSupportRedPacket(int isSupportRedPacket) {
        this.isSupportRedPacket = isSupportRedPacket;
    }

    public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getAlipayState() {
		return alipayState;
	}
	public void setAlipayState(int alipayState) {
		this.alipayState = alipayState;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}
	public int getDistributionType() {
		return distributionType;
	}
	public void setDistributionType(int distributionType) {
		this.distributionType = distributionType;
	}
	public int getAlipayType() {
		return alipayType;
	}
	public void setAlipayType(int alipayType) {
		this.alipayType = alipayType;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}

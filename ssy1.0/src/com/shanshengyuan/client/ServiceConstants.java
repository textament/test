package com.shanshengyuan.client;

/**
 * @author lihao
 */
public class ServiceConstants {
	
	public static final String SERVICE_AD = "ad/ad_list"; // 广告
	public static final String SERVICE_DISH_LIST = "dish/dish_list";//菜品列表
	public static final String SERVICE_DISH_TYPE = "dishType/dishType_list";//菜品类型
	public static final String SERVICE_DISH_DETAIL = "dish/dish_detail";//菜品详情
	public static final String SERVICE_DISH_ORDER = "reckoning/reckoning_order";//结算订单
	public static final String SERVICE_ADDRESS_LIST = "takeAddress/takeAddress_list";//地址列表
	public static final String SERVICE_ADDRESS_ADD = "takeAddress/takeAddress_add";//添加地址
	public static final String SERVICE_ADDRESS_UPDATE = "takeAddress/takeAddress_update";//更新地址
	public static final String SERVICE_DISH_FINISH = "order/order_add";//下订单
	public static final String SERVICE_ORDER_LIST = "order/order_list"; //订单列表
	public static final String SERVICE_ORDER_DETAIL = "order/order_detail";//订单详情
	public static final String SERVICE_FEED_BACK = "opinionFeedback/opinionFeedback_add";//意见反馈
	public static final String SERVICE_DISH_SC = "dish/dish_statistics";//菜品统计
	public static final String SERVICE_VER_CODE = "note/note_verify";//验证码
	public static final String SERVICE_NEW_VERSION = "config/config_getNewVersionNum";//版本更新接口
    public static final String SERVICE_MY_RED = "redPacket/redPacket_list";//我的红包
    public static final String SERVICE_RED_AR_NUM = "amount/amount_totalNum";//红包抵用卷数量
    public static final String SERVICE_ARR_VOL = "userArriveUseTicket/userArriveUseTicket_list";//抵用卷列表
    public static final String SERVICE_ARR_USE_VOL = "userArriveUseTicket/userArriveUseTicket_usableList";//可用抵用卷列表

    public static final String SERVICE_MEAL_LIST = "meal/meal_list";//套餐列表
	public static final String SERVICE_MEAL_DETAIL = "meal/meal_detail";//套餐详情
}

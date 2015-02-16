/**
 * 
 */
package com.shanshengyuan.client.model.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.DeviceKey;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author lihao
 *
 */
public class OrderFinishRequest extends BaseRequest {

	private String channel; //终端类型 0.app 1.微信
	private String appId;//终端标识：手机IMEI号与微信的openId
	private String timestamp; //终端本地时间戳
	private String version; //终端版本号
	private String apiVersion;//接口版本号
	private String key;//密匙
	
	private Integer id; //地址ID
	private String name; //收货人姓名
	private String phone; //收货人电话
	private String address;//收货人地址
	private String fetchDishTime;//取菜时间
	private String dishes;//菜品ID串
	private Integer distributionType;//0.送货上门 1.取菜点自提
	private Integer alipayType;//0.货到付款 1.在线支付
	private Integer storeId;//取菜点ID，此字段只有在distributionType值为1时必填
	private Integer noteId;//验证码ID
	private String verify;//验证码
    private Integer mealId;//套餐id
    private Integer userArriveUseTicketId;//抵用卷id

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public Integer getUserArriveUseTicketId() {
        return userArriveUseTicketId;
    }

    public void setUserArriveUseTicketId(Integer userArriveUseTicketId) {
        this.userArriveUseTicketId = userArriveUseTicketId;
    }

    public Integer getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}

	public Integer getAlipayType() {
		return alipayType;
	}

	public void setAlipayType(Integer alipayType) {
		this.alipayType = alipayType;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDishes() {
		return dishes;
	}

	public void setDishes(String dishes) {
		this.dishes = dishes;
	}
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFetchDishTime() {
		return fetchDishTime;
	}

	public void setFetchDishTime(String fetchDishTime) {
		this.fetchDishTime = fetchDishTime;
	}

	public void setDk() {
		
	    List<String> dkList;

		DeviceKey dk;

		dk = new DeviceKey();
		dkList = new ArrayList<String>();
		dkList.add("channel:" + dk.getChannel());
		dkList.add("appId:" + dk.getAppId());
		dkList.add("timestamp:" + dk.getTimestamp());
		dkList.add("version:" + dk.getVersion());
		dkList.add("apiVersion:" + dk.getApiVersion());
		
		if(id!=null){
			dkList.add("id:" + getId());
		}
		if(!StringUtils.isEmpty(name))
			dkList.add("name:" + getName());
		if(!StringUtils.isEmpty(phone))
			dkList.add("phone:" + getPhone());
		if(!StringUtils.isEmpty(address))
			dkList.add("address:" + getAddress());
		if(!StringUtils.isEmpty(fetchDishTime))
			dkList.add("fetchDishTime:" + getFetchDishTime());
		if(!StringUtils.isEmpty(dishes))
			dkList.add("dishes:" + getDishes());
		
		if(distributionType!=null){
			dkList.add("distributionType:" +getDistributionType());
		}
		if(alipayType!=null){
			dkList.add("alipayType:" +getAlipayType());
		}
		if(storeId!=null){
			dkList.add("storeId:" +getStoreId());
		}
		if(noteId!=null){
			dkList.add("noteId:" +getNoteId());
		}
		if(!StringUtils.isEmpty(verify)){
			dkList.add("verify:" +getVerify());
		}

        if(mealId!=null){
            dkList.add("mealId:" +getMealId());
        }

        if(userArriveUseTicketId!=null){
            dkList.add("userArriveUseTicketId:" +getUserArriveUseTicketId());
        }

		String s = "";
		List<String> list = dkList;
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			s += list.get(i) + "@";
		}
		String s1 = s.substring(0, s.lastIndexOf("@"));
		setChannel(dk.getChannel());
		setAppId(dk.getAppId());
		setTimestamp(dk.getTimestamp());
		setVersion(dk.getVersion());
		setApiVersion(dk.getApiVersion());
		setKey(StringUtils.MD5(s1));
	}
}

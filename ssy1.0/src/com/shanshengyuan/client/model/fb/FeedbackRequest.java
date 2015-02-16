package com.shanshengyuan.client.model.fb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.DeviceKey;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * 
 * @author lihao
 *
 */
public class FeedbackRequest extends BaseRequest {

	private String channel; //终端类型 0.app 1.微信
	private String appId;//终端标识：手机IMEI号与微信的openId
	private String timestamp; //终端本地时间戳
	private String version; //终端版本号
	private String apiVersion;//接口版本号
	private String key;//密匙
	
	private String name; //姓名
	private String phone; //电话
	private String content;//内容

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

		if(!StringUtils.isEmpty(content))
			dkList.add("content:" + getContent());
		
		
		if(!StringUtils.isEmpty(name))
			dkList.add("name:" + getName());
		
		if(!StringUtils.isEmpty(phone))
			dkList.add("phone:" + getPhone());
		


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

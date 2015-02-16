/**
 * com.zhubajie.client.model.ad
 * AdverRequest.java
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-25 		lihao
 *
 *  Copyright (c) 2013 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.model.ad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.DeviceKey;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * ClassName:AdverRequest Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author lihao
 * @version
 * 
 * @see
 * 
 */
public class AdverRequest extends BaseRequest {

	private String channel; //终端类型 0.app 1.微信
	private String appId;//终端标识：手机IMEI号与微信的openId
	private String timestamp; //终端本地时间戳
	private String version; //终端版本号
	private String apiVersion;//接口版本号
	private String key;//密匙

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
		dkList.add("hasProcessDialog:0");
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

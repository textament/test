/**
 * 
 */
package com.sststore.client;

import java.util.Date;


/**
 * @author Action.Tan
 * 
 */
public class DeviceKey {
	private String channel; //终端类型 0.app 1.微信
	private String appId;//终端标识：手机IMEI号与微信的openId
	private String timestamp; //终端本地时间戳
	private String version; //终端版本号
	private String apiVersion;//接口版本号
	private String key;//密匙

	public DeviceKey() {
		channel = "0";
		appId = BaseApplication.sIMEI;
		timestamp = new Date().getTime()+"";
		version = BaseApplication.VERSION;
		apiVersion = BaseApplication.API_VERSION;
		
//		if (Config.BAIDU_USER != null) {
//			baidu_userid = Config.BAIDU_USER;
//		} else {
//			baidu_userid = null;
//		}
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






	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}


}

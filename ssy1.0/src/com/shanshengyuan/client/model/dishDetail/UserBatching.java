package com.shanshengyuan.client.model.dishDetail;

import java.io.Serializable;

/**
 * 
 * @author lihao
 *
 */
public class UserBatching implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//名称
	String burdeningName;
	//重量
	String weight;
	public String getBurdeningName() {
		return burdeningName;
	}
	public void setBurdeningName(String burdeningName) {
		this.burdeningName = burdeningName;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	

}

package com.shanshengyuan.client.model.address;

import java.io.Serializable;

/**
 * 
 * @author lihao
 *
 */
public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String name;
	
	private String phone;
	
	private String address;
	
	private String isDefault;
	//地址类型 0.送货上门 1.取菜点自提
	private int type;
	//是否进行短信验证 0.未验证 1.已验证
	private int isNoteVerify;
	
	private int storeId;
	
	
	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsNoteVerify() {
		return isNoteVerify;
	}

	public void setIsNoteVerify(int isNoteVerify) {
		this.isNoteVerify = isNoteVerify;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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

	
}

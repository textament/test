/**
 * 
 */
package com.shanshengyuan.client.model.dishDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @author lihao
 *
 */
public class DishDetail implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String imgBig;
	private String videoLink;
	private String price;
	private List<Batching> burdeningSSY;
	private List<UserBatching> burdeningUser;
	private String healthTalk;
	private List<Steps> step;
	private String warmPrompt;
	
	
	public String getWarmPrompt() {
		return warmPrompt;
	}
	public void setWarmPrompt(String warmPrompt) {
		this.warmPrompt = warmPrompt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgBig() {
		return imgBig;
	}
	public void setImgBig(String imgBig) {
		this.imgBig = imgBig;
	}
	public String getVideoLink() {
		return videoLink;
	}
	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
	public List<Batching> getBurdeningSSY() {
		return burdeningSSY;
	}
	public void setBurdeningSSY(List<Batching> burdeningSSY) {
		this.burdeningSSY = burdeningSSY;
	}
	public List<UserBatching> getBurdeningUser() {
		return burdeningUser;
	}
	public void setBurdeningUser(List<UserBatching> burdeningUser) {
		this.burdeningUser = burdeningUser;
	}
	public String getHealthTalk() {
		return healthTalk;
	}
	public void setHealthTalk(String healthTalk) {
		this.healthTalk = healthTalk;
	}
	public List<Steps> getStep() {
		return step;
	}
	public void setStep(List<Steps> step) {
		this.step = step;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
}

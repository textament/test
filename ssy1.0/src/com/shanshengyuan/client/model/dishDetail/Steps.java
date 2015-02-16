/**
 * 
 */
package com.shanshengyuan.client.model.dishDetail;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class Steps implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//步骤内容 
	String stepDescription;
	//步骤数
	String sNum;
	//图片
	String stepImg;
	
	
	public String getsNum() {
		return sNum;
	}
	public void setsNum(String sNum) {
		this.sNum = sNum;
	}
	public String getStepDescription() {
		return stepDescription;
	}
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}
	public String getStepImg() {
		return stepImg;
	}
	public void setStepImg(String stepImg) {
		this.stepImg = stepImg;
	}
	
	
	
}

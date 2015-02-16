/**
 * LoginValidateUtil.java
 * com.zhubajie.client.utils.Log
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-11-23 		lihao
 *
 *  Copyright (c) 2012 zhubajie, TNT All Rights Reserved.
*/

package com.shanshengyuan.client.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName:LoginValidateUtil
 * Function:登录验证
 *
 * @author   lihao
 * @version  
 * @since    Ver 2.0.1
 * @Date	 2012-11-23
 *
 * @see 	 
 * 
 */
public class LoginValidateUtil {
	
	
	public static String loginNameValidate(String loginName){
		
		String errorMsg = "";
		
		if(StringUtils.isEmpty(loginName)){
			errorMsg = "请输入登录名";
		}else{
			//汉字个数
			int chn = getChineseChar(loginName);
			//非汉字
			int notCHN = getNotChineseChar(loginName);
			//总个数
			int totleNum = chn*2 + notCHN;
			if(totleNum<4||totleNum>16){
				errorMsg = "登录名由4-16位字符组成";
			}else
			if(isFirstletterContain(loginName.substring(0, 1))==false){
				errorMsg =  "登录名只能以汉字和英文字母开头";
			}else if(isFirstletterContain(loginName.substring(0, 1))){
				if(isOtherLetterContain(loginName.substring(1, loginName.length()))==false){
					errorMsg = "该登录名包含非法字符";
				}
			}
		}
		
		
		return errorMsg ;
	}

	
	
	/**
	 * 
	 * isContain:首字母用户名验证
	 * 
	 * @param match
	 *            正则表达式
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author lihao
	 * @since zhubajie　Ver 2.0.1
	 */
	public static boolean isFirstletterContain(String str) {
		//首字符不能为数字
		String validate = "^[a-zA-Z\\u4e00-\\u9fa5]";
		Pattern pattern = Pattern.compile(validate);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * isContain:其他字母用户名验证
	 * 
	 * @param match
	 *            正则表达式
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author lihao
	 * @since zhubajie　Ver 2.0.1
	 */
	private static boolean isOtherLetterContain(String str){
		String validate = "[0-9a-zA-Z\\u4e00-\\u9fa5]+$";
		Pattern pattern = Pattern.compile(validate);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * getChineseChar:获取字符串中汉字的个数
	 *
	 * @param  @param str
	 * 				值
	 * @param  @return    
	 * @return int    
	 * @throws 
	 * @author  
	 * @since  zhubajie　Ver 2.0.1
	 */
	public static int getChineseChar(String str){
		  int chCnt = 0;  
	        String regEx = "[\\u4e00-\\u9fa5]"; // 如果考虑繁体字，u9fa5-->u9fff
	        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regEx);  
	        java.util.regex.Matcher m = p.matcher(str);  
	        while (m.find()) {  
	                chCnt++;  
	        }  
	        return chCnt;
	}
	
	/**
	 * 
	 * getNotChineseChar:非中文个数
	 *
	 * @param  @param str
	 * 				值
	 * @param  @return    
	 * @return int    
	 * @throws 
	 * @author  
	 * @since  zhubajie　Ver 2.0.1
	 */
	public static int getNotChineseChar(String str){
		 int chCnt = 0;  
	        String regEx = "[^\\u4e00-\\u9fa5]"; // 如果考虑繁体字，u9fa5-->u9fff
	        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regEx);  
	        java.util.regex.Matcher m = p.matcher(str);  
	        while (m.find()) {  
	                chCnt++;  
	        }  
	        return chCnt;
	}

}


package com.sststore.client;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Action Tan
 * @date Mar 26, 2012
 */
public class BaseRequest {

    public String hasProcessDialog = null;
    
    public BaseRequest() {
     
//        String s = JSONHelper.objToJson(deviceKey);
//        dk = Base64.encodeBytes(s.getBytes());
        
//        if(Config.BAIDU_USER != null) {
//        	baidu_userid = Config.BAIDU_USER;
//        }else{
//        	baidu_userid = null;
//        }
    }
 
  
//    public String getBaidu_userid() {
//		return baidu_userid;
//	}
    
    


	public String getHasProcessDialog() {
		return hasProcessDialog;
	}

	



	public void setHasProcessDialog(String hasProcessDialog) {
		this.hasProcessDialog = hasProcessDialog;
	}

}

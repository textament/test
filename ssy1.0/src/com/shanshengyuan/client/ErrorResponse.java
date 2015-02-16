package com.shanshengyuan.client;


/**
 * hlee
 */
public class ErrorResponse extends BaseResponse {

	
	  protected String msg;
	    
	    protected int status;
	    
	    protected String verification;
	    //下单失败的错误提示
	    protected int failType;
   
    public ErrorResponse() {

    }

    public ErrorResponse(String errorMessage) {

        this.msg = errorMessage;
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public int getFailType() {
		return failType;
	}

	public void setFailType(int failType) {
		this.failType = failType;
	}

    
    
    

  
}

package com.shanshengyuan.client;

/**
 * @author lihao
 */
public class BaseResponse {
    protected int result;
    protected String msg;
    
    protected int status;
    
    protected String verification;
    //下单失败的错误提示
    protected int failType;
    
    
    
	public int getFailType() {
		return failType;
	}

	public void setFailType(int failType) {
		this.failType = failType;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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
    
    
}

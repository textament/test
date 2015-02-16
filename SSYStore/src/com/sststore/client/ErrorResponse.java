package com.sststore.client;


/**
 * @author Action Tan
 * @date Mar 27, 2012
 */
public class ErrorResponse extends BaseResponse {

   
    public ErrorResponse() {

    }

    public ErrorResponse(String errorMessage) {

        this.msg = errorMessage;
    }

    
    
    

  
}

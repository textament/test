package com.shanshengyuan.client;

/**
 * @author Action Tan
 * @date Mar 28, 2012
 */
public class AsyncTaskPayload {
    private int taskType = -1;
    private Object[] data = null;
    private Object result = null;
    private BaseResponse response = null;
    
    
    public AsyncTaskPayload() {
        this(-1, null);
    }

    public AsyncTaskPayload(int taskType) {
        this(taskType, null);
    }
    
    public AsyncTaskPayload(Object[] data) {
        this(-1, data);
    }
    
    public AsyncTaskPayload(int taskType, Object[] data) {
        this.taskType = taskType;
        this.data = data;
    }
    
    public int getTaskType() {
        return taskType;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public void setResult(Object object) {
        result = object;
    }
    
    public Object getResult() {
        return result;
    }
    
    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }

    
}

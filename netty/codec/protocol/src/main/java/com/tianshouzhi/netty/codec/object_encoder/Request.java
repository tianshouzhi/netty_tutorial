package com.tianshouzhi.netty.codec.object_encoder;

import java.util.Date;

public class Request {
    private Date requestTime;
    private String request;

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}

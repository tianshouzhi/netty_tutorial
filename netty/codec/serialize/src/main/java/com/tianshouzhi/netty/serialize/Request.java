package com.tianshouzhi.netty.serialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tianshouzhi on 2018/9/8.
 */
public class Request implements Serializable{
    private String request;
    private Date requestTime;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "Request{" +
                "request='" + request + '\'' +
                ", requestTime=" + requestTime.toLocaleString() +
                '}';
    }
}

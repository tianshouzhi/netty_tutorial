package com.tianshouzhi.netty.serialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tianshouzhi on 2018/9/8.
 */
public class Response implements Serializable{
    private String response;
    private Date responseTime;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                ", responseTime=" + responseTime.toLocaleString() +
                '}';
    }
}

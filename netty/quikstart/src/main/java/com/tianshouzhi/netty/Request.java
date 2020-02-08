package com.tianshouzhi.netty;

import java.io.Serializable;

public class Request implements Serializable {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Request{" +
                "content='" + content + '\'' +
                '}';
    }
}

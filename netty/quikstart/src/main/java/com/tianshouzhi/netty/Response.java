package com.tianshouzhi.netty;

import java.io.Serializable;

public class Response implements Serializable {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "content='" + content + '\'' +
                '}';
    }
}

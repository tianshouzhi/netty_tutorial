package com.tianshouzhi.time;

import java.io.Serializable;

public class Request implements Serializable {
	//请求id
	protected Long requestId;
	//请求体
	protected Object requestBody;
	//请求创建时间
	protected Long createTime;
	//请求超时时间
	private int timeoutMillis;

	public Request() {
	}

	public Request(Long requestId, Object requestBody, Long createTime) {
		this.requestId = requestId;
		this.requestBody = requestBody;
		this.createTime = createTime;
	}

	public Request(Long requestId, Object requestBody, Long createTime, int timeoutMillis) {
		this.requestId = requestId;
		this.requestBody = requestBody;
		this.createTime = createTime;
		this.timeoutMillis = timeoutMillis;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public String toString() {
		return "Request{" +
				"requestId=" + requestId +
				", requestBody=" + requestBody +
				", createTime=" + createTime +
				", timeoutMillis=" + timeoutMillis +
				'}';
	}
}

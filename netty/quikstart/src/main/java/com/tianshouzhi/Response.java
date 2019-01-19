package com.tianshouzhi;

import java.io.Serializable;

public class Response implements Serializable {
	private Long requestId;

	private Object responseBody;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Object getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return "Response{" +
				"requestId=" + requestId +
				", responseBody=" + responseBody +
				'}';
	}
}

package com.tianshouzhi.time;

import com.tianshouzhi.Response;

public abstract class RpcInvoke {
	protected Request request;

	protected Response response;

	public RpcInvoke(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}

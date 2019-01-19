package com.tianshouzhi.time;

import com.tianshouzhi.Response;

public class RpcInvokeCallback extends RpcInvoke {
	private Callback callback;

	public RpcInvokeCallback(Request request, Callback callback) {
		super(request);
		this.callback = callback;
	}

	@Override
	public void setResponse(Response response) {
		super.setResponse(response);
		callback.onComplete(request, response);
	}
}

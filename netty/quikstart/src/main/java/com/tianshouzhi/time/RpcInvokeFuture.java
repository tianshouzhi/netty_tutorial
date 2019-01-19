package com.tianshouzhi.time;

import com.tianshouzhi.Response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class RpcInvokeFuture extends RpcInvoke {
	private Thread thread;

	public RpcInvokeFuture(Request request) {
		super(request);
	}

	@Override
	public Response getResponse() {
		if (response != null) {
			return response;
		}
		this.thread = Thread.currentThread();
		LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(request.getTimeoutMillis()));
		// 如果响应为null，说明请求超时，从rpc调用队列中移出，并抛出超时异常
		if (response == null) {
			throw new RuntimeException("timeout in " + request.getTimeoutMillis() + " millis!");
		}
		return response;
	}

	public Object getResponseBody() {
		return getResponse().getResponseBody();
	}

	@Override
	public void setResponse(Response response) {
		super.setResponse(response);
		LockSupport.unpark(thread);
	}
}

package com.tianshouzhi.time;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcInvokeQueue {
	private Map<Long, RpcInvoke> requestMap = new ConcurrentHashMap<Long, RpcInvoke>(32);

	public void add(RpcInvoke future) {
		requestMap.put(future.getRequest().getRequestId(), future);
	}

	public RpcInvoke remove(Long requestId) {
		return requestMap.remove(requestId);
	}
}

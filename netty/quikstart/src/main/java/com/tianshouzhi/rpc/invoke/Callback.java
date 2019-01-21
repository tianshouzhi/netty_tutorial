package com.tianshouzhi.rpc.invoke;

import com.tianshouzhi.rpc.processor.Response;

public interface Callback {
	void onComplete(Request request, Response response);
}

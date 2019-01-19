package com.tianshouzhi.time;

import com.tianshouzhi.Response;

public interface Callback {
	void onComplete(Request request, Response response);
}

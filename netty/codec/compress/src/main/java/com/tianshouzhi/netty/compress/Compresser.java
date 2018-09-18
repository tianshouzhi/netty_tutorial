package com.tianshouzhi.netty.compress;

import java.io.IOException;

/**
 * Created by tianshouzhi on 2018/9/12.
 */
public interface Compresser {
	public byte[] compress(byte srcBytes[]) throws IOException;

	public byte[] unCompress(byte[] bytes) throws IOException;
}

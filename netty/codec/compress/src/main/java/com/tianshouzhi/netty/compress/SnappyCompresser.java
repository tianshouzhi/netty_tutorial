package com.tianshouzhi.netty.compress;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Created by tianshouzhi on 2018/9/12.
 */
public class SnappyCompresser implements Compresser {
	public byte[] compress(byte srcBytes[]) throws IOException {
		return Snappy.compress(srcBytes);
	}

	public byte[] unCompress(byte[] bytes) throws IOException {
		return Snappy.uncompress(bytes);
	}
}

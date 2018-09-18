package com.tianshouzhi.netty.compress;

import java.io.IOException;

/**
 * Created by tianshouzhi on 2018/9/13.
 */
public class Bzip2Compresser implements Compresser{
    public byte[] compress(byte[] srcBytes) throws IOException {
        return new byte[0];
    }

    public byte[] unCompress(byte[] bytes) throws IOException {
        return new byte[0];
    }
}

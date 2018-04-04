package com.tianshouzhi.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class BufferPutDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        print(buffer);
        byte H=0x48;
        byte e=0x65;
        byte l=0x6C;
        byte o=0x6F;
        buffer.put(H).put(e).put(l).put(l).put(o);
        print(buffer);
    }
    private static void print(Buffer buffer) {
        System.out.println("capacity="+buffer.capacity()
                +",limit="+buffer.limit()
                +",position="+buffer.position()
                +",hasRemaining:"+buffer.hasArray()
                +",remaining="+buffer.remaining()
                +",hasArray="+buffer.hasArray()
                +",isReadOnly="+buffer.isReadOnly()
                +",arrayOffset="+buffer.arrayOffset());
    }
}

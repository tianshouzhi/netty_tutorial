package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Created by tianshouzhi on 2018/4/27.
 */
public class HeapBufferTest {
	public static void main(String[] args) {

		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer(16);
        System.out.println("=====1、create a new ByteBuf, initial capacity is 16====");
        print(byteBuf);

        System.out.println("=====2、write 'helloworld'====");
        byteBuf.writeBytes("helloworld".getBytes());
        print(byteBuf);

        System.out.println("=====3、read first 5 bytes====");
        byte[] dst=new byte[5];
        byteBuf.readBytes(dst);
        print(byteBuf);

        System.out.println("=====4、discard read bytes====");
        byteBuf.discardReadBytes();
        print(byteBuf);
	}

	private static void print(ByteBuf byteBuf) {
		System.out.println("readerIndex:" + byteBuf.readerIndex() +
                ",writerIndex:" + byteBuf.writerIndex() +
                ",capacity:" + byteBuf.capacity()+
                ",readableBytes:"+byteBuf.readableBytes()+
                ",writableBytes:"+byteBuf.writableBytes());
	}
}

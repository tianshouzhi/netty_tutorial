package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * Created by tianshouzhi on 2018/4/26.
 */
public class UnpooledTest {

    @Test
    public void test(){
        ByteBuf buffer = Unpooled.buffer(16, 32);
        System.out.println("Unpooled.buffer:"+buffer.isDirect());
        System.out.println("Unpooled.hasArray:"+buffer.hasArray());

        ByteBuf directBuffer = Unpooled.directBuffer(16, 32);
        System.out.println("Unpooled.directBuffer:"+directBuffer.isDirect());
    }

    @Test
    public void test1(){
        ByteBuf buffer = Unpooled.buffer(16, 32);
        System.out.println("Unpooled.buffer:"+buffer.isDirect());

        Unpooled.directBuffer(16,32);
        System.out.println("Unpooled.directBuffer:"+buffer.isDirect());

        //...操作buffer...，后面的代码都添加到这里
//        buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
//        CharSequence charSequence = buffer.readCharSequence(5, CharsetUtil.ISO_8859_1);
//        System.out.println(charSequence);
//        buffer.discardReadBytes();
//        buffer.clear();
//        System.out.println("readerIndex:"+buffer.readerIndex());
//        System.out.println("writerIndex:"+buffer.writerIndex());
//        System.out.println("capacity:"+buffer.capacity());
//        System.out.println("maxCapacity:"+buffer.maxCapacity());
//        System.out.println("readableBytes:"+buffer.readableBytes());
//        System.out.println("isReadable:"+buffer.isReadable());
//        System.out.println("writableBytes:"+buffer.writableBytes());
//        System.out.println("isWritable:"+buffer.isWritable());
    }
}

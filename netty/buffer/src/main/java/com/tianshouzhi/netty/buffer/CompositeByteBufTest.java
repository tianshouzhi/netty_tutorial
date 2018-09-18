package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.CharsetUtil;
import org.junit.Test;

/**
 * Created by tianshouzhi on 2018/9/15.
 */
public class CompositeByteBufTest {
	@Test
	public void test() {
		CompositeByteBuf byteBufs = ByteBufAllocator.DEFAULT.compositeBuffer();


		ByteBuf body = ByteBufAllocator.DEFAULT.buffer(256);
        body.writeCharSequence("test composite byte buffer", CharsetUtil.UTF_8);

        ByteBuf lengthHeader = ByteBufAllocator.DEFAULT.buffer();
        lengthHeader.writeLong(body.readableBytes());
        byteBufs.addComponents(lengthHeader,body);

        System.out.println(byteBufs);
        for (ByteBuf byteBuf : byteBufs) {

        }
    }
}

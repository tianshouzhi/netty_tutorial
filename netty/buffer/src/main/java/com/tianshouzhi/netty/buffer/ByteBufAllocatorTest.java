package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.Test;

/**
 * Created by tianshouzhi on 2018/4/4.
 */
public class ByteBufAllocatorTest {
	@Test
	public void testDefault(){

		ByteBufAllocator unpooled = UnpooledByteBufAllocator.DEFAULT;

		System.out.println("------------" + unpooled + "-----------");
		System.out.println("buffer :" + unpooled.buffer().getClass().getSimpleName());
		System.out.println("ioBuffer :" + unpooled.ioBuffer().getClass().getSimpleName());
		System.out.println("heapBuffer :" + unpooled.heapBuffer().getClass().getSimpleName());
		System.out.println("directBuffer:" + unpooled.directBuffer().getClass().getSimpleName());

		ByteBufAllocator pooled = PooledByteBufAllocator.DEFAULT;
		pooled.heapBuffer();
		System.out.println("\n------------" + pooled + "-----------");
		System.out.println("buffer :" + pooled.buffer().getClass().getSimpleName());
		System.out.println("ioBuffer :" + pooled.ioBuffer().getClass().getSimpleName());
		System.out.println("heapBuffer :" + pooled.heapBuffer().getClass().getSimpleName());
		System.out.println("directBuffer:" + pooled.directBuffer().getClass().getSimpleName());
	}
}

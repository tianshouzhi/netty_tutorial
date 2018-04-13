package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * Created by tianshouzhi on 2018/4/4.
 */
public class ByteBufAllocatorTest {
	@Test
	public void test() {
		ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
		ByteBuf buffer = allocator.buffer();
		buffer.release();
		System.out.println(allocator);
		System.out.println(buffer);

	}
}

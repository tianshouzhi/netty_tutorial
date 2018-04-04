package com.tianshouzhi.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tianshouzhi on 2018/3/9.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) { // (1)
		System.out.println(ctx.channel().config());
		System.out.println(ctx.alloc());
		final ByteBuf time = ctx.alloc().buffer(4); // (2)
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

		final ChannelFuture f = ctx.writeAndFlush(time); // (3)
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert f == future;
				ctx.close();
			}
		}); // (4)
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBufAllocator allocator = ctx.alloc();
		ByteBuf buffer = allocator.buffer();
		buffer.writeBytes("your response".getBytes());
		ctx.writeAndFlush(buffer);
	}
}

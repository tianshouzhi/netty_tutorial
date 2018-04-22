package com.tianshouzhi.eventloop;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tianshouzhi on 2018/4/19.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	ExecutorService executor=Executors.newSingleThreadExecutor();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(Thread.currentThread() + "-->ClientHandler.channelActive");
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(Thread.currentThread() + "-->ClientHandler.channelRead");
		executor.submit(new EchoTask(ctx, msg));
	}

	class EchoTask implements Runnable{

		private final ChannelHandlerContext ctx;
		private final Object msg;

		public EchoTask(ChannelHandlerContext ctx, Object msg) {
			this.ctx = ctx;
			this.msg = msg;
		}

		@Override
		public void run() {
			ByteBuf request = (ByteBuf) msg;
			ByteBuf response = Unpooled.buffer();
			response.writeBytes("echo:".getBytes());
			response.writeBytes(request);

			ctx.writeAndFlush(response); // (1)
		}
	}
}

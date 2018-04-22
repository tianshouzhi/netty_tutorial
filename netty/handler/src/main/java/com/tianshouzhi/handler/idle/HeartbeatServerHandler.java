package com.tianshouzhi.handler.idle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by tianshouzhi on 2018/4/22.
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	private int heartbeatCount = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if ("PING".equals(msg)) {
			ByteBuf response = Unpooled.buffer();
			response.writeBytes(Unpooled.buffer().writeBytes("PONG\n".getBytes()));
			ctx.writeAndFlush(response); // (1)
			heartbeatCount++;
			System.out.println(" receive heartbeat from " + ctx.channel().remoteAddress() + ",count:" + heartbeatCount);
		} else {
			heartbeatCount = 0;
			super.channelRead(ctx, msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			// 读超时，关闭连接
			if (e.state() == IdleState.READER_IDLE) {
				ctx.close();
				System.out.println("READER_IDLE 读超时，关闭客户端连接");
			}
		}
	}
}

package com.tianshouzhi.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by tianshouzhi on 2018/4/14.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {// 1
		String request = (String) msg;
		String response = null;

		if ("QUERY TIME ORDER".equals(request)) {// 2
			response = new Date(System.currentTimeMillis()).toString();
		} else {
			response = "BAD REQUEST";
		}
		response = response + System.getProperty("line.separator");// 3
		ByteBuf resp = Unpooled.copiedBuffer(response.getBytes());// 4
		ctx.writeAndFlush(resp);// 5
	}
}

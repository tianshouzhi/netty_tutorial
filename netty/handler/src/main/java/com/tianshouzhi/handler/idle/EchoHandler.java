package com.tianshouzhi.handler.idle;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tianshouzhi on 2018/4/22.
 */
public class EchoHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String request= (String) msg;
        byte[] response = ("echo:" + request+"\n").getBytes();
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(response));
    }
}

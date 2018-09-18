package com.tianshouzhi.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tianshouzhi on 2018/3/9.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private byte[] req=("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();

    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf message = Unpooled.buffer(req.length);
        message.writeBytes(req);
        ctx.writeAndFlush(message);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("Now is:" + body);
        ctx.close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

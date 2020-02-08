package com.tianshouzhi.netty.server;

import com.tianshouzhi.netty.Request;
import com.tianshouzhi.netty.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive request:" + msg);
        if (msg instanceof Request) {
            Response response = new Response();
            String content = ((Request) msg).getContent();
            response.setContent("echo:" + content);
            ctx.writeAndFlush(response);// 5
        }
    }
}

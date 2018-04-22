package com.tianshouzhi.eventloop;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * Created by tianshouzhi on 2018/4/19.
 */
public class ServerHandler extends ChannelOutboundHandlerAdapter{
    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println(Thread.currentThread()+"-->ServerHandler.channelRead");
        ctx.bind(localAddress,promise);
    }
}

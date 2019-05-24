package com.tianshouzhi.netty.http2.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.util.ReferenceCountUtil;

public class CustomHttp2ServerHandler extends SimpleChannelInboundHandler<HttpMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpMessage msg) throws Exception {
        // If this handler is hit then no upgrade has been attempted and the client is just talking HTTP.
        System.err.println("Directly talking: " + msg.protocolVersion() + " (no upgrade was attempted)");
        ChannelPipeline pipeline = ctx.pipeline();
        ChannelHandlerContext thisCtx = pipeline.context(this);
        pipeline.addAfter(thisCtx.name(), null, new CustomHttp1Handler("Direct. No Upgrade Attempted."));
        pipeline.replace(this, null, new HttpObjectAggregator(16 * 1024));
        ctx.fireChannelRead(ReferenceCountUtil.retain(msg));
    }
}

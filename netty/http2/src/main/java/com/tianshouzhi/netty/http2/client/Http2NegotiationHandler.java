/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tianshouzhi.netty.http2.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;


import java.util.concurrent.TimeUnit;

import static io.netty.handler.logging.LogLevel.INFO;

/**
 * A handler that triggers the cleartext upgrade to HTTP/2 by sending an initial HTTP request.
 */
public  class Http2NegotiationHandler extends SimpleChannelInboundHandler<Http2Settings> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DefaultFullHttpRequest upgradeRequest =
                new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
        ctx.writeAndFlush(upgradeRequest);
        ctx.fireChannelActive();
        // Done with this handler, remove it from the pipeline.
        ctx.pipeline().remove(this);
        ctx.pipeline().addLast(new HttpResponseHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Http2Settings msg) throws Exception {
        System.out.println(msg);
        // Only care about the first settings message
        ctx.pipeline().remove(this);
    }
}

package com.tianshouzhi.netty.http2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http2.CleartextHttp2ServerUpgradeHandler;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.logging.LoggingHandler;

public class Http2Server {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        final NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        final NioEventLoopGroup childGroup = new NioEventLoopGroup();
        ChannelFuture future = server.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(buildCleartextHttp2ServerUpgradeHandler());
                        p.addLast(new CustomHttp2ServerHandler());
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(8080)
                .sync();

        future.channel().closeFuture().sync();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

    private static CleartextHttp2ServerUpgradeHandler buildCleartextHttp2ServerUpgradeHandler() {
        //1
        HttpServerCodec sourceCodec = new HttpServerCodec();
        //2
        HttpServerUpgradeHandler upgradeHandler = new HttpServerUpgradeHandler(sourceCodec, new CustomHttp2UpgradeCodecFactory());
        //3
        Http2ConnectionHandler http2ServerHandler = new CustomHttp2ConnectionHandlerBuilder().build();

        return new CleartextHttp2ServerUpgradeHandler(sourceCodec, upgradeHandler, http2ServerHandler);
    }
}


package com.tianshouzhi.netty.http2.know;

import com.tianshouzhi.netty.http2.server.CustomHttp2ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http2.*;

public class Http2KnowServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        final NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        final NioEventLoopGroup childGroup = new NioEventLoopGroup();
        ChannelFuture future = server.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline p = ch.pipeline();
//                        Http2Connection connection = new DefaultHttp2Connection(false);
//                        Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection,new DefaultHttp2ConnectionEncoder());
//                        DefaultHttp2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection,new DecoratingHttp2FrameWriter());
//                        Http2Settings initialSettings = new Http2Settings();

//                        Http2ConnectionHandler http2ConnectionHandler =new Http2ConnectionHandler(true,new Http2FrameLogger());
//                        p.addLast(http2ConnectionHandler);
//                        p.addLast(new CustomHttp2ServerHandler());
                    }
                })
                .bind(8080)
                .sync();

        future.channel().closeFuture().sync();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }
}

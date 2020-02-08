package com.tianshouzhi.netty.client;

import com.tianshouzhi.netty.Request;
import com.tianshouzhi.netty.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.SocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Client {
    private EventLoopGroup workerGroup = null;
    private Bootstrap bootstrap = null;
    private Channel channel;
    private BlockingQueue<Response> responseQueue;

    public Client() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup();
        this.responseQueue=new ArrayBlockingQueue<Response>(1);
    }

    public void connect(SocketAddress socketAddress) throws InterruptedException {
        bootstrap.group(workerGroup); // (2)
        bootstrap.channel(NioSocketChannel.class); // (3)
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000); // (4)
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ObjectEncoder());
                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if (msg instanceof Response) {
                            Response response = (Response) msg;
                            responseQueue.put(response);
                        }
                    }
                });

            }
        });
        ChannelFuture channelFuture = bootstrap.connect(socketAddress).sync();
        channel = channelFuture.channel();
    }

    public Response invoke(Request request)
            throws InterruptedException {
        //发送请求
        channel.writeAndFlush(request);
        //等待响应
        return responseQueue.take();
    }

    public void shutdown() {
        channel.close();
        workerGroup.shutdownGracefully();
    }
}

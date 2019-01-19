package com.tianshouzhi.time;

/**
 * Created by tianshouzhi on 2018/3/9.
 */

import com.tianshouzhi.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Discards any incoming data.
 */
public class RpcServer {

    private int port=8080;

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(new ClassResolver() {
                                public Class<?> resolve(String className) throws ClassNotFoundException {
                                    return Class.forName(className);
                                }
                            }));

                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if(msg instanceof Request){
                                        System.out.println("receive request:"+msg);
                                        String requestBody = (String) ((Request) msg).getRequestBody();

                                        String responseBody = "BAD REQUEST";
                                        if ("QUERY TIME ORDER".equals(requestBody)) {// 2
                                            responseBody = new Date(System.currentTimeMillis()).toString();
                                        }

                                        Response response=new Response();
                                        response.setRequestId(((Request) msg).getRequestId());
                                        response.setResponseBody(responseBody);
                                        ctx.writeAndFlush(response);// 5
                                    }
                                }
                            });
                        }
                    });

            ChannelFuture f = b.bind(new InetSocketAddress("127.0.0.1",port)).sync(); // (5)
            System.out.println("TimeServer Started on 8080...");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new RpcServer().run();
    }
}

package com.tianshouzhi.rpc;

/**
 * Created by tianshouzhi on 2018/3/9.
 */

import com.tianshouzhi.rpc.invoke.Request;
import com.tianshouzhi.rpc.processor.Response;
import com.tianshouzhi.rpc.processor.RpcProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * Discards any incoming data.
 */
public class RpcServer {

	private String ip;

	private int port;

	private RpcProcessor rpcProcessor;

	public RpcServer(String ip, int port, RpcProcessor rpcProcessor) {
		this.ip = ip;
		this.port = port;
		this.rpcProcessor = rpcProcessor;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
			      .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
				      @Override
				      public void initChannel(SocketChannel ch) throws Exception {
					      ch.pipeline().addLast(new ObjectEncoder());
					      ch.pipeline().addLast(new ObjectDecoder(new ClassResolver() {
						      public Class<?> resolve(String className) throws ClassNotFoundException {
							      return Class.forName(className);
						      }
					      }));

					      ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						      @Override
						      public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							      if (msg instanceof Request) {
								      Object responseBody = rpcProcessor.process(((Request) msg).getRequestBody());
								      Response response = new Response();
								      response.setRequestId(((Request) msg).getRequestId());
								      response.setResponseBody(responseBody);
								      ctx.writeAndFlush(response);// 5
							      }
						      }
					      });
				      }
			      });

			ChannelFuture f = b.bind(new InetSocketAddress(ip, port)).sync(); // (5)
			System.out.println("RpcServer Started on " + port + "...");
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}

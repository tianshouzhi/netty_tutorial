package com.tianshouzhi.netty.codec;

import com.tianshouzhi.netty.codec.object_encoder.Request;
import com.tianshouzhi.netty.codec.object_encoder.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Date;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by tianshouzhi on 2018/9/9.
 */
public class NettyClient {

	private String host;

	private int port;

	private Channel channel;

	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	private SynchronousQueue<Response> queue = new SynchronousQueue<Response>();

	public NettyClient(String host, int port) throws InterruptedException {
		this.host = host;
		this.port = port;
		this.workerGroup = new NioEventLoopGroup();
		this.connect();
	}

	private void connect() throws InterruptedException {
		Bootstrap b = new Bootstrap(); // (1)
		b.group(workerGroup); // (2)
		b.channel(NioSocketChannel.class); // (3)
		b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		b.handler(new ChannelInitializer<SocketChannel>() {
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
						queue.put((Response) msg);
					}
				});
			}
		});
		// Start the client.
		this.channel = b.connect(host, port).sync().channel(); // (5)
	}

	public void close() throws InterruptedException {
		try {
			// Wait until the connection is closed.
			channel.close().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	public synchronized Response send(Request request) throws Exception {
		channel.writeAndFlush(request);
		return queue.take();
	}

	public static void main(String[] args) throws Exception {
		NettyClient client = new NettyClient("127.0.0.1", 8080);
		try {
			Request request = new Request();
			request.setRequestTime(new Date());
			request.setRequest("i am a request");
			Response response = client.send(request);
			System.out.println(response);
		} finally {
			client.close();
		}
	}
}

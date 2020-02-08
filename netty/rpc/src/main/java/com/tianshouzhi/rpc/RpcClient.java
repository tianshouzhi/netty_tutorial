package com.tianshouzhi.rpc;

import com.tianshouzhi.rpc.invoke.*;
import com.tianshouzhi.rpc.processor.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tianshouzhi on 2018/3/9.
 */
public class RpcClient {

	private AttributeKey<Object> REQUEST_CACHE_KEY = AttributeKey.newInstance("request-cache");

	private EventLoopGroup workerGroup = null;

	private Bootstrap bootstrap = null;

	private Map<SocketAddress, List<Channel>> connectionPool = new ConcurrentHashMap<SocketAddress, List<Channel>>();

	private AtomicLong idGenerator = new AtomicLong(0);

	public RpcClient() {
		this.bootstrap = new Bootstrap();
		this.workerGroup = new NioEventLoopGroup();
	}

	public void connect(SocketAddress socketAddress) throws InterruptedException {
		bootstrap.group(workerGroup); // (2)
		bootstrap.channel(NioSocketChannel.class); // (3)
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000); // (4)
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.attr(REQUEST_CACHE_KEY).set(new RpcInvokeQueue());
				ch.pipeline().addLast(new ObjectEncoder());
				ch.pipeline()
				      .addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));
				ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						if (msg instanceof Response) {
							Response response = (Response) msg;
							RpcInvokeQueue rpcInvokeQueue = (RpcInvokeQueue) ctx.channel().attr(REQUEST_CACHE_KEY).get();
							RpcInvoke rpcInvoke = rpcInvokeQueue.remove(response.getRequestId());
							rpcInvoke.setResponse(response);
						}
					}
				});

			}
		});
		ChannelFuture channelFuture = bootstrap.connect(socketAddress).sync();
		Channel channel = channelFuture.channel();
		SocketAddress remoteAddress = channel.remoteAddress();
		List<Channel> channels = connectionPool.get(remoteAddress);
		if (channels == null) {
			synchronized (this) {
				channels = new ArrayList<Channel>();
				connectionPool.put(remoteAddress, channels);
			}
		}
		channels.add(channel);
	}

	public <T> T invokeSync(SocketAddress address, Object requestBody, int timeoutMillis, Class<T> clazz)
	      throws InterruptedException {
		long createTime = System.currentTimeMillis();

		//选择连接
		List<Channel> channels = connectionPool.get(address);
		Channel channel = channels.get(0);

		//构造请求对象，并添加到rpc调用队列
		Request request = new Request(idGenerator.incrementAndGet(), requestBody, createTime, timeoutMillis);
		RpcInvokeQueue rpcInvokeQueue = (RpcInvokeQueue) channel.attr(REQUEST_CACHE_KEY).get();
		RpcInvokeFuture invokeFuture = new RpcInvokeFuture(request);
		rpcInvokeQueue.add(invokeFuture);

		//发送请求
		channel.writeAndFlush(request);

		//等待响应
		return (T) invokeFuture.getResponseBody();
	}

	public RpcInvokeFuture invokeFuture(SocketAddress address, Object requestBody, int timeoutMillis) throws InterruptedException {
		long createTime = System.currentTimeMillis();

		//选择连接
		List<Channel> channels = connectionPool.get(address);
		Channel channel = channels.get(0);

		//构造请求对象，并添加到rpc调用队列
		Request request = new Request(idGenerator.incrementAndGet(), requestBody, createTime, timeoutMillis);
		RpcInvokeFuture invokeFuture = new RpcInvokeFuture(request);
		RpcInvokeQueue rpcInvokeQueue = (RpcInvokeQueue) channel.attr(REQUEST_CACHE_KEY).get();
		rpcInvokeQueue.add(invokeFuture);

		//发送请求
		channel.writeAndFlush(request).sync();
		return invokeFuture;
	}

	public void invokeCallback(SocketAddress address, String requestBody, Callback callback)
	      throws InterruptedException {
		long createTime = System.currentTimeMillis();

		//选择连接
		List<Channel> channels = connectionPool.get(address);
		Channel channel = channels.get(0);

		//构造请求对象，并添加到rpc调用队列
		Request request = new Request(idGenerator.incrementAndGet(), requestBody, createTime);
		RpcInvokeCallback invokeFuture = new RpcInvokeCallback(request,callback);
		RpcInvokeQueue rpcInvokeQueue = (RpcInvokeQueue) channel.attr(REQUEST_CACHE_KEY).get();
		rpcInvokeQueue.add(invokeFuture);

		//发送请求
		channel.writeAndFlush(request).sync();
	}

	public void close(SocketAddress socketAddress) {
		List<Channel> channels = connectionPool.get(socketAddress);
		if (channels != null) {
			for (Channel channel : channels) {
				channel.close();
			}
		}
		connectionPool.remove(socketAddress);
	}

	public void shutdown() {
		for (SocketAddress socketAddress : connectionPool.keySet()) {
			close(socketAddress);
		}
		workerGroup.shutdownGracefully();
	}
}
package com.tianshouzhi.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServer {

	private static BlockingQueue<SocketChannel> idleQueue = new LinkedBlockingQueue<SocketChannel>();

	private static BlockingQueue<Future<SocketChannel>> workingQueue = new LinkedBlockingQueue<Future<SocketChannel>>();

	private static ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void main(String[] args) throws IOException, InterruptedException {
		TimeServer timeServer = new TimeServer();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(8080));
        System.out.println("TimeServer Started on 8080...");
		while (true) {
			SocketChannel socketChannel = ssc.accept();
			if (socketChannel == null) {
				continue;
			} else {
				socketChannel.configureBlocking(false);
				timeServer.idleQueue.add(socketChannel);
			}
			runIdleQueue();
			checkWorkingQueue();
		}
	}

	private static void runIdleQueue() throws InterruptedException, IOException {
		// task1：迭代当前idleQueue中的SocketChannel，提交到线程池中执行任务，并将其移到workingQueue中
		for (int i = 0; i < idleQueue.size(); i++) {
			SocketChannel socketChannel = idleQueue.poll();
			if (socketChannel != null) {
				Future<SocketChannel> result = executor.submit(new TimeServerHandleTask(socketChannel), socketChannel);
				workingQueue.put(result);
			}
		}
	}
	
	private static void checkWorkingQueue() throws InterruptedException, IOException {
		// task2：迭代当前workingQueue中的SocketChannel，如果任务执行完成，将其移到idleQueue中
		for (int i = 0; i < workingQueue.size(); i++) {
			Future<SocketChannel> future = workingQueue.poll();
			if (!future.isDone()) {
				workingQueue.put(future);
				continue;
			}

			SocketChannel channel = null;
			try {
				channel = future.get();
				idleQueue.put(channel);
			} catch (ExecutionException e) {
				// 如果future.get()抛出异常，关闭SocketChannel，不再放回idleQueue
				channel.close();
				e.printStackTrace();
			}
		}
	}
}

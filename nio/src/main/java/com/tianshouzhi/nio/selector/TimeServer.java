package com.tianshouzhi.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServer {

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void main(String[] args) throws IOException, InterruptedException {
		TimeServer timeServer = new TimeServer();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(8080));
        System.out.println("TimeServer Started on 8080...");
		ssc.configureBlocking(false);
		Selector selector = Selector.open();
		// 注册ServerSocketChannel
		SelectionKey sscSelectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			int readyCount = selector.select(1000);

			if (readyCount == 0) {
				continue;
			}

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey selectionKey = keyIterator.next();
				if (selectionKey.isValid()) {
					// 表示ServerSocketChannel
					if (selectionKey.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
						SocketChannel socketChannel = server.accept();
						if (socketChannel != null) {
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						}
					}

					// 表示SocketChannel
					if (selectionKey.isReadable()) {
						timeServer.executor.submit(new TimeServerHandleTask((SocketChannel) selectionKey.channel()));
					}
					keyIterator.remove();
				}
			}
		}
	}
}

package com.tianshouzhi.nio.channel;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServerHandleTask implements Runnable {
	SocketChannel socketChannel;

	public TimeServerHandleTask(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		try {

			ByteBuffer requestBuffer = ByteBuffer.allocate("GET CURRENT TIME".length());

			int bytesRead = socketChannel.read(requestBuffer);

			if (bytesRead <= 0) {
				return;
			}

			while (requestBuffer.hasRemaining()) {
				socketChannel.read(requestBuffer);
			}

			String requestStr = new String(requestBuffer.array());

			if (!"GET CURRENT TIME".equals(requestStr)) {
				String bad_request = "BAD_REQUEST";
				ByteBuffer responseBuffer = ByteBuffer.allocate(bad_request.length());
                responseBuffer.put(bad_request.getBytes());
                responseBuffer.flip();
                socketChannel.write(responseBuffer);
			} else {
                String timeStr = Calendar.getInstance().getTime().toLocaleString();
                ByteBuffer responseBuffer = ByteBuffer.allocate(timeStr.length());
                responseBuffer.put(timeStr.getBytes());
                responseBuffer.flip();
				socketChannel.write(responseBuffer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

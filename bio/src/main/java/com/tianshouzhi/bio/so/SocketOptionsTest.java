package com.tianshouzhi.bio.so;

import org.junit.Test;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by tianshouzhi on 2018/8/19.
 */
public class SocketOptionsTest {

	@Test
	public void testPrintAll() throws Exception {
		Socket client = new Socket("115.239.210.27", 80);
		System.out.println("SO_KEEPALIVE:" + client.getKeepAlive());
		System.out.println("SO_RCVBUF:" + client.getReceiveBufferSize());
		System.out.println("SO_SNDBUF:" + client.getSendBufferSize());
		System.out.println("TCP_NODELAY:" + client.getTcpNoDelay());
		System.out.println("SO_TIMEOUT:" + client.getSoTimeout());
		System.out.println("SO_REUSEADDR:" + client.getReuseAddress());
		System.out.println("SO_LINGER:" + client.getSoLinger());
		System.out.println("SO_OOBINLINE:" + client.getOOBInline());
	}

	@Test
	public void testReuseAddress() throws Exception {
		Socket client = new Socket();
//		client.setReuseAddress(true);
//		client.bind(new InetSocketAddress(3456));
		client.connect(new InetSocketAddress("115.28.171.77", 80));

		Thread.sleep(1000);
		client.close();
		Thread.sleep(1000);
	}

	@Test
	public void testKeepAlive() {

	}

	@Test
	public void testTcpNodelay() {

	}

	@Test
	public void testSendAndRecvBufferSize() throws Exception {
		Socket client = new Socket("115.239.210.27", 80);
		System.out.println("SO_RCVBUF:" + client.getReceiveBufferSize());
		System.out.println("SO_SNDBUF:" + client.getSendBufferSize());
		client.close();

		client = new Socket("115.28.171.77", 80);
		System.out.println("SO_RCVBUF:" + client.getReceiveBufferSize());
		System.out.println("SO_SNDBUF:" + client.getSendBufferSize());
	}

	@Test
	public void testSoLinger() {

	}

	@Test
	public void testOOBInline() {

	}

	@Test
	public void testSoTimeout() {

	}

	@Test
	public void testReuseAddress1() throws Exception {
		Socket client = new Socket();
		client.setReuseAddress(true);
		client.bind(new InetSocketAddress(3456));
		client.connect(new InetSocketAddress("115.239.210.27", 80));
		InputStream inputStream = client.getInputStream();
		// Thread.sleep(20000);
		client.close();
	}
}

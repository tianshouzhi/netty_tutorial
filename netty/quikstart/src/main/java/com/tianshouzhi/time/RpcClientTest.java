package com.tianshouzhi.time;

import com.tianshouzhi.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

public class RpcClientTest {
	private static RpcClient client = null;

	private static SocketAddress address = new InetSocketAddress("127.0.0.1", 8080);

	private static String requestBody = "QUERY TIME ORDER";

	private static int timeoutMillis = 3000;

	@BeforeClass
	public static void beforeClass() throws InterruptedException {
		client = new RpcClient();
		client.connect(address);
	}

	@Test
	public void testInvokeSync() throws InterruptedException {
		String response = client.invokeSync(address, requestBody, timeoutMillis, String.class);
		System.out.println(response);
	}

	@Test
	public void testInvokeCallback() throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		final Thread currentThread = Thread.currentThread();
		client.invokeCallback(address, requestBody, new Callback() {
			public void onComplete(Request request, Response response) {
				Thread callbackThread = Thread.currentThread();
				assert currentThread != callbackThread;

				System.out.println(response);
				countDownLatch.countDown();

			}
		});
		countDownLatch.await();
	}

	@Test
	public void testInvokeFuture() throws InterruptedException {
        System.out.println(new Timestamp(System.currentTimeMillis()).toString());
        RpcInvokeFuture invokeFuture = client.invokeFuture(address, requestBody, timeoutMillis);
        System.out.println(new Timestamp(System.currentTimeMillis()).toString());
        System.out.println(invokeFuture.getResponseBody());
        System.out.println(new Timestamp(System.currentTimeMillis()).toString());

    }

	@AfterClass
	public static void afterClass() {
		client.shutdown();
	}
}

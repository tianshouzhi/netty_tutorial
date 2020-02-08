package com.tianshouzhi.rpc;

import com.tianshouzhi.rpc.invoke.Callback;
import com.tianshouzhi.rpc.invoke.Request;
import com.tianshouzhi.rpc.invoke.RpcInvokeFuture;
import com.tianshouzhi.rpc.processor.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

public class RpcClientTest {
    private static RpcClient client = null;
    private static SocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
    private static int timeoutMillis = 3000;
    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        client = new RpcClient();
        client.connect(address);
    }
    @Test
    public void testInvokeSync() throws InterruptedException {
        String response = client.invokeSync(address, "i am a sync request", timeoutMillis, String.class);
        System.out.println(response);
    }
    @Test
    public void testInvokeCallback() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Thread currentThread = Thread.currentThread();
        client.invokeCallback(address, "i am a callback request", new Callback() {
            public void onComplete(Request request, Response response) {
                Thread callbackThread = Thread.currentThread();
                assert currentThread != callbackThread;
                System.out.println(response.getResponseBody());
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
    @Test
    public void testInvokeFuture() throws InterruptedException {
        RpcInvokeFuture invokeFuture = client.invokeFuture(address, "i am a future request", timeoutMillis);
        Object responseBody = invokeFuture.getResponseBody();
        System.out.println(responseBody);
    }
    @AfterClass
    public static void afterClass() {
        client.shutdown();
    }
}


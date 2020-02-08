package com.tianshouzhi.netty;

import com.tianshouzhi.netty.client.Client;
import org.junit.Test;

import java.net.InetSocketAddress;

public class ClientTest {
    @Test
    public void test() throws InterruptedException {
        Client client = new Client();
        client.connect(new InetSocketAddress("localhost", 8888));
        Request request1 = new Request();
        request1.setContent("tianshouzhi");
        Response response1 = client.invoke(request1);
        System.out.println("receive resp:" + response1);

        Request request2 = new Request();
        request2.setContent("wangxiaoxiao");
        Response response2 = client.invoke(request2);
        System.out.println("receive resp:" + response2);

        client.shutdown();
    }
}

package com.tianshouzhi.bio.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        Socket client = new Socket();
        client.setSendBufferSize(8192);
        client.setReceiveBufferSize(8192);
        client.connect(new InetSocketAddress("localhost", 8080));

        System.out.println(client.getReceiveBufferSize());

        OutputStream out = client.getOutputStream();
        out.write("GET CURRENT TIME".getBytes());
        out.flush();

        TimeUnit.MINUTES.sleep(10);

    }
}

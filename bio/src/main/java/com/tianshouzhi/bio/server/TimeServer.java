package com.tianshouzhi.bio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket();
        server.setReceiveBufferSize(8192);
        server.bind(new InetSocketAddress(8080));

        System.out.println("TimeServer Started on 8080...");

        while (true) {
            Socket client = server.accept();
            TimeUnit.MINUTES.sleep(10);
        }
    }
}
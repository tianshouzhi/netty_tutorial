package com.tianshouzhi.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServer {
    public static void main(String[] args) {
        ServerSocket server=null;
        try {
            server=new ServerSocket(8080);
            System.out.println("TimeServer Started on 8080...");
            while (true){
                Socket client = server.accept();
                new Thread(new TimeServerHandler(client)).start();//每次接收到一个新的客户端连接，启动一个新的线程来处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
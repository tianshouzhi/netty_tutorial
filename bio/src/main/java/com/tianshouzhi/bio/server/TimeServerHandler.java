package com.tianshouzhi.bio.server;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianshouzhi on 2018/3/25.
 */
public class TimeServerHandler implements Runnable {
    private Socket client;

    public TimeServerHandler(Socket client) {
        this.client = client;
    }


    @Override
    public void run() {

    }
}

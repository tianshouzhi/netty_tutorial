package com.tianshouzhi.netty;

import com.tianshouzhi.netty.server.Server;
import org.junit.Test;

public class ServerTest {
    @Test
    public void run() throws Exception {
        Server server = new Server("localhost", 8888);
        server.start();
    }
}

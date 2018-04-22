package com.tianshouzhi.handler.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by tianshouzhi on 2018/4/22.
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            //读超时，关闭连接
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
                System.out.println("READER_IDLE 读超时，关闭客户端连接");
            }
        }
    }
}

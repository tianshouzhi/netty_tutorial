package com.tianshouzhi.handler.idle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

/**
 * Created by tianshouzhi on 2018/4/22.
 */
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            //读超时，关闭连接
            if (e.state() == IdleState.WRITER_IDLE) {
                ByteBuf buf = ctx.alloc().buffer(5);
                buf.writeBytes("PING\n".getBytes());
                ctx.writeAndFlush(buf);
                System.out.println(new Date().toLocaleString()+":WRITER_IDLE 写超时，发送心跳包");
            }
        }
    }
}

package com.tianshouzhi.netty.http2.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http2.*;
import io.netty.util.AsciiString;

import static io.netty.handler.logging.LogLevel.INFO;

public class Http2ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        //1 HttpServerCodec
        HttpServerCodec httpServerCodec = new HttpServerCodec();

        //2 Http2ConnectionHandler
        final Http2ConnectionHandler http2connectionHandler = new Http2ConnectionHandlerBuilder()
                .frameLogger(new Http2FrameLogger(INFO))
                .frameListener(new Http2FrameListenerImpl())
                .build();

        //3 HttpServerUpgradeHandler
        HttpServerUpgradeHandler upgradeHandler = new HttpServerUpgradeHandler(
                httpServerCodec,
                new HttpServerUpgradeHandler.UpgradeCodecFactory() {
                    @Override
                    public HttpServerUpgradeHandler.UpgradeCodec newUpgradeCodec(CharSequence protocol) {
                        //判断protocol值是否为h2c，如果是，返回Http2ServerUpgradeCodec
                        if (AsciiString.contentEquals(Http2CodecUtil.HTTP_UPGRADE_PROTOCOL_NAME, protocol)) {
                            return new Http2ServerUpgradeCodec(http2connectionHandler);
                        } else {
                            return null;
                        }
                    }
                });

        //4
        CleartextHttp2ServerUpgradeHandler serverHandler =
                new CleartextHttp2ServerUpgradeHandler(
                        httpServerCodec,
                        upgradeHandler,
                        http2connectionHandler);

        ch.pipeline().addLast(serverHandler);
    }
}

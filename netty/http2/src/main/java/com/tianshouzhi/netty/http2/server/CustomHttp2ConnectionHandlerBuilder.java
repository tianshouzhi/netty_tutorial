package com.tianshouzhi.netty.http2.server;

import io.netty.handler.codec.http2.*;

import static io.netty.handler.logging.LogLevel.INFO;

public final class CustomHttp2ConnectionHandlerBuilder
        extends AbstractHttp2ConnectionHandlerBuilder<CustomHttp2ConnectionHandler, CustomHttp2ConnectionHandlerBuilder> {

    private static final Http2FrameLogger logger = new Http2FrameLogger(INFO, CustomHttp2ConnectionHandler.class);

    public CustomHttp2ConnectionHandlerBuilder() {
        frameLogger(logger);
    }

    @Override
    public CustomHttp2ConnectionHandler build() {
        return super.build();
    }

    @Override
    protected CustomHttp2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder,
                                                 Http2Settings initialSettings) {
        CustomHttp2ConnectionHandler handler = new CustomHttp2ConnectionHandler(decoder, encoder, initialSettings);
        frameListener(handler);
        return handler;
    }
}

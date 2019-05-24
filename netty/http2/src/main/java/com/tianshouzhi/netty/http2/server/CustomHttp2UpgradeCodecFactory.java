package com.tianshouzhi.netty.http2.server;

import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.Http2ServerUpgradeCodec;
import io.netty.util.AsciiString;

public class CustomHttp2UpgradeCodecFactory implements HttpServerUpgradeHandler.UpgradeCodecFactory{
    @Override
    public HttpServerUpgradeHandler.UpgradeCodec newUpgradeCodec(CharSequence protocol) {
        //判断protocol值是否为h2c，如果是，返回Http2ServerUpgradeCodec
        if (AsciiString.contentEquals(Http2CodecUtil.HTTP_UPGRADE_PROTOCOL_NAME, protocol)) {
            return new Http2ServerUpgradeCodec(new CustomHttp2ConnectionHandlerBuilder().build());
        } else {
            return null;
        }
    }
}

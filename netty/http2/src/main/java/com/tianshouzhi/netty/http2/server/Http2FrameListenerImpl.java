package com.tianshouzhi.netty.http2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.buffer.Unpooled.unreleasableBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class Http2FrameListenerImpl extends Http2FrameAdapter {

    @Override
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
        System.out.println("-------------onDataRead---------------");
        CharSequence charSequence = data.getCharSequence(data.readerIndex(), data.readableBytes(), Charset.defaultCharset());
        System.out.println(charSequence);
        int i = super.onDataRead(ctx, streamId, data, padding, endOfStream);
        Http2Headers headers = new DefaultHttp2Headers().status(OK.codeAsText());
//        encoder().writeHeaders(ctx, streamId, headers, 0, false, ctx.newPromise());
//        encoder().writeData(ctx, streamId, payload, 0, true, ctx.newPromise());
        Http2ConnectionHandler handler = (Http2ConnectionHandler) ctx.handler();
        handler.encoder().writeHeaders(ctx, streamId, headers, 0, false, ctx.newPromise());
        handler.encoder().writeData(ctx, streamId, Unpooled.wrappedBuffer("Hello World - via HTTP/2".getBytes()), 0, true, ctx.newPromise());
        return i;

    }

    @Override
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
        super.onHeadersRead(ctx, streamId, headers, padding, endStream);
        System.out.println("-------------onHeadersRead---------------");
    }

    @Override
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
        super.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
        System.out.println("-------------onHeadersRead---------------");
        System.out.println(headers);
    }

    @Override
    public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive) throws Http2Exception {
        super.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
    }

    @Override
    public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
        super.onRstStreamRead(ctx, streamId, errorCode);
    }

    @Override
    public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception {
        super.onSettingsAckRead(ctx);
        System.out.println("-------------onSettingsAckRead---------------");
    }

    @Override
    public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
        super.onSettingsRead(ctx, settings);
        System.out.println("-------------onSettingsRead---------------");
        System.out.println(settings);
    }

    @Override
    public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
        super.onPingRead(ctx, data);
    }

    @Override
    public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception {
        super.onPingAckRead(ctx, data);
    }

    @Override
    public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
        super.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
    }

    @Override
    public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception {
        super.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
        System.out.println("-------------onGoAwayRead---------------");
    }

    @Override
    public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement) throws Http2Exception {
        super.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
    }

    @Override
    public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) {
        super.onUnknownFrame(ctx, frameType, streamId, flags, payload);
    }
}

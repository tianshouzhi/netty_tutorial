/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tianshouzhi.netty.http2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.logging.LogLevel.INFO;

/**
 * An HTTP2 client that allows you to send HTTP2 frames to a server. Inbound and outbound frames are
 * logged. When run from the command-line, sends a single HEADERS frame to the server and gets back
 * a "Hello World" response.
 */
public final class Http2Client {

    private String host;
    private int port;
    private Channel channel;
    private EventLoopGroup workerGroup;

    public Http2Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        this.workerGroup = new NioEventLoopGroup();
        final Http2NegotiationHandler negotiationHandler = new Http2NegotiationHandler();
        // Configure the client.
        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        Http2Connection connection = new DefaultHttp2Connection(false);
                        HttpToHttp2ConnectionHandler connectionHandler = new HttpToHttp2ConnectionHandlerBuilder()
                                .frameListener(new DelegatingDecompressorFrameListener(
                                        connection,
                                        new InboundHttp2ToHttpAdapterBuilder(connection)
                                                .maxContentLength(Integer.MAX_VALUE)
                                                .propagateSettings(true)
                                                .build()))
                                .frameLogger(new Http2FrameLogger(INFO))
                                .connection(connection)
                                .build();

                        HttpClientCodec sourceCodec = new HttpClientCodec();
                        Http2ClientUpgradeCodec upgradeCodec = new Http2ClientUpgradeCodec(connectionHandler);
                        ch.pipeline().addLast(sourceCodec);
                        ch.pipeline().addLast(new HttpClientUpgradeHandler(sourceCodec, upgradeCodec, 65536));
                        ch.pipeline().addLast(negotiationHandler);
                    }
                });

        // Start the client.
        this.channel = bootstrap.connect().syncUninterruptibly().await().channel();

        System.out.println("Connected to [" + host + ':' + port + ']');

//        negotiationHandler.awaitSettings(5, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(5);
    }

    private void sendRequest(FullHttpRequest request) throws InterruptedException {
        channel.writeAndFlush(request).sync().await();
    }

    public void close() {
        // Wait until the connection is closed.
        channel.close().syncUninterruptibly();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        Http2Client http2Client = new Http2Client("localhost", 8080);
        http2Client.start();

        // Create a simple GET request.
        FullHttpRequest getRequest = new DefaultFullHttpRequest(HTTP_1_1, GET, "/whatever", Unpooled.EMPTY_BUFFER);
        getRequest.headers().add(HttpHeaderNames.HOST, "localhost");
        getRequest.headers().add(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), HttpScheme.HTTP.name());
        getRequest.headers().add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        getRequest.headers().add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.DEFLATE);
        http2Client.sendRequest(getRequest);

        // Create a simple POST request with a body.
        FullHttpRequest postRequest = new DefaultFullHttpRequest(HTTP_1_1, POST, "/",
                wrappedBuffer("TEST POST Request".getBytes(CharsetUtil.UTF_8)));
        postRequest.headers().add(HttpHeaderNames.HOST, "localhost");
        postRequest.headers().add(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), HttpScheme.HTTP.name());
        postRequest.headers().add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        postRequest.headers().add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.DEFLATE);
        http2Client.sendRequest(postRequest);

        http2Client.close();
    }
}

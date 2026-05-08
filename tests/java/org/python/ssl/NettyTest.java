package org.python.ssl;

import org.junit.Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NettyTest {

    @Test
    public void test() throws Exception {
        doIt();
    }

    static void doIt() throws Exception {
        String host = "self-signed.pythontest.net";
        int port = 443;

        // 1. SslContext erstellen (erlaubt TLS 1.2 und 1.3)
        final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    // SslHandler muss ZUERST kommen
                    p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                    p.addLast(new SimpleChannelInboundHandler<Object>() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            System.out.println("TCP Verbindung steht. Handshake läuft...");
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                            System.out.println("Daten empfangen!");
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            cause.printStackTrace();
                            ctx.close();
                        }
                    });
                }
            });

            System.out.println("Verbinde mit " + host + "...");
            b.connect(host, port).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}

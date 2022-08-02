package com.zzf.client;

import com.zzf.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

/**
 * @author zzf
 * @date 2022/7/30 11:09 上午
 */
public class NettyClient {

    public void connent(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new SimpleClientHandler());
                            socketChannel.pipeline().addLast(new StringEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().writeAndFlush("hello server");
            channelFuture.channel().writeAndFlush("\r\n");
            channelFuture.channel().closeFuture().sync();
            Object msg = channelFuture.channel().attr(AttributeKey.valueOf("msg")).get();
            System.out.println("服务器返回数据：" + msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyClient().connent(8080, "localhost");
    }
}

package com.zzf.client;

import com.alibaba.fastjson.JSONObject;
import com.zzf.future.DefaultFuture;
import com.zzf.handler.SimpleClientHandler;
import com.zzf.model.ClientRequest;
import com.zzf.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author zzf
 * @date 2022/7/30 4:05 下午
 */
public class TcpClient {

    static final Bootstrap bootstrap = new Bootstrap();
    static ChannelFuture channelFuture = null;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new SimpleClientHandler());
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                });
        int port = 8080;
        String host = "localhost";
        try {
            channelFuture = bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }

    public static Response send(ClientRequest request) {
        channelFuture.channel().writeAndFlush(JSONObject.toJSONString(request));
        channelFuture.channel().writeAndFlush("\r\n");
        DefaultFuture future = new DefaultFuture(request);
        return future.get();
    }
}

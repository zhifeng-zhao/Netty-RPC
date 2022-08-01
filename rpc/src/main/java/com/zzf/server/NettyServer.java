package com.zzf.server;

import com.zzf.client.constant.Constants;
import com.zzf.client.factory.ZookeeperFactory;
import com.zzf.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zzf
 * @date 2022/7/29 7:45 下午
 */
public class NettyServer {

    public void bind() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new SimpleServerHandler());
                            socketChannel.pipeline().addLast(new StringEncoder());
                        }
                    });
            int port = 8081;
            int weight = 1;
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            CuratorFramework curatorFramework = ZookeeperFactory.create();
            InetAddress address = InetAddress.getLocalHost();
            curatorFramework.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(Constants.SERVER_PATH + address.getHostAddress() + "#" + port + "#" + weight + "#");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer().bind();
    }
}

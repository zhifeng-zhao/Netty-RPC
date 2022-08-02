package com.zzf.client.core;

import com.zzf.client.constant.Constants;
import com.zzf.client.factory.ZookeeperFactory;
import com.zzf.client.handler.SimpleClientHandler;
import com.zzf.client.parm.ClientRequest;
import com.zzf.client.parm.Response;
import com.alibaba.fastjson.JSONObject;
import com.zzf.client.watch.ServerWatch;
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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zzf
 * @date 2022/7/30 4:05 下午
 */
public class TcpClient {

    public static final Bootstrap bootstrap = new Bootstrap();
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
                        socketChannel.pipeline().addLast(new SimpleClientHandler());
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                });
        int port = 8080;
        String host = "localhost";
        CuratorFramework curatorFramework = ZookeeperFactory.create();
        try {
            List<String> paths = curatorFramework.getChildren().forPath(Constants.SERVER_PATH);
            // zk监听服务器
            CuratorWatcher serverWatch = new ServerWatch();
            curatorFramework.getChildren().usingWatcher(serverWatch).forPath(Constants.SERVER_PATH);
            paths.forEach(path -> {
                String[] str = path.split("#");
                ChannelManager.realServerPath.add(str[0] + ":" + str[1]);
                // 权重
                for (int i = 0; i < Integer.parseInt(str[2]); i++) {
                    ChannelManager.add(TcpClient.bootstrap.connect(str[0], Integer.parseInt(str[1])));
                }
            });
            if (ChannelManager.realServerPath.size() > 0) {
                String[] addr = ChannelManager.realServerPath.toArray()[0].toString().split(":");
                host = addr[0];
                port = Integer.parseInt(addr[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            channelFuture = bootstrap.connect(host, port).sync();
//        } catch (Exception e) {
//            e.printStackTrace();
//            group.shutdownGracefully();
//        }
    }

    public static Response send(ClientRequest request) {
        channelFuture = ChannelManager.get(ChannelManager.position);
        channelFuture.channel().writeAndFlush(JSONObject.toJSONString(request));
        channelFuture.channel().writeAndFlush("\r\n");
        DefaultFuture future = new DefaultFuture(request);
        return future.get();
    }
}

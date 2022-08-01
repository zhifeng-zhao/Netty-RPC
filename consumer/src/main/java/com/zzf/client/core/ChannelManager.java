package com.zzf.client.core;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zzf
 * @date 2022/8/1 6:32 下午
 */
public class ChannelManager {

    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<ChannelFuture>();
    public static CopyOnWriteArrayList<String> realServerPath = new CopyOnWriteArrayList<String>();
    public static AtomicInteger position = new AtomicInteger(0);

    public static void add(ChannelFuture channelFuture) {
        channelFutures.add(channelFuture);
    }

    public static void remove(ChannelFuture channelFuture) {
        channelFutures.remove(channelFuture);
    }

    public static void clear() {
        channelFutures.clear();
    }

    public static ChannelFuture get(AtomicInteger i) {
        // 采用轮循机制
        ChannelFuture channelFuture = null;
        int size = channelFutures.size();
        if (i.get() >= size) {
             channelFuture = channelFutures.get(0);
            ChannelManager.position = new AtomicInteger(1);
        } else {
            channelFuture = channelFutures.get(i.getAndIncrement());
        }
        return channelFuture;
    }
}

package com.zzf.client.watch;

import com.zzf.client.core.ChannelManager;
import com.zzf.client.core.TcpClient;
import com.zzf.client.factory.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

/**
 * @author zzf
 * @date 2022/8/1 5:52 下午
 */
public class ServerWatch implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
        String path = watchedEvent.getPath();
        client.getChildren().usingWatcher(this).forPath(path);
        List<String> paths = client.getChildren().forPath(path);
        ChannelManager.realServerPath.clear();
        paths.forEach(value -> {
            String[] str = path.split("#");
            for (int i = 0; i < Integer.parseInt(str[2]); i++) {
                ChannelManager.realServerPath.add(str[0] + ":" + str[1]);
            }
        });
        // 重新建立连接
        ChannelManager.clear();
        ChannelManager.realServerPath.forEach(realPath -> {
            String[] str = realPath.split(":");
            ChannelManager.add(TcpClient.bootstrap.connect(str[0], Integer.parseInt(str[1])));
        });
    }
}

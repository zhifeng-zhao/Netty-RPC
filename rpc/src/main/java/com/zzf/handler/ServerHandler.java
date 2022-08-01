package com.zzf.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzf.medium.Media;
import com.zzf.model.Response;
import com.zzf.model.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zzf
 * @date 2022/7/29 11:02 下午
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
        Media media = Media.newInstance();
        Response result = media.process(request);
        ctx.channel().writeAndFlush(JSONObject.toJSONString(result));
        ctx.channel().writeAndFlush("\r\n");
    }
}

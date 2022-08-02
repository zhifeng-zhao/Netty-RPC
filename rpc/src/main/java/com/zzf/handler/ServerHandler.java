package com.zzf.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzf.medium.Media;
import com.zzf.model.Response;
import com.zzf.model.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleState = (IdleStateEvent) evt;
            if (idleState.state().equals(IdleState.READER_IDLE)) {
                System.out.println("==读空闲==");
                ctx.channel().close();
            } else if (idleState.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("==写空闲==");
            } else if (idleState.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("==读写空闲==");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }
}

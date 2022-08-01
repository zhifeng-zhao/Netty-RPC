package com.zzf.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzf.future.DefaultFuture;
import com.zzf.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author zzf
 * @date 2022/7/30 11:09 上午
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("ping".equals(msg.toString())) {
            ctx.channel().writeAndFlush("pong\r\n");
            return;
        }
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        DefaultFuture.receive(response);
//        ctx.attr(AttributeKey.valueOf("msg")).set(msg);
//        ctx.channel().close();
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

package handler;

import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import pojo.StunMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class CustomSimpleChannelInboundHandler extends SimpleChannelInboundHandler<StunMsg> {

    public static final ReadTimeoutHandler readTimeoutHandler = new ReadTimeoutHandler(3, TimeUnit.SECONDS);
    private static ConcurrentHashMap<Channel,StunMsg> msgMap= new ConcurrentHashMap<>();

    public void writeOnRetry(Channel channel,StunMsg msg){
        ChannelPipeline pipeline = channel.pipeline();
        ChannelHandler handler = pipeline.get("readTimeoutHandler");
        if(handler==null){
            pipeline.addLast("readTimeoutHandler",readTimeoutHandler);
        }
        msgMap.put(channel, msg);
        if(msg.plusTimes()<=3){
            writeOnFail(channel,msg);
        }else{
            System.out.println("超时重传3次全部失败，关闭连接");
            channel.close();
        }
    }

    public void writeOnFail(Channel channel,StunMsg msg){
        this._writeOnFail(channel,msg,1);
    }

    private void _writeOnFail(Channel channel,StunMsg msg,int times){
        channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess() && times<3) {
                    _writeOnFail(channel, msg, times + 1);
                }else{
                    if(channel.isActive()){
                        System.out.println("客户端发送消息失败,关闭连接。");
                        channel.close();
                    }
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause instanceof ReadTimeoutException){
            Channel channel = ctx.channel();
            StunMsg msg = msgMap.get(channel);
            writeOnRetry(ctx.channel(),msg);
        }else{
            cause.printStackTrace();
            ctx.channel().close();
        }

    }

    public void cancelReadTimeout(Channel channel){
        ChannelPipeline pipeline = channel.pipeline();
        //去掉超时的handler
        pipeline.remove("readTimeoutHandler");
        //删除消息
        msgMap.remove(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {
        if(stunMsg.getRes()==1) {
            cancelReadTimeout(channelHandlerContext.channel());
        }
        handle(channelHandlerContext,stunMsg);
    }

    protected abstract void handle(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception;
}

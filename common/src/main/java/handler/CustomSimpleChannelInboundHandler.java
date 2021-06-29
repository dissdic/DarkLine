package handler;

import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import pojo.StunMsg;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class CustomSimpleChannelInboundHandler extends SimpleChannelInboundHandler<StunMsg> {

    private static ConcurrentHashMap<Channel,StunMsg> msgMap= new ConcurrentHashMap<>();

    //超时机制
    public void writeOnRetry(Channel channel,StunMsg msg){
        ChannelPipeline pipeline = channel.pipeline();
        ChannelHandler handler = pipeline.get("readTimeoutHandler");
        if(handler!=null){
            pipeline.remove("readTimeoutHandler");
        }
        pipeline.addFirst("readTimeoutHandler",new CustomReadTimeoutHandler(5, TimeUnit.SECONDS));
        msgMap.put(channel, msg);
        if(msg.plusTimes()<=3){
            System.out.println("发送："+msg.toString()+" 次数："+msg.getTimes());
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
                }else if(!channelFuture.isSuccess()){
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
        ChannelHandler handler = pipeline.get("readTimeoutHandler");
        if(handler != null){
            //去掉超时的handler
            pipeline.remove("readTimeoutHandler");
        }
        //删除消息
        msgMap.remove(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {
        if(stunMsg.isRes()) {
            cancelReadTimeout(channelHandlerContext.channel());
        }else{
            Thread.sleep(10000);
            //发送返回报文
            InetSocketAddress addr = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            if(addr == null){
                addr = stunMsg.getAddr();
            }
            StunMsg msg = new StunMsg(addr).res();
            writeOnFail(channelHandlerContext.channel(),msg);

            handle(channelHandlerContext,stunMsg);
        }
    }

    public void retry(Channel channel,StunMsg msg){

    }

    protected abstract void handle(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception;
}

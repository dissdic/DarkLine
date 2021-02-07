package code.stun.handler;

import dto.VarEnums;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.StunMsg;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class StunUdpResponseHandler extends CustomSimpleChannelInboundHandler {

    private InetSocketAddress preNat;

    @Override
    protected void handle(ChannelHandlerContext ctx, StunMsg stunMsg) throws Exception {


        InetSocketAddress localAddr = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddr = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress responseAddr = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());

        System.out.println(localAddr.toString());
        System.out.println(remoteAddr.toString());
        System.out.println(responseAddr.toString());

        StunMsg msg = new StunMsg(localAddr.getHostString(),localAddr.getPort(),ctx.channel());

        if(localAddr.equals(responseAddr)){
            System.out.println("公网客户端");
        }else if(preNat!=null){
            if(preNat.equals(responseAddr)){
                //限制和端口限制
                System.out.println("全锥型NAT");
            }else{
                System.out.println("对称型NAT");
            }
        }else{

            preNat = responseAddr;
            writeOnRetry(ctx.channel(),msg);

            ChannelFuture channelFuture = ctx.channel().connect(VarEnums.server2).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(!channelFuture.isSuccess()){
                        channelFuture.cause().printStackTrace();
                        channelFuture.channel().close();
                    }
                }
            }).syncUninterruptibly();
            System.out.println("old channel:"+ctx.channel());
            System.out.println("new channel:"+channelFuture.channel());

            channelFuture.channel().writeAndFlush(msg);

        }
    }



}

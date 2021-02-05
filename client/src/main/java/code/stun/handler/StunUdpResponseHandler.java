package code.stun.handler;

import dto.GeneralUtil;
import dto.VarEnums;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import pojo.StunMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class StunUdpResponseHandler extends SimpleChannelInboundHandler<StunMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StunMsg stunMsg) throws Exception {
        InetSocketAddress localAddr = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddr = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress responseAddr = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());
        System.out.println(localAddr.toString());
        System.out.println(remoteAddr.toString());
        if(localAddr.equals(responseAddr)){
            System.out.println("公网客户端");
        }else{
            ChannelFuture channelFuture = ctx.channel().connect(VarEnums.server2).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(!channelFuture.isSuccess()){
                        channelFuture.cause().printStackTrace();
                        channelFuture.channel().close();
                    }
                }
            }).syncUninterruptibly();

            //channelFuture.channel().writeAndFlush();
        }
    }
}

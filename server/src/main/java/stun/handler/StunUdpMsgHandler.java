package stun.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pojo.StunMsg;

import java.net.InetSocketAddress;

public class StunUdpMsgHandler extends SimpleChannelInboundHandler<StunMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {
        InetSocketAddress addr = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        InetSocketAddress address = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());
        System.out.println("消息内地址："+address.toString());
        System.out.println("请求者地址："+addr.toString());
        if(addr.equals(address)){
            //记录下
            System.out.println("公网客户端:"+addr.toString());
        }
        StunMsg msg = new StunMsg(addr.getHostString(),addr.getPort(),addr);
        channelHandlerContext.channel().writeAndFlush(msg);
    }


}

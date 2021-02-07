package code.stun.handler;

import dto.StunMsgBizType;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import pojo.StunMsg;

import java.net.InetSocketAddress;

public class StunUdpMsgHandler extends CustomSimpleChannelInboundHandler {

    private Channel channel;

    public StunUdpMsgHandler(Channel channel){
        this.channel = channel;
    }

    @Override
    protected void handle(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {

        InetSocketAddress addr = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        InetSocketAddress address = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());
        System.out.println("消息内地址："+address.toString());
        System.out.println("请求者地址："+addr.toString());

        if(stunMsg.getBiz()== StunMsgBizType.NAT_ADDRESS.type){
            if(!stunMsg.isRes()){
                if(addr.equals(address)){
                    //记录下
                    System.out.println("公网客户端:"+addr.toString());
                }
                StunMsg msg = new StunMsg(addr.getHostString(),addr.getPort(),channelHandlerContext.channel(),addr);
                writeOnRetry(channelHandlerContext.channel(),msg);
            }else{
                //换个channel发送
                StunMsg message = new StunMsg(channel,addr).biz(StunMsgBizType.PORT_LIMIT.type);
                writeOnRetry(channel,message);
            }
        }

    }
}

package code.stun.handler;

import code.stun.StunServer;
import dto.StunMsgBizType;
import dto.VarEnums;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import pojo.StunMsg;

import java.net.InetSocketAddress;

public class StunUdpMsgHandler extends CustomSimpleChannelInboundHandler {

    @Override
    protected void handle(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {

        InetSocketAddress remote = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        InetSocketAddress local = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());
        System.out.println("消息内地址："+local.toString());
        System.out.println("请求者地址："+remote.toString());

        if(stunMsg.getBiz()== StunMsgBizType.NAT_ADDRESS.type){
            if(!stunMsg.isRes()){
                if(remote.equals(local)){
                    //记录下
                    System.out.println("公网客户端:"+remote.toString());
                }
                StunMsg msg = new StunMsg(remote,channelHandlerContext.channel()).biz(StunMsgBizType.NAT_ADDRESS.type);
                writeOnRetry(channelHandlerContext.channel(),msg);
            }else{
                //换个channel发送
                Channel channel = StunServer.getInstance().getChannel(VarEnums.server2.getPort());
                StunMsg message = new StunMsg(channel,remote).biz(StunMsgBizType.PORT_LIMIT.type);
                writeOnRetry(channel,message);
            }
        }else if(stunMsg.getBiz() == StunMsgBizType.CHANGE_PORT.type){
            if(remote.equals(local)){
                System.out.println("端口限制型NAT:"+remote.toString());
            }else{
                System.out.println("对称型NAT:"+remote.toString());
            }
        }

    }
}

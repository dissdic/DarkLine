package code.stun.handler;

import code.stun.StunServer;
import dto.StunMsgBizType;
import dto.VarEnums;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import pojo.StunMsg;

import java.net.InetSocketAddress;
import java.util.Optional;

public class StunUdpMsgHandler extends CustomSimpleChannelInboundHandler {

    private static Channel channel_2 = null;
    @Override
    protected void handle(ChannelHandlerContext channelHandlerContext, StunMsg stunMsg) throws Exception {
        if(channel_2==null){
            channel_2 = StunServer.getInstance().getChannel(VarEnums.server2.getPort());
        }
        InetSocketAddress remote = Optional.ofNullable((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).orElse(stunMsg.getAddr());
        InetSocketAddress local = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());
        System.out.println("消息地址："+local.toString());
        System.out.println("远程地址："+remote.toString());

        if(stunMsg.getBiz()== StunMsgBizType.NAT_ADDRESS.type){
            boolean over = false;
            if(remote.equals(local)){
                //记录下
                System.out.println("公网客户端:"+remote.toString());
                over = true;
            }
            StunMsg msg = new StunMsg(remote).biz(StunMsgBizType.NAT_ADDRESS.type);
            writeOnRetry(channelHandlerContext.channel(),msg);
            if(!over) {
                StunMsg message = new StunMsg(remote).biz(StunMsgBizType.SERVER_CHANGE_PORT.type);
                writeOnRetry(channel_2, message);
            }
        }else if(stunMsg.getBiz() == StunMsgBizType.CLIENT_CHANGE_PORT.type){
            if(remote.equals(local)){
                System.out.println("端口限制型NAT:"+remote.toString());
            }else{
                System.out.println("对称型NAT:"+remote.toString());
            }
            StunMsg message = new StunMsg(remote).biz(StunMsgBizType.CONFIRM.type);
            writeOnRetry(channel_2,message);
        }else if(stunMsg.getBiz() == StunMsgBizType.CLIENT_CHANGE_HOST.type){
            StunMsg msg = new StunMsg(remote.getHostString(),remote.getPort(),VarEnums.server3).biz(StunMsgBizType.SERVER_CHANGE_HOST.type);
            writeOnRetry(channelHandlerContext.channel(),msg);
        }else if(stunMsg.getBiz() == StunMsgBizType.SERVER_CHANGE_HOST.type){
            StunMsg msg = new StunMsg(local).biz(StunMsgBizType.SERVER_CHANGE_HOST.type);
            writeOnRetry(channelHandlerContext.channel(),msg);
        }
    }
}

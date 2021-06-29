package code;

import code.stun.StunClient;
import code.stun.handler.StunUdpResponseHandler;
import dto.GeneralUtil;
import dto.StunMsgBizType;
import dto.VarEnums;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import pojo.StunMsg;

import java.net.InetSocketAddress;

public class ClientMainApplication {

    public static void main(String[] args){
        StunClient client = StunClient.getInstance();
        Channel channel = client.getChannel();
        String ip = GeneralUtil.getIp();
        if(ip!=null){
            System.out.println("本地IP:"+ip);
            InetSocketAddress local = (InetSocketAddress) channel.localAddress();
            StunMsg msg = new StunMsg(ip,local.getPort(),VarEnums.server1).biz(StunMsgBizType.NAT_ADDRESS.type);
//            for (int i = 0; i < 100; i++) {
//                channel.writeAndFlush(msg);
//            }
            channel.writeAndFlush(msg);
        }else{
            System.out.println("未获取到本地IP");
        }
    }

    public static void main_(String[] args) {

        ToolClient client= ToolClient.getInstance();
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1",1234);
        Channel channel = client.createClientChannel(addr,6666);
        channel.writeAndFlush("this's not a demo");
        client.stop();
    }
}

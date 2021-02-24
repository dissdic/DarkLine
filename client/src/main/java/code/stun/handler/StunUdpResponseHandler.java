package code.stun.handler;

import dto.StunMsgBizType;
import dto.VarEnums;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.Channel;
import pojo.StunMsg;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StunUdpResponseHandler extends CustomSimpleChannelInboundHandler {

    private InetSocketAddress ipAndPort;

    private ScheduledFuture<?> future1 = null;

    private ScheduledFuture<?> future2 = null;

    @Override
    protected void handle(ChannelHandlerContext ctx, StunMsg stunMsg) throws Exception {
        Channel channel = ctx.channel();

        InetSocketAddress localAddr = (InetSocketAddress) channel.localAddress();
        InetSocketAddress remoteAddr = (InetSocketAddress) channel.remoteAddress();
        InetSocketAddress responseAddr = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());

        System.out.println("本地地址:"+localAddr.toString());
        System.out.println("远程地址:"+remoteAddr.toString());
        System.out.println("消息地址:"+responseAddr.toString());

        int bizType = stunMsg.getBiz();
        if(bizType==StunMsgBizType.NAT_ADDRESS.type){
            if(localAddr.equals(responseAddr)){
                System.out.println("公网客户端");
            }else{
                ipAndPort = responseAddr;
                //开启定时器
                future1 = channel.eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //10秒后这块代码被执行，代表服务端换了端口不行
                        StunMsg msg = new StunMsg(ipAndPort.getHostString(),ipAndPort.getPort(),VarEnums.server2).biz(StunMsgBizType.CLIENT_CHANGE_PORT.type);
                        writeOnRetry(channel,msg);
                    }
                },10, TimeUnit.SECONDS);

            }
        }else if(bizType==StunMsgBizType.SERVER_CHANGE_PORT.type){
            if(future1!=null){
                //立即取消定时任务
                future1.cancel(false);
            }
            StunMsg msg = new StunMsg(VarEnums.server1).biz(StunMsgBizType.CLIENT_CHANGE_HOST.type);
            writeOnRetry(channel,msg);
            //开启定时任务
            future2 = channel.eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("主机限制型NAT");
                }
            },10,TimeUnit.SECONDS);

        }else if(bizType==StunMsgBizType.CONFIRM.type){
            if(ipAndPort.equals(responseAddr)){
                System.out.println("端口限制型NAT");
            }else{
                System.out.println("对称型NAT");
            }
        }else if(bizType==StunMsgBizType.SERVER_CHANGE_HOST.type){
            if(future2!=null){
                //立即取消定时任务
                future2.cancel(false);
            }
            System.out.println("全锥型NAT");
        }

    }



}

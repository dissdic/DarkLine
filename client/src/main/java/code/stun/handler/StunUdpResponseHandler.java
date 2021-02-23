package code.stun.handler;

import dto.StunMsgBizType;
import dto.VarEnums;
import handler.CustomSimpleChannelInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.StunMsg;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StunUdpResponseHandler extends CustomSimpleChannelInboundHandler {

    private InetSocketAddress ipAndPort;

    private Boolean canChangePort;
    private Boolean canChangeHost;

    private ScheduledFuture<?> future = null;

    @Override
    protected void handle(ChannelHandlerContext ctx, StunMsg stunMsg) throws Exception {
        Channel channel = ctx.channel();

        InetSocketAddress localAddr = (InetSocketAddress) channel.localAddress();
        InetSocketAddress remoteAddr = (InetSocketAddress) channel.remoteAddress();
        InetSocketAddress responseAddr = new InetSocketAddress(stunMsg.getHost(),stunMsg.getPort());

        System.out.println(localAddr.toString());
        System.out.println(remoteAddr.toString());
        System.out.println(responseAddr.toString());

        int bizType = stunMsg.getBiz();
        if(bizType==StunMsgBizType.NAT_ADDRESS.type){
            if(localAddr.equals(responseAddr)){
                System.out.println("公网客户端");
            }else{
                ipAndPort = responseAddr;
                //开启定时器
                future = channel.eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //30秒后这块代码被执行，代表服务端换了端口不行
                        canChangePort = false;
                        StunMsg msg = new StunMsg(channel,VarEnums.server2).biz(StunMsgBizType.CHANGE_PORT.type);
                        writeOnRetry(channel,msg);
                    }
                },30, TimeUnit.SECONDS);

            }
        }else if(bizType==StunMsgBizType.PORT_LIMIT.type){
            if(future!=null){
                //立即取消定时任务
                future.cancel(false);
            }
            canChangePort = true;

            System.out.println("");
        }

    }



}

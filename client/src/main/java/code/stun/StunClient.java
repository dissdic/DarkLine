package code.stun;

import dto.VarEnums;
import handler.StunUdpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public class StunClient {
    //为了防止NAT端口老化，需要加个定时监听器?
    private StunClient(){
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new StunUdpMsgEncoder());
    }
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;

    private static final StunClient instance = new StunClient();
    public static StunClient getInstance(){
        return instance;
    }

    public Channel getChannel(InetSocketAddress addr,int port){
        ChannelFuture channelFuture = bootstrap.attr(VarEnums.remoteAddr,addr).bind(port).syncUninterruptibly();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.cause().printStackTrace();
                channelFuture.channel().close();
            }
        });
        return channelFuture.channel();
    }

    public void stop(){
        group.shutdownGracefully().syncUninterruptibly();
    }

}

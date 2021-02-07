package code.stun;

import handler.StunUdpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import handler.StunUdpMsgDecoder;
import code.stun.handler.StunUdpMsgHandler;

import java.net.InetSocketAddress;

public class StunServer {

    private StunServer(){
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StunUdpMsgDecoder())
                                .addLast(new StunUdpMsgHandler())
                                .addLast(new StunUdpMsgEncoder());
                    }
                });
    }
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;


    public Channel getChannel(InetSocketAddress addr,int port){
        ChannelFuture channelFuture = bootstrap.localAddress(addr).bind().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    channelFuture.cause().printStackTrace();
                    channelFuture.channel().close();
                }
            }
        }).syncUninterruptibly();
        return channelFuture.channel();
    }

    public void stop(){
        group.shutdownGracefully().syncUninterruptibly();
    }
}

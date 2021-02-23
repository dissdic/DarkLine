package code.stun;

import handler.StunUdpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import handler.StunUdpMsgDecoder;
import code.stun.handler.StunUdpMsgHandler;
/**
 * 监听者
*/
public class StunServer {

    private StunServer(){
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(EpollDatagramChannel.class)
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

    private static StunServer instance = new StunServer();

    public static StunServer getInstance(){
        return instance;
    }

    private final Bootstrap bootstrap;
    private final EventLoopGroup group;


    public Channel getChannel(int port){
        ChannelFuture channelFuture = bootstrap.bind(port).addListener(new ChannelFutureListener() {
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

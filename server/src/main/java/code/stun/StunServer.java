package code.stun;

import dto.GeneralUtil;
import handler.StunUdpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import handler.StunUdpMsgDecoder;
import code.stun.handler.StunUdpMsgHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.udt.nio.NioUdtProvider;

/**
 * 监听者
*/
public class StunServer {

    private StunServer(){
        boolean win = GeneralUtil.windows();
        if(win){
            System.out.println("windows服务端");
            this.group = new NioEventLoopGroup();
        }else{
            System.out.println("linux服务端");
            this.group = new EpollEventLoopGroup();
        }
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(win?NioDatagramChannel.class:EpollDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,false)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StunUdpMsgDecoder())
                                .addLast(new StunUdpMsgEncoder())
                                .addLast(new StunUdpMsgHandler());
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
        System.out.println("绑定端口："+port);
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

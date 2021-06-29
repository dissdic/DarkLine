package code.stun;

import code.stun.handler.StunUdpResponseHandler;
import dto.GeneralUtil;
import handler.StunUdpMsgDecoder;
import handler.StunUdpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 广播者
*/
public class StunClient {
    //为了防止NAT端口老化，需要加个定时监听器?
    private StunClient(){
        boolean win = GeneralUtil.windows();
        if(win){
            System.out.println("windows客户端");
            this.group = new NioEventLoopGroup();
        }else{
            System.out.println("linux客户端");
            this.group = new EpollEventLoopGroup();
        }
        this.bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(win?NioDatagramChannel.class:EpollDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,false)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StunUdpMsgDecoder())
                                .addLast(new StunUdpMsgEncoder())
                                .addLast(new StunUdpResponseHandler());
                    }
                });
    }
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;

    private static final StunClient instance = new StunClient();
    public static StunClient getInstance(){
        return instance;
    }

    public Channel getChannel(){
        //port 为 0 则随机端口
        ChannelFuture channelFuture = bootstrap.bind(0).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    System.out.println("初始化客户端失败");
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

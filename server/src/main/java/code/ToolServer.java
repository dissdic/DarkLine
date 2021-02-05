package code;

import decoder.MsgLogDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ToolServer {

    private ToolServer(){}

    private final static ToolServer instance = new ToolServer();

    public static ToolServer getInstance(){
        return instance;
    }

    private final EventLoopGroup eventGroup = new NioEventLoopGroup();

    public void createHttpServerChannel(int port){
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new MyChannelInitializer(false,true,null));
            ChannelFuture future = serverBootstrap.bind().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("Channel【"+port+"】成功");
                    }else{
                        channelFuture.cause().printStackTrace();
                        channelFuture.channel().close();
                    }
                }
            }).sync();
            future.channel().closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
             eventGroup.shutdownGracefully();
        }
    }

}

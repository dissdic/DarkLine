package chapter1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class FirstClient {

    public void start() throws Exception{
        EventLoopGroup eventGroup = new NioEventLoopGroup();
        final ChannelHandler clientCommonHandler = new FirstClientHandler();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventGroup)
                    .channel(NioSocketChannel.class)
                    .localAddress(1212)
                    .remoteAddress(new InetSocketAddress("127.0.0.1",1111))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            System.out.println("client:init a client channel");
                            channel.pipeline().addLast(clientCommonHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        }finally{
            eventGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{

        new FirstClient().start();
    }

}

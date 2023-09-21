package chapter1;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) throws Exception{

        Client client1 = new Client();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i = 1234;i<1300;i++){
            int port = i;
            executorService.submit(()->{
                try {
                    client1.init(port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public static class Server{

        public static void main(String[] args) throws Exception {
            EventLoopGroup group = new NioEventLoopGroup(1);
            new ServerBootstrap()
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel channel) throws Exception {
                            System.out.println(channel.getClass()+" "+channel);
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                                    long mil = System.currentTimeMillis()+3000;
                                    System.out.println("开始执行："+((ByteBuf)o).toString(CharsetUtil.UTF_8));
                                    while(System.currentTimeMillis()<mil){
//                                        System.out.println(o.getClass()+" "+((ByteBuf)o).toString(CharsetUtil.UTF_8));
                                    }
                                    System.out.println("结束执行："+((ByteBuf)o).toString(CharsetUtil.UTF_8));
                                }
                            });
                        }
                    })
                    .bind(8888).channel().closeFuture().sync();
            group.shutdownGracefully().sync();
        }
    }
    public static class Client{

        public void init(int port) throws InterruptedException {
            EventLoopGroup group = new NioEventLoopGroup();
            Channel channel = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .localAddress(port)
                    .remoteAddress(new InetSocketAddress("127.0.0.1",8888))
                    .handler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                                    System.out.println(o.toString());
                                }
                            });
                        }
                    }).connect().sync().channel();

            channel.writeAndFlush(Unpooled.copiedBuffer(port+"xxx", CharsetUtil.UTF_8));
            channel.writeAndFlush(Unpooled.copiedBuffer(port+"aaa", CharsetUtil.UTF_8));

            channel.close();
            System.out.println("nnnn");
            group.shutdownGracefully().sync();
        }
    }
}

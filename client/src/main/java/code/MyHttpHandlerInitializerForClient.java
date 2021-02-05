package code;

import code.handler.*;
import handler.HttpCommonHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;

public class MyHttpHandlerInitializerForClient extends HttpCommonHandler {

    final MyOutboundHandler outboundHandler = new MyOutboundHandler();

    public MyHttpHandlerInitializerForClient(boolean isClient, boolean isServer, SslContext sslContext) {
        super(isClient, isServer, sslContext);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("codec","outRecord",outboundHandler);
        pipeline.addLast(new MyInboundHandler());
        System.out.println(pipeline.names());
    }
}

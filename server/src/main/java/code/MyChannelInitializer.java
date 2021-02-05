package code;

import code.handler.MyInboundHandler;
import code.handler.RequestHandler;
import handler.HttpCommonHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;

public class MyChannelInitializer extends HttpCommonHandler {

    RequestHandler handler = new RequestHandler();

    public MyChannelInitializer(boolean isClient, boolean isServer, SslContext sslContext) {
        super(isClient, isServer, sslContext);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("codec","inbound",new MyInboundHandler());
        pipeline.addLast("requestHandler",handler);
        System.out.println(pipeline.names());
    }
}

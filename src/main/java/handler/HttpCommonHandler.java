package handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

@ChannelHandler.Sharable
public class HttpCommonHandler extends ConnectCommonHandler {

    boolean isClient = false;
    boolean isServer = false;
    SslContext context;

    public HttpCommonHandler(boolean isClient, boolean isServer, SslContext sslContext){
        this.isClient = isClient;
        this.isServer = isServer;
        this.context = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        ChannelPipeline pipeline = channel.pipeline();
        if(context!=null){
            SSLEngine engine = context.newEngine(channel.alloc());
            //支持Https
            pipeline.addFirst("ssl",new SslHandler(engine));
        }
        if(isServer){
            pipeline.addLast("codec",new HttpServerCodec());
            //pipeline.addLast("compressor",new HttpContentCompressor());
        }
        if(isClient){
            pipeline.addLast("codec",new HttpClientCodec());
            //pipeline.addLast("decompressor",new HttpContentDecompressor());
        }
        //聚合
        pipeline.addLast("aggregator",new HttpObjectAggregator(512 * 1024));
    }
}

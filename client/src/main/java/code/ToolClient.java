package code;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class ToolClient {


    private final EventLoopGroup group = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();
    private final static ToolClient instance= new ToolClient();

    public static ToolClient getInstance(){
        return instance;
    }

    private ToolClient(){
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new MyHttpHandlerInitializerForClient(true,false,null));
    }

    public Channel createClientChannel(InetSocketAddress remote,int localPort) {
        bootstrap.localAddress(localPort).remoteAddress(remote);
        ChannelFuture future = bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    channelFuture.cause().printStackTrace();
                    channelFuture.channel().close().syncUninterruptibly();
                }
            }
        }).syncUninterruptibly();
        return future.channel();
    }

    public void stop(){
        group.shutdownGracefully().syncUninterruptibly();
    }

    public HttpRequest toRequest(String msg,InetSocketAddress addr){
        return toRequest(msg,addr,HttpHeaderValues.APPLICATION_JSON.toString());
    }
    public HttpRequest toRequest(String msg,InetSocketAddress addr,String contentType){
        ByteBuf data = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add(HttpHeaderNames.CONTENT_TYPE, contentType)
                .add(HttpHeaderNames.HOST,addr.getHostString())
                .add(HttpHeaderNames.CONTENT_LENGTH,data.readableBytes())
                .add(HttpHeaderNames.ACCEPT,"*/*")
                .add(HttpHeaderNames.ACCEPT_ENCODING,HttpHeaderValues.GZIP_DEFLATE)
                .add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,"http://"+addr.getHostString()+":"+addr.getPort(),data);
        request.headers().add(headers);
        return request;
    }
}

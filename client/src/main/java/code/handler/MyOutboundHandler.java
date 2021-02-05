package code.handler;

import dto.VarEnums;
import exception.CustomException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
@ChannelHandler.Sharable
public class MyOutboundHandler extends MessageToMessageEncoder<Object> {

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("本地："+localAddress.toString()+" 远程："+remoteAddress.toString());
        super.connect(ctx,remoteAddress,localAddress,promise);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> list) throws Exception {
        SocketAddress addr = ctx.channel().remoteAddress();
        InetSocketAddress res = (InetSocketAddress)addr;
        ByteBuf data = null;
        if(msg instanceof ByteBuf){
            data = (ByteBuf) msg;
        }else if(msg instanceof String){
            data = Unpooled.copiedBuffer(String.valueOf(msg),CharsetUtil.UTF_8);
        }else{
            throw new CustomException("消息不是字符串或缓冲区格式");
        }
        String contentType = ctx.channel().attr(VarEnums.CONTENT_TYPE).get();
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add(HttpHeaderNames.CONTENT_TYPE,contentType!=null?contentType: HttpHeaderValues.APPLICATION_JSON)
                .add(HttpHeaderNames.HOST,res.getHostString())
                .add(HttpHeaderNames.CONTENT_LENGTH,data.readableBytes())
                .add(HttpHeaderNames.ACCEPT,"*/*")
                .add(HttpHeaderNames.ACCEPT_ENCODING,HttpHeaderValues.GZIP_DEFLATE)
                .add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,"http://"+res.getHostString()+":"+res.getPort(),data);
        request.headers().add(headers);
        list.add(request);
    }
}

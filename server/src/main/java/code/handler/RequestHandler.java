package code.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class RequestHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest o) throws Exception {
        if(o instanceof ByteBuf){
            ByteBuf data = (ByteBuf)o;
            System.out.println(data.toString(CharsetUtil.UTF_8));
            if(data.refCnt()==0)
                data.release();
        }else{
            System.out.println(o.toString());
        }
        //channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("连接【"+Thread.currentThread().getName()+"】入站异常："+cause.getMessage());
        ctx.close();
    }
}

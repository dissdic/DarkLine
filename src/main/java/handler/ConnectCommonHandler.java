package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ConnectCommonHandler extends ChannelInitializer<Channel> {

    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //空闲
        pipeline.addLast("idle",new IdleStateHandler(0,0,60, TimeUnit.SECONDS));
        pipeline.addLast("handleIdle",new HeartbeatHandler());
    }

    public static class HeartbeatHandler extends ChannelInboundHandlerAdapter {

        private ByteBuf beat = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Are you OK", CharsetUtil.UTF_8));
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent){
                //发送请求，验证远程服务
                ctx.writeAndFlush(beat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);//异步
            }else{
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}

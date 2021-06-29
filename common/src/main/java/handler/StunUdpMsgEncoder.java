package handler;

import dto.GeneralUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import pojo.StunMsg;
import java.util.List;

public class StunUdpMsgEncoder extends MessageToMessageEncoder<StunMsg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, StunMsg msg, List<Object> list) throws Exception {
        System.out.println("编码："+msg);
        GeneralUtil.nullThrow(msg);
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
        byteBuf.writeInt(msg.getLength());
        if(!GeneralUtil.isBlank(msg.getHost())){
            CharSequence sequence = msg.getHost();
            byteBuf.writeCharSequence(sequence, CharsetUtil.UTF_8);
        }
        byteBuf.writeInt(msg.getPort());
        byteBuf.writeInt(msg.getRes());
        byteBuf.writeInt(msg.getBiz());
        DatagramPacket packet = new DatagramPacket(byteBuf,msg.getAddr());
        list.add(packet);

    }

}

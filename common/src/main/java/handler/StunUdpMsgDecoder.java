package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import pojo.StunMsg;
import java.util.List;

public class StunUdpMsgDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf content = datagramPacket.content();
        System.out.println("解码："+content.toString(CharsetUtil.UTF_8));
        String host="";
        int port;
        int res;
        int biz;
        int len = content.readInt();
        if(len != 0){
            CharSequence chs = content.readCharSequence(len, CharsetUtil.UTF_8);
            host = chs.toString();
        }
        port = content.readInt();
        res = content.readInt();
        biz = content.readInt();
        StunMsg msg = new StunMsg();
        msg.setHost(host);
        msg.setPort(port);
        msg.setRes(res);
        msg.setBiz(biz);
        msg.setAddr(datagramPacket.sender());
        list.add(msg);
    }

}

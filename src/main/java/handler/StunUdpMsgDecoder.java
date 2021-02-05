package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import pojo.StunMsg;

import java.net.InetSocketAddress;
import java.util.List;

public class StunUdpMsgDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf content = datagramPacket.content();
        StringBuilder host = new StringBuilder();
        int port;
        while (content.readableBytes()>0){
            char ch = content.readChar();
            if(ch == StunMsg.seq()){
                port = content.readInt();
                StunMsg msg = new StunMsg(host.toString(),port);
                list.add(msg);
                break;
            }else{
                host.append(ch);
            }
        }
    }
}

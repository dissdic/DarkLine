package code;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class MainApplicationForTest {

    public static void main(String[] args) {

        ToolClient client= ToolClient.getInstance();
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1",1234);
        Channel channel = client.createClientChannel(addr,6666);
        channel.writeAndFlush("this's not a demo");
        client.stop();
    }
}

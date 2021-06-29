package code;

import code.stun.StunServer;
import dto.VarEnums;
import io.netty.channel.Channel;

public class ServerMainApplication {

    public static void main(String[] args) throws Exception{
        StunServer server = StunServer.getInstance();
        try {
            Channel channel = server.getChannel(VarEnums.server1.getPort());
            //这个很蠢，后面要改
            //channel.closeFuture().sync();
            //System.out.println("关闭channel："+channel.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //这个很蠢，后面要改
            //System.out.println("关闭EventLoopGroup");
            //server.stop();
        }
    }
}

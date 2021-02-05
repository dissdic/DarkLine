package dto;

import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class VarEnums {
    public final static AttributeKey<String> CONTENT_TYPE = AttributeKey.newInstance("content_type");
    public final static AttributeKey<InetSocketAddress> remoteAddr = AttributeKey.newInstance("remoteAddr");

    public final static InetSocketAddress server1 = new InetSocketAddress("",0);
    public final static InetSocketAddress server2 = new InetSocketAddress("",0);
}

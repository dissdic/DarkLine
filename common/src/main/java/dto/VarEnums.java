package dto;

import io.netty.util.AttributeKey;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class VarEnums {
    public final static AttributeKey<String> CONTENT_TYPE = AttributeKey.newInstance("content_type");

    public final static InetSocketAddress server1;
    public final static InetSocketAddress server2;
    public final static InetSocketAddress server3;

    static {
        String server = new String(System.getProperty("server").getBytes(), StandardCharsets.UTF_8).intern();
        String server_ = new String(System.getProperty("server_").getBytes(), StandardCharsets.UTF_8).intern();
        String port = new String(System.getProperty("port").getBytes(), StandardCharsets.UTF_8).intern();
        String port_ = new String(System.getProperty("port_").getBytes(), StandardCharsets.UTF_8).intern();

        server1 = new InetSocketAddress(server,Integer.parseInt(port));
        server2 = new InetSocketAddress(server,Integer.parseInt(port_));
        server3 = new InetSocketAddress(server_,Integer.parseInt(port));
    }


}

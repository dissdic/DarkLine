package pojo;

import java.net.InetSocketAddress;

public class StunMsg {

    public StunMsg(String host,int port) {
        this.host=host;
        this.port=port;
    }

    public StunMsg(String host,int port,InetSocketAddress addr){
        new StunMsg(host,port);
        this.addr = addr;
    }

    private int port;
    private String host;
    private InetSocketAddress addr;
    private final static char seq=':';

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static char seq() {
        return seq;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    public void setAddr(InetSocketAddress addr) {
        this.addr = addr;
    }
}

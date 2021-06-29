package pojo;

import dto.GeneralUtil;

import java.net.InetSocketAddress;
import java.util.Date;

public class StunMsg {

    public StunMsg(InetSocketAddress addr){
        this(addr.getHostString(),addr.getPort(),addr);
    }

    public StunMsg(){}

    private StunMsg(String host,int port) {
        GeneralUtil.nullThrow(host);
        this.host=host;
        this.port=port;
        this.times=0;
        this.res = 0;
        this.length = host.length();
    }

    public StunMsg(String host,int port,InetSocketAddress addr){
        this(host,port);
        this.addr = addr;
    }

    public StunMsg res(){
        this.res = 1;
        return this;
    }

    private int port;
    private String host;
    private InetSocketAddress addr;
    private int times;
    //是否是返回报文
    private int res;
    private int biz;
    private int length;

    public StunMsg biz(int biz){
        this.biz = biz;
        return this;
    }



    public int plusTimes(){
        return this.times+=1;
    }

    public boolean isRes(){
        return this.res==1;
    }

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

    public InetSocketAddress getAddr() {
        return addr;
    }

    public void setAddr(InetSocketAddress addr) {
        this.addr = addr;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "StunMsg{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", addr=" + addr +
                ", res=" + res +
                ", biz=" + biz +
                '}';
    }
}

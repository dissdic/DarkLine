package network.frame;

public class ACKFrame {
    public byte type = 0x02;
    //ACK Ranges identify acknowledged packets
    public long[] ranges;

}

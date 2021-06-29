package network.common;

public class NumberUtil {

    public static byte[] longToBytes(long l) {
        byte[] b=new byte[8];
        b[0]=(byte)(l>>56 & 0xff);
        b[1]=(byte)(l>>48 & 0xff);
        b[2]=(byte)(l>>40 & 0xff);
        b[3]=(byte)(l>>32 & 0xff);
        b[4]=(byte)(l>>24 & 0xff);
        b[5]=(byte)(l>>16 & 0xff);
        b[6]=(byte)(l>>8  & 0xff);
        b[7]=(byte)(l     & 0xff);
        return b;
    }

}

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

    public static long byteToLong(byte[] bytes){
        long val = 0;
        for(int i=0;i<bytes.length;i++){
            byte bt = bytes[i];
            long tem = ((long)(bt & 0xff)) << ((bytes.length-(i+1)) * 8);
            val += tem;
        }
        return val;
    }

    /*
        判断一个数是不是素数
     */
    public static boolean isPrime(int num){
        if(num<2){
            return false;
        }else{
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if(num%i==0){
                    return false;
                }
            }
        }
        return true;
    }

    public static double log(double value,double base){
        return Math.log(value) / Math.log(base);
    }

}

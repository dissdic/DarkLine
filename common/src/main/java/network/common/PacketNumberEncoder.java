package network.common;

import java.util.Arrays;

public class PacketNumberEncoder {

    public static byte[] encode(long packetNum,long ackNum){
        long unacknowledgedNum = 0;
        if(ackNum==0){
            unacknowledgedNum = packetNum + 1;
        }else{
            unacknowledgedNum = packetNum - ackNum;
        }

        double minimumBit = NumberUtil.log((double) unacknowledgedNum,2D)+1;
        double byteNum = Math.ceil(minimumBit/8);

        //编码
//        byte[] bytes = VariableLengthInt8Encoder.encode(packetNum);
        byte[] bytes = NumberUtil.longToBytes(packetNum);

        int truncate = Double.valueOf(byteNum).intValue();
        byte[] result = new byte[truncate];
        System.arraycopy(bytes,bytes.length - truncate,result,0,truncate);

        return result;

    }


}

package network.common;

import network.exception.Int8ByteLengthOverSIzeException;

import java.nio.ByteBuffer;

public class VariableLengthInt8Decoder {

    /**
     * bytes的长度最大是8
    */
    public static long decode(byte[] bytes){
        if(bytes==null || bytes.length>8 || bytes.length==0){
            throw new Int8ByteLengthOverSIzeException();
        }
        byte firstByte = bytes[0];
        int prefix = (firstByte & 0xff) >> 6;
        int length = 1 << prefix;
        long remain = firstByte & 0x3f;//0011 1111
        for (int i = 1; i < length; i++) {
            remain = (remain << 8) + (bytes[i] & 0xff);
        }
        return remain;
    }
}

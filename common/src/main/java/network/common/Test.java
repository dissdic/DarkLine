package network.common;

public class Test {

    public static void main(String[] args){
        byte[] b = VariableLengthInt8Encoder.encode(494878333L);
        long r = VariableLengthInt8Decoder.decode(b);
        System.out.println(r);
    }
}

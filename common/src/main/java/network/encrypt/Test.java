package network.encrypt;

import network.common.NumberUtil;
import network.common.PacketNumberDecoder;
import network.common.PacketNumberEncoder;
import network.common.VariableLengthInt8Decoder;
import network.encrypt.dh.ClientDHEncrypt;
import network.encrypt.dh.ServerDHEncrypt;

public class Test {

    public static void main(String[] args) throws Exception{

        byte[] result = PacketNumberEncoder.encode(0x1422ac5c02123432L,0x14224a9d242342e2L);

//        long dd = VariableLengthInt8Decoder.decode(result);
        long dd = NumberUtil.byteToLong(result);
        long bh = PacketNumberDecoder.decode(0x14224a9d242342e2L,dd,48);

        if(1==1)return;
        System.out.println((byte)0xff);

        System.out.println(0x7fffffff);

        ServerDHEncrypt s = new ServerDHEncrypt();
        int[] pg = s.getPG();
        System.out.println(pg[0]);
        System.out.println(pg[1]);



        EncryptPublicAndPrivateKey pair = s.keyPair();
        System.out.println(pair.getPrivateKey());
        System.out.println(pair.getPublicKey());

        ClientDHEncrypt c = new ClientDHEncrypt(pg[0],pg[1]);
        EncryptPublicAndPrivateKey pair1 = c.keyPair();
        System.out.println(pair1.getPrivateKey());
        System.out.println(pair1.getPublicKey());

        c.setKey(Integer.parseInt(pair.getPublicKey()));
        System.out.println(c.getKey());

        s.setKey(Integer.parseInt(pair1.getPublicKey()));
        System.out.println(c.getKey());
    }
}

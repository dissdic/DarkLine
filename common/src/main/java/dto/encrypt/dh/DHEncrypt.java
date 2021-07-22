package dto.encrypt.dh;

import dto.encrypt.Encrypt;
import dto.encrypt.EncryptPublicAndPrivateKey;
import dto.encrypt.aes.AESEncrypt;
import network.common.GetPrime;
import network.common.NumberUtil;

import java.util.Random;

public abstract class DHEncrypt extends AESEncrypt  {

    protected static final int max = 100;

    protected int p ;
    protected int g ;

    private int privateKey;

    public void setKey(int peerPubKey){
        System.out.println(peerPubKey+" ^ "+privateKey+" mod "+p);
        int key = (int)(Math.pow(peerPubKey,privateKey) % p);
        this.key = String.valueOf(key);
    }
    public String getKey(){
        return this.key;
    }
    @Override
    public EncryptPublicAndPrivateKey keyPair() throws Exception {
        Random random = new Random();
        int privateKey = random.nextInt(max);
        int publicKey = (int)(Math.pow(g,privateKey)% p);
        this.privateKey = privateKey;
        EncryptPublicAndPrivateKey pubAndPri = new EncryptPublicAndPrivateKey();
        pubAndPri.setPublicKey(String.valueOf(publicKey));
        pubAndPri.setPrivateKey(String.valueOf(privateKey));
        return pubAndPri;
    }

    public int[] getPG(){
        return new int[]{p,g};
    }
}

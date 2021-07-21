package dto.encrypt.dh;

import dto.encrypt.Encrypt;
import dto.encrypt.EncryptPublicAndPrivateKey;
import dto.encrypt.aes.AESEncrypt;
import network.common.GetPrime;
import network.common.NumberUtil;

import java.util.Random;

public abstract class DHEncrypt implements Encrypt {

    private static final int max = 100;

    private AESEncrypt aesEncrypt = AESEncrypt.getInstance();

    @Override
    public byte[] encrypt(byte[] data, String publicKey) throws Exception {
        return aesEncrypt.encrypt(data,publicKey);
    }

    @Override
    public byte[] decrypt(byte[] data, String privateKey) throws Exception {
        return aesEncrypt.decrypt(data,privateKey);
    }

    @Override
    public EncryptPublicAndPrivateKey keyPair() throws Exception {
        Random random = new Random();
        int p = random.nextInt(max);
        while(!NumberUtil.isPrime(p)){
            p = random.nextInt();
        }
        int g = GetPrime.getRootPrime(p);

        int privateKey = random.nextInt(max);

        int publicKey = new Double(Math.pow(g,privateKey)).intValue() % p;
        EncryptPublicAndPrivateKey pubAndPri = new EncryptPublicAndPrivateKey();
        pubAndPri.setPublicKey(String.valueOf(publicKey));
        pubAndPri.setPrivateKey(String.valueOf(privateKey));
        return pubAndPri;
    }


}

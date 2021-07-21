package dto.encrypt.aes;

import dto.encrypt.Encrypt;
import dto.encrypt.EncryptPublicAndPrivateKey;
import exception.CustomException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncrypt implements Encrypt {

    private AESEncrypt(){}
    private static final AESEncrypt instance= new AESEncrypt();
    public static AESEncrypt getInstance(){
        return instance;
    }

    @Override
    public EncryptPublicAndPrivateKey keyPair() throws Exception {

        return null;
    }

    @Override
    public byte[] encrypt(byte[] data, String publicKey) throws Exception {
        if(publicKey.length()!=16){
            throw new CustomException("AES加密KEY长度不是16位");
        }
        byte[] raw = Base64.getDecoder().decode(publicKey);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data, String privateKey) throws Exception {
        if(privateKey.length()!=16){
            throw new CustomException("AES解密KEY长度不是16位");
        }
        byte[] raw = Base64.getDecoder().decode(privateKey);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(data);
    }
}

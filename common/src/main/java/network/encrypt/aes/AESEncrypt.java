package network.encrypt.aes;

import dto.GeneralUtil;
import network.encrypt.Encrypt;
import exception.CustomException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public abstract class AESEncrypt implements Encrypt {

    protected String key;

    public byte[] encrypt(byte[] data) throws Exception {
        if(GeneralUtil.isBlank(key)){
            throw new CustomException("对称秘钥不可以为空");
        }
        return encrypt(data,key);
    }
    public byte[] decrypt(byte[] data) throws Exception {
        if(GeneralUtil.isBlank(key)){
            throw new CustomException("对称秘钥不可以为空");
        }
        return decrypt(data,key);
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

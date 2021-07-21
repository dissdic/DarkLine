package dto.encrypt.rsa;


import dto.encrypt.Encrypt;
import dto.encrypt.EncryptPublicAndPrivateKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/*
    RSA：公钥加密算法，加密耗时较长，不适合频繁使用
 */
public class RSAEncrypt implements Encrypt {
    /*
        用于生成一对RSA的秘钥（公钥，私钥）
     */
    public EncryptPublicAndPrivateKey keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //设置秘钥的长度，1024位，基本不会被破解
        keyPairGenerator.initialize(1024,new SecureRandom());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        EncryptPublicAndPrivateKey result = new EncryptPublicAndPrivateKey();
        result.setPrivateKey(privateKeyStr);
        result.setPublicKey(publicKeyStr);

        return result;
    }
    /*
        加密
     */
    public byte[] encrypt(byte[] data,String publicKey) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,pubKey);
        return cipher.doFinal(data);
    }

    /*
        解密
     */
    public byte[] decrypt(byte[] data,String privateKey) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,priKey);
        return cipher.doFinal(data);
    }

}

package network.encrypt;

public interface Encrypt {

    EncryptPublicAndPrivateKey keyPair() throws Exception;

    byte[] encrypt(byte[] data,String publicKey) throws Exception;

    byte[] decrypt(byte[] data,String privateKey) throws Exception;
}

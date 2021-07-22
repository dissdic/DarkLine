package dto.encrypt.dh;

import network.common.GetPrime;
import network.common.NumberUtil;

import java.util.Random;

public class ServerDHEncrypt extends DHEncrypt{

    public ServerDHEncrypt(){

        Random random = new Random();
        int p = random.nextInt(max);
        while(!NumberUtil.isPrime(p)){
            p = random.nextInt(max);
        }
        int g = GetPrime.getRootPrime(p);

        this.p = p;
        this.g = g;
    }
}

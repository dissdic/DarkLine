package network.common;

public class GetPrime {
    private static boolean[] primes = new boolean[100];

    public static int getRootPrime(int p){
        setPrime();
        for (int i = 2; i < p; i++)
            if (quickPow(i, p - 1, p) == 1)
                if (isPrimeRoot(i, p - 1)) {
                    return i;
                }

        return 512;
    }

    private static boolean isPrimeRoot(int g, int P) {
        for (int i = 2; i < P; i++) {
            if (!primes[i] && quickPow(P, 1, i) == 0)
                if (quickPow(g, P / i, P + 1) == 1)
                    return false;
            while (P % i == 0) P /= i;
        }
        return true;
    }

    private static void setPrime() {
        for (int i = 2; i < 100; i++)
            if (!primes[i])
                for (int j = i * 2; j < 100; j += i)
                    primes[j] = true;
    }

    private static int quickPow(int a, int b, int c) {
        int ans = 1;
        for (; b != 0; b /= 2) {
            if (b % 2 == 1)
                ans = (ans * a) % c;
            a = (a * a) % c;
        }
        return ans;
    }

}

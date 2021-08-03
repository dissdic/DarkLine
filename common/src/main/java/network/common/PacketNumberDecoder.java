package network.common;

public class PacketNumberDecoder {

    public static long decode(long largestPn,long truncatePn,int bits){

        long expectedPn = largestPn + 1;
        long pnWin = 1<<bits;
        long pnHWin = pnWin/2;
        long pnMask = pnWin-1;

        long candidatePn = (expectedPn & ~pnMask) | truncatePn;

        if(candidatePn <= (expectedPn - pnHWin) && candidatePn < (1L<<62)-pnWin){
            return candidatePn + pnWin;
        }

        if(candidatePn > (expectedPn - pnHWin) && candidatePn >= pnWin){
            return candidatePn - pnWin;
        }

        return candidatePn;
    }
}

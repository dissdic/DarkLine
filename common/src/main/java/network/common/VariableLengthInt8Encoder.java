package network.common;

public class VariableLengthInt8Encoder {

    public static byte[] encode(long number){
        long max = 1L<<62-1;
        if(number > max){
            throw new StringIndexOutOfBoundsException();
        }
        int size = 1;
        int base = 8;
        while((1L<<base-1)<=number){
            size++;
            base = size==8?62:8*size;
        }
        if(size%2!=0 && size!=1){
            size +=1;
        }

        byte[] bytes = NumberUtil.longToBytes(number);
        if(bytes.length!=size){
            byte[] newBytes = new byte[size];
            System.arraycopy(bytes,8-size,newBytes,0,size);
            byte firstByte = newBytes[0];

            int head = base(size);
            newBytes[0] = (byte)((firstByte & 0x3f)+head);

            return newBytes;
        }else{
            byte firstByte = bytes[0];
            int head = base(size);
            bytes[0] = (byte)((firstByte & 0x3f)+head);
            return bytes;
        }
    }

    private static int base(int size){
        int result = -1;
        switch (size){
            case 1:
                result = 0x00;
                break;
            case 2:
                result = 0x40;
                break;
            case 4:
                result = 0x80;
                break;
            case 8:
                result = 0xc0;
        }
        return result;
    }
}

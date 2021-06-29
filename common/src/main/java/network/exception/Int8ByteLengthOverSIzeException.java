package network.exception;

public class Int8ByteLengthOverSIzeException extends RuntimeException {

    public Int8ByteLengthOverSIzeException() {
        super("int8 bytes length ranges from 1 to 8");
    }
}

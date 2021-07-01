package network.exception;

public class StreamIdOverNumberException extends RuntimeException {

    public StreamIdOverNumberException() {
        super("A stream ID is a 62-bit integer (0 to 2^62-1)");
    }
}

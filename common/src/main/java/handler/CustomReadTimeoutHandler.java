package handler;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;
@ChannelHandler.Sharable
public class CustomReadTimeoutHandler extends ReadTimeoutHandler {
    public CustomReadTimeoutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public CustomReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }
}

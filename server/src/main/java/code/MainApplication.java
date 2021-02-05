package code;
public class MainApplication {

    public static void main(String[] args){
        ToolServer server = ToolServer.getInstance();
        server.createHttpServerChannel(1234);
    }
}

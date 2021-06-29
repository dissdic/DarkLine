package dto;

import exception.CustomException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Optional;

public class GeneralUtil {

    public static boolean isBlank(String str){
        if(str==null){
            return true;
        }else{
            str = str.trim();
            return str.isEmpty();
        }
    }

    public static void nullThrow(Object obj) {
        if(obj==null){
            throw new CustomException("空对象");
        }
    }

    public static String getIp(){
        String ip = null;
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if(networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual()){
                    continue;
                }
                Enumeration<InetAddress> addr = networkInterface.getInetAddresses();
                while(addr.hasMoreElements()){
                    InetAddress address = addr.nextElement();
                    if (!address.isLoopbackAddress() && address.isSiteLocalAddress() && !address.isAnyLocalAddress()) {
                        ip = address.getHostAddress();
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return Optional.ofNullable(ip).orElse(localAddress());
    }

    public static boolean windows(){
        String val = System.getProperty("os.name");
        if(val.toLowerCase().contains("windows")){
            return true;
        }
        return false;
    }

    private static String localAddress(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(addr).map(InetAddress::getHostAddress).orElseThrow(()->new CustomException("没网卡"));
    }
}

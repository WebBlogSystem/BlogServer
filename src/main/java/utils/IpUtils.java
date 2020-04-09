package utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtils {
    public static String getNativeIpAddress() {
        try{
            Enumeration< NetworkInterface > allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip=(InetAddress)addresses.nextElement();
                    if(ip!=null
                    && ip instanceof Inet4Address
                    && !ip.isLoopbackAddress()
                    && ip.getHostAddress().indexOf(":")==-1){
                        System.out.println("Ip:"+ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        getNativeIpAddress();
    }
}

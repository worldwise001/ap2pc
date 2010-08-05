/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.main;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author sarah
 */
public class Me {

    private String username = System.getProperty("user.name");
    private SHOW show = SHOW.AVAILABLE;
    private String status = "around";

    synchronized public String getUsername() {
        return username;
    }

    synchronized public void setUsername(String name) {
        this.username = name;
    }

    synchronized public SHOW getShow() {
        return show;
    }

    synchronized public void setShow(SHOW show) {
        this.show = show;
    }

    synchronized public String getStatus() {
        return status;
    }

    synchronized public void setStatus(String status) {
        this.status = status;
    }

    synchronized public String getIdentifier() {
        return username + "@" + getLocalHost();
    }

    public static String getLocalHost() {
        String host = "localhost";
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        return host;
    }

    public static String getLocalIP() {
        String ip = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> netif = NetworkInterface.getNetworkInterfaces();
            while (netif.hasMoreElements()) {
                NetworkInterface netifa = netif.nextElement();
                List<InterfaceAddress> ifaddr = netifa.getInterfaceAddresses();
                for (int i = 0; i < ifaddr.size(); i++) {
                    InterfaceAddress ifaddra = ifaddr.get(i);
                    InetAddress addr = ifaddra.getAddress();
//                    System.out.print(addr.getHostAddress()+": ");
//                    if (addr.isLinkLocalAddress()) System.out.print("link-local ");
//                    if (addr.isLoopbackAddress()) System.out.print("loopback ");
//                    if (addr.isMulticastAddress()) System.out.print("multicast ");
//                    if (addr.isSiteLocalAddress()) System.out.print("site-local ");
//                    if (addr.isAnyLocalAddress()) System.out.print("any-local ");
//                    System.out.println();
                    if (addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    public static boolean isMe(String ip) {
        try {
            Enumeration<NetworkInterface> netif = NetworkInterface.getNetworkInterfaces();
            while (netif.hasMoreElements()) {
                NetworkInterface netifa = netif.nextElement();
                List<InterfaceAddress> ifaddr = netifa.getInterfaceAddresses();
                for (int i = 0; i < ifaddr.size(); i++) {
                    InterfaceAddress ifaddra = ifaddr.get(i);
                    InetAddress addr = ifaddra.getAddress();
                    if (addr.getHostAddress().equals(ip)) {
                        return true;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String showToString(SHOW state) {
        switch (state) {
            case AWAY:
                return "away";
            case DND:
                return "dnd";
            case XA:
                return "xa";
            case AVAILABLE:
                return "available";
            case OFFLINE:
                return "";
            case UNKNOWN:
            default:
                return "unknown";
        }
    }

    public static SHOW stringToShow(String str) {
        SHOW state = SHOW.UNKNOWN;
        if (str == null || str.equalsIgnoreCase("available")) {
            state = SHOW.AVAILABLE;
        } else if (str.equalsIgnoreCase("away")) {
            state = SHOW.AWAY;
        } else if (str.equalsIgnoreCase("xa")) {
            state = SHOW.XA;
        } else if (str.equalsIgnoreCase("dnd")) {
            state = SHOW.DND;
        }
        return state;
    }

    public enum SHOW {

        AVAILABLE, AWAY, DND, XA, OFFLINE, UNKNOWN
    }
}

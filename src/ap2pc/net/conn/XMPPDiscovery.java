/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.conn;

import ap2pc.main.Me;
import ap2pc.net.stanza.obj.Stanza;
import ap2pc.net.stanza.obj.StanzaFormatter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * @author barker
 */
public class XMPPDiscovery extends DatagramSocket {

    private DatagramPacket sendPacket, receivePacket;

    public XMPPDiscovery() throws SocketException {
        super(Network.UDP_PORT);
        this.setSoTimeout(Network.TIMEOUT * 1000);
    }

    public Stanza receiveStanza() throws SocketTimeoutException {
        while (true) {
            try {
                // set up packet
                byte buf[] = new byte[this.getReceiveBufferSize()];
                receivePacket = new DatagramPacket(buf, buf.length);

                // wait for packet
                receive(receivePacket);
                if (Me.isMe(receivePacket.getAddress().getHostAddress()))
                {
                    continue;
                }

                // process packet
                String packetData = new String(receivePacket.getData());
                int pos = packetData.indexOf(0);
                if (pos >= 0) packetData = packetData.substring(0, pos);
                Stanza newPacket = StanzaFormatter.getInstance().XMLToStanza(packetData);
                newPacket.putOriginator(receivePacket.getAddress());
                return newPacket;
            } catch (SocketTimeoutException ex) {
                throw ex;
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    public void sendStanza(Stanza input, String address) {
        String temp = StanzaFormatter.getInstance().stanzaToXML(input);
        byte[] buf = temp.getBytes();
        InetAddress addy = null;

        try {
            addy = InetAddress.getByName(address);
        } catch (UnknownHostException he) {
            he.printStackTrace();
        }

        sendPacket = new DatagramPacket(buf, buf.length, addy, Network.UDP_PORT);

        try {
            send(sendPacket);
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    public void broadcast(Stanza s) {
        sendStanza(s, "255.255.255.255");
    }
}

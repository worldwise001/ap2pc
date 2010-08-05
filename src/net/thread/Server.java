/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.thread;

import ap2pc.main.AP2PC;
import ap2pc.main.Network;
import ap2pc.net.conn.XMPPClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author sarah
 */
public class Server extends Thread {

    private ServerSocket ss;
    private AP2PC ap2pc;

    public Server(AP2PC a) {
        ap2pc = a;
        try {
            ss = new ServerSocket(Network.TCP_PORT);
        } catch (IOException e) {
            System.err.println("Error setting up server on port 5222, quitting!");
            System.exit(1);
        }
    }

    public XMPPClient acceptClient() {
        XMPPClient c = null;
        try {
            Socket s = ss.accept();
            c = new XMPPClient(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    public void run() {
        while (ap2pc.running()) {
            XMPPClient c = acceptClient();
            try {
                c.handshake(ap2pc);
                if (!ap2pc.getNet().processNewClient(c)) {
                    c.terminate();
                }
            } catch (XMLStreamException e) {
                System.err.println("Error establishing client stream, closing connection");
            }
        }
    }
}

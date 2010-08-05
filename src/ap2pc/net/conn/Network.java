/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.conn;

import ap2pc.main.AP2PC;
import ap2pc.main.User;
import ap2pc.conversation.Conversation;
import ap2pc.net.conn.XMPPClient;
import ap2pc.net.stanza.obj.ConversationStanza;
import ap2pc.net.stanza.obj.Stanza;
import ap2pc.net.conn.Client;
import ap2pc.net.conn.DiscoveryThread;
import ap2pc.net.conn.Server;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author sharvey
 */
public class Network {

    private AP2PC ap2pc;
    private Server server;
    private DiscoveryThread discovery;
    public static final int TIMEOUT = 2;
    public static final int TCP_PORT = 5222;
    public static final int UDP_PORT = 5222;

    private boolean busy = false;

    public Network(AP2PC a) {
        ap2pc = a;
    }

    public void init() {
        discovery = new DiscoveryThread(ap2pc);
        discovery.start();
        server = new Server(ap2pc);
        server.start();
    }

    synchronized public void terminate() {
        discovery.terminate();
    }

    synchronized public void broadcast(Stanza s) {
        discovery.broadcast(s);
    }

    synchronized public void invite(Conversation c, User u) {
        discovery.invite(c, u);
    }

    synchronized public boolean processNewClient(XMPPClient c) {
        boolean ret = false;
        lock();
        if (!ap2pc.getLibrary().conversationExists(c.getIdentifier())) {
            ConversationStanza cs = generateGTFO(c.getEndpoint());
            c.sendStanza(cs);
        }

        Conversation con = ap2pc.getLibrary().getConversation(c.getIdentifier());
        if (!c.isAux() || (c.isAux() && con.needsUsers())) {
            Client cl = new Client(ap2pc, c);
            con.addClient(cl, c.isAux());
            cl.setConversation(con);
            cl.start();
            ConversationStanza cs = con.generateAccept(c.getEndpoint());
            c.sendStanza(cs);
            ret = true;
        } else {
            ConversationStanza cs = generateGTFO(c.getEndpoint());
            c.sendStanza(cs);
        }
        unlock();
        return ret;
    }

    synchronized public void join(ConversationStanza cs) {
        lock();
        String identifier = cs.getId();
        String endpoint = cs.getFrom();
        String host = cs.getOriginator().getHostAddress();
        try {
            XMPPClient cl = new XMPPClient(host);
            boolean result = cl.handshake(ap2pc, identifier, endpoint, false);
            if (result) {
                Client tcl = new Client(ap2pc, cl);
                Conversation c = ap2pc.getLibrary().create(identifier, cs.getName());
                tcl.setConversation(c);
                c.addClient(tcl, false);
                tcl.start();
                c.requestRefresh();
            } else {
                ap2pc.log("Connnection to "+endpoint+" refused");
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
        unlock();
    }

    synchronized public void rejoin(ConversationStanza cs) {
        lock();
        String identifier = cs.getTo();
        String endpoint = cs.getFrom();
        String host = cs.getIp();
        try {
            XMPPClient cl = new XMPPClient(host);
            boolean result = cl.handshake(ap2pc, identifier, endpoint, true);
            if (result) {
                Client tcl = new Client(ap2pc, cl);
                Conversation c = ap2pc.getLibrary().getConversation(identifier);
                tcl.setConversation(c);
                c.addClient(tcl, true);
                tcl.start();
                c.requestRefresh();
            } else {
                ap2pc.log("Connnection to "+endpoint+" refused");
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
        unlock();
    }

    public ConversationStanza generateGTFO(String to) {
        ConversationStanza cs = new ConversationStanza();
        cs.setTo(to);
        cs.setFrom(ap2pc.getMe().getIdentifier());
        cs.setType("gtfo");
        return cs;
    }

    synchronized public void lock()
    {
        while (busy)
        {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        busy = true;
    }

    synchronized public void unlock()
    {
        busy = false;
        notifyAll();
    }
}

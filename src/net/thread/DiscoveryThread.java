/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.thread;

import ap2pc.main.AP2PC;
import ap2pc.main.User;
import ap2pc.main.conversation.Conversation;
import ap2pc.main.Me;
import ap2pc.main.Network;
import ap2pc.net.conn.XMPPDiscovery;
import ap2pc.net.stanza.obj.ConversationStanza;
import ap2pc.net.stanza.obj.PresenceStanza;
import ap2pc.net.stanza.obj.Stanza;
import ap2pc.net.stanza.obj.StanzaFormatter;
import ap2pc.net.stanza.obj.TransportStanza;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;

/**
 *
 * @author sarah
 */
public class DiscoveryThread extends Thread {

    private XMPPDiscovery udp;
    private AP2PC ap2pc;
    private long lastBroadcast = 0;

    public DiscoveryThread(AP2PC a) {
        super("DiscoveryThread");
        ap2pc = a;
        try {
            udp = new XMPPDiscovery();
        } catch (SocketException ex) {
            System.err.println("Error setting up server on port 5222, quitting!");
            System.exit(1);
        }
    }

    public void run() {
        Stanza s = null;
        lastBroadcast = Calendar.getInstance().getTimeInMillis();
        while (ap2pc.running()) {
            try {
                s = udp.receiveStanza();
                if (s != null) {
                    handleStanza(s);
                }
                if (beenAWhile()) {
                    probe();
                }
            } catch (SocketTimeoutException ex) {
                probe();
            }
        }
    }

    public void broadcast(Stanza s) {
        udp.broadcast(s);
    }

    public void invite(Conversation c, User u) {
        ConversationStanza cs = new ConversationStanza();
        cs.setFrom(ap2pc.getMe().getIdentifier());
        cs.setId(c.getIdentifier());
        cs.setTo(u.getIdentifier());
        cs.setName(c.getName());
        cs.setType("invite");
        udp.sendStanza(cs, u.getIp());
    }

    private void handleStanza(Stanza s) {
        if (!(s instanceof TransportStanza)) {
            return;
        }
        if (s instanceof PresenceStanza) {
            PresenceStanza ps = (PresenceStanza) s;
            if (ps.getType().equalsIgnoreCase("subscribed")) {
                ap2pc.updateUser(ps);
            } else if (ps.getType().equalsIgnoreCase("probe")) {
                PresenceStanza psr = reply();
                String to = ps.getOriginator().getHostAddress();
                udp.sendStanza(psr, to);
            } else if (ps.getType().equalsIgnoreCase("unavailable")) {
                ap2pc.updateUser(ps);
            }
        } else if (s instanceof ConversationStanza) {
            ConversationStanza cs = (ConversationStanza) s;
            if (cs.getType().equalsIgnoreCase("invite")) {
                ap2pc.getLibrary().inviteReceived(cs);
            }
        }
    }

    private PresenceStanza reply() {
        PresenceStanza ps = new PresenceStanza();
        ps.setType("subscribed");
        ps.setFrom(ap2pc.getMe().getIdentifier());
        ps.setPriority(0);
        ps.setStatus(ap2pc.getMe().getStatus());
        ps.setShow(Me.showToString(ap2pc.getMe().getShow()));
        return ps;
    }

    public void terminate() {
        PresenceStanza ps = new PresenceStanza();
        ps.setType("unavailable");
        ps.setFrom(ap2pc.getMe().getIdentifier());
        udp.broadcast(ps);
    }

    private boolean beenAWhile() {
        return ((Calendar.getInstance().getTimeInMillis() - lastBroadcast) > Network.TIMEOUT * 1000);
    }

    private void probe() {
        PresenceStanza ps = new PresenceStanza();
        ps.setFrom(ap2pc.getMe().getIdentifier());
        ps.setType("probe");
        udp.broadcast(ps);
        lastBroadcast = Calendar.getInstance().getTimeInMillis();
    }
}

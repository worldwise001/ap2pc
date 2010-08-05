/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.conn;

import ap2pc.main.AP2PC;
import ap2pc.conversation.Conversation;
import ap2pc.conversation.ConversationMessage;
import ap2pc.net.conn.XMPPClient;
import ap2pc.net.stanza.obj.ConversationStanza;
import ap2pc.net.stanza.obj.MessageStanza;
import ap2pc.net.stanza.obj.Stanza;
import java.io.EOFException;
import java.util.Calendar;

/**
 *
 * @author sarah
 */
public class Client extends Thread {

    private XMPPClient tcp;
    private AP2PC ap2pc;
    private Conversation conversation;
    private String localIP = "127.0.0.1";
    private boolean run = true;

    public Client(AP2PC a, XMPPClient tcp) {
        super("TCP Client " + tcp.getEndpoint());
        this.tcp = tcp;
        ap2pc = a;
    }

    public void run() {
        ConversationStanza csip = new ConversationStanza();
        csip.setFrom(ap2pc.getMe().getIdentifier());
        csip.setTo(tcp.getEndpoint());
        csip.setType("ipex");
        csip.setIp(tcp.getRemoteIP());
        tcp.sendStanza(csip);
        while (ap2pc.running() && run) {
            Stanza s = null;
            try {
                s = tcp.receiveStanza();
            } catch (EOFException ex) {
                break;
            }
            if (s == null) {
                break;

            }
            if (s instanceof ConversationStanza) {
                ConversationStanza cs = (ConversationStanza) s;
                if (cs.getType().equalsIgnoreCase("refresh")) {
                    conversation.respondToRefresh(cs);
                } else if (cs.getType().equalsIgnoreCase("update")) {
                    conversation.refreshUpdate(cs);
                } else if (cs.getType().equalsIgnoreCase("ipex")) {
                    localIP = cs.getIp();
                } else if (cs.getType().equalsIgnoreCase("reqconn")) {
                    conversation.processReqConn(cs);
                }
            }
            if (s instanceof MessageStanza) {
                MessageStanza ms = (MessageStanza) s;
                ConversationMessage msg = new ConversationMessage();
                msg.setFrom(ms.getFrom());
                msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
                msg.setTo(ms.getTo());
                msg.setMessage(ms.getBody());
                msg.setIdentifier(ms.getId());
                conversation.converse(msg);
            }
        }
        conversation.removeClient(this);
    }

    synchronized public void sendMessage(ConversationMessage msg) {
        MessageStanza ms = new MessageStanza();
        ms.setFrom(msg.getFrom());
        ms.setTo(msg.getTo());
        ms.setId(msg.getIdentifier());
        ms.setBody(msg.getMessage());
        tcp.sendStanza(ms);
    }

    synchronized public void sendInfo(ConversationStanza cs) {
        tcp.sendStanza(cs);
    }

    synchronized public void terminate()
    {
        run = false;
        tcp.terminate();
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getEndpoint() {
        return tcp.getEndpoint();
    }

    public String getLocalIP() {
        return localIP;
    }
}

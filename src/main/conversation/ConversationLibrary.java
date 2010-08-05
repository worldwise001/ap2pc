/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.main.conversation;

import ap2pc.main.AP2PC;
import ap2pc.net.stanza.obj.ConversationStanza;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author sharvey
 */
public class ConversationLibrary {

    private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
    private AP2PC ap2pc;

    public ConversationLibrary(AP2PC a) {
        ap2pc = a;
    }

    public static String generateUniqueIdentifier(AP2PC ap2pc) {
        String s = ap2pc.getMe().getIdentifier() + ":" + Calendar.getInstance().getTimeInMillis();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes());
            BigInteger bigInt = new BigInteger(1, digest);
            String hash = bigInt.toString(16);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            return s;
        }
    }

    synchronized public Conversation create(String id, String name) {
        Conversation c = new Conversation(ap2pc, id);
        c.setName(name);
        c.setTopic("Welcome to " + name);
        conversations.put(id, c);
        ap2pc.getVisual().initConversation(c);
        return c;
    }

    synchronized public Conversation create(String name) {
        String id = generateUniqueIdentifier(ap2pc);
        while (conversations.containsKey(id)) {
            id = generateUniqueIdentifier(ap2pc);
        }
        return create(id, name);
    }

    synchronized public void inviteReceived(ConversationStanza cs) {
        boolean result = ap2pc.getVisual().requestToJoinConversation(cs.getFrom(), cs.getName());
        if (result) {
            ap2pc.getNet().join(cs);
        }
    }

    synchronized public ConversationMessage createCMsg(String identifier, String message) {
        ConversationMessage msg = new ConversationMessage();
        msg.setFrom(ap2pc.getMe().getIdentifier());
        msg.setTo(identifier);
        msg.setIdentifier(generateUniqueIdentifier(ap2pc));
        msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
        msg.setMessage(message);
        return msg;
    }

    synchronized public boolean conversationExists(String identifier) {
        return (conversations.containsKey(identifier));
    }

    synchronized public Conversation getConversation(String identifier) {
        return conversations.get(identifier);
    }

    synchronized public HashMap<String, Conversation> getConversations() {
        return conversations;
    }

    synchronized public void removeConversation(Conversation c)
    {
        conversations.remove(c.getIdentifier());
    }
}

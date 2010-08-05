/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.main.conversation;

import ap2pc.main.AP2PC;
import ap2pc.main.User;
import ap2pc.main.visual.ConversationVisual;
import ap2pc.net.stanza.obj.ConversationStanza;
import ap2pc.net.thread.Client;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author sarah
 */
public class Conversation {

    private String identifier = "";
    private String name = "";
    private String topic = "";
    private String lastRefreshId = "";
    private int contention = 0;
    private boolean broadcast = false;
    private AP2PC ap2pc = null;
    private HashMap<String, Long> log = new HashMap<String, Long>();
    private ArrayList<Client> clients = new ArrayList<Client>();
    private ArrayList<String> users = new ArrayList<String>();
    public static final int TIMEOUT_MIN = 5;
    private ConversationVisual visual;
    private Random generator = new Random();

    public Conversation(AP2PC a, String identifier) {
        ap2pc = a;
        this.identifier = identifier;
    }

    synchronized public String getIdentifier() {
        return identifier;
    }

    synchronized public void sendCMsg(String message) {
        ConversationMessage cmsg = ap2pc.getLibrary().createCMsg(identifier, message);
        converse(cmsg);
    }

    synchronized public void converse(ConversationMessage cmsg) {
        String mid = cmsg.getIdentifier();
        if (!log.containsKey(mid)) {
            log.put(mid, cmsg.getTimestamp());
            cleanup();
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).sendMessage(cmsg);
            }
            visual.displayMessage(cmsg);
        }
    }

    synchronized public void removeClient(Client c) {
        int i = clients.indexOf(c);
        if (i == -1) {
            return;
        }
        visual.displayStatus("link between " + ap2pc.getMe().getIdentifier() + " and " + c.getEndpoint() + " has been broken");
        String cid = c.getEndpoint();
        clients.remove(i);
        removeUser(cid);
        requestRefresh();
    }

    synchronized public void cleanup() {
        if (log.size() > 100) {
            Set<String> set = log.keySet();
            String[] keys = new String[set.size()];
            keys = set.toArray(keys);
            for (int i = 0; i < keys.length; i++) {
                if ((Calendar.getInstance().getTimeInMillis() - log.get(keys[i])) > TIMEOUT_MIN * 60000) {
                    log.remove(keys[i]);
                }
            }
        }
    }

    synchronized public void addClient(Client c, boolean aux) {
        clients.add(c);
        visual.displayStatus("link between " + ap2pc.getMe().getIdentifier() + " and " + c.getEndpoint() + " has been created");
        if (!userExists(c.getEndpoint()) && !aux) {
            addUser(c.getEndpoint());
        }
    }

    synchronized public void invite(User[] users) {
        for (int i = 0; i < users.length; i++) {
            ap2pc.getNet().invite(this, users[i]);
        }
    }

    synchronized public void invite(User u) {
        if (!userExists(u.getIdentifier()))
            ap2pc.getNet().invite(this, u);
    }

    synchronized public String getName() {
        return name;
    }

    synchronized public void setName(String name) {
        this.name = name;
    }

    synchronized public String getTopic() {
        return topic;
    }

    synchronized public void setTopic(String topic) {
        this.topic = topic;
    }

    public ConversationVisual getVisual() {
        return visual;
    }

    public void setVisual(ConversationVisual visual) {
        this.visual = visual;
        addUser(ap2pc.getMe().getIdentifier());
    }

    private boolean userExists(String identifier) {
        return users.contains(identifier.toLowerCase());
    }

    synchronized public void close() {
        ap2pc.getLibrary().removeConversation(this);
        for (int i = clients.size() - 1; i >= 0; i--) {
            clients.get(i).terminate();
        }
    }

    synchronized public void refreshUpdate(ConversationStanza cr) {
        String mid = cr.getId();
        if (!log.containsKey(mid)) {
            log.put(mid, Calendar.getInstance().getTimeInMillis());
            cleanup();
            boolean updatePerformed = false;
            if (!lastRefreshId.equalsIgnoreCase(cr.getRefresh())) {
                return;
            }
            List<String> u = cr.getUsers();
            for (int i = 0; i < u.size(); i++) {
                if (userExists(u.get(i))) {
                    continue;
                }
                addUser(u.get(i));
                updatePerformed = true;
            }
            if (updatePerformed) {
                ConversationStanza cs = new ConversationStanza();
                cs.setFrom(ap2pc.getMe().getIdentifier());
                cs.setUsers(getUsers());
                cs.setType("update");
                cs.setTo(identifier);
                cs.setRefresh(cr.getRefresh());
                cs.setId(ConversationLibrary.generateUniqueIdentifier(ap2pc));
                for (int i = 0; i < clients.size(); i++) {
                    clients.get(i).sendInfo(cs);
                }
            }
        }
    }

    synchronized public void respondToRefresh(ConversationStanza cr) {
        String mid = cr.getId();
        if (!log.containsKey(mid)) {
            for (int i = getUsers().size() - 1; i >= 0; i--) {
                removeUser(getUsers().get(i));
            }
            addUser(ap2pc.getMe().getIdentifier());
            log.put(mid, Calendar.getInstance().getTimeInMillis());
            cleanup();
            ConversationStanza cs = new ConversationStanza();
            cs.setFrom(ap2pc.getMe().getIdentifier());
            cs.setRefresh(cr.getId());
            cs.setType("update");
            cs.setTo(identifier);
            cs.setId(ConversationLibrary.generateUniqueIdentifier(ap2pc));
            ArrayList<String> u = new ArrayList<String>();
            u.add(ap2pc.getMe().getIdentifier());
            for (int i = 0; i < clients.size(); i++) {
                u.add(clients.get(i).getEndpoint());
            }
            cs.setUsers(u);
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).sendInfo(cr);
                clients.get(i).sendInfo(cs);
            }

            lastRefreshId = cr.getId();
        }
    }

    synchronized public void requestRefresh() {
        ConversationStanza cs = new ConversationStanza();
        cs.setFrom(ap2pc.getMe().getIdentifier());
        cs.setType("refresh");
        cs.setTo(identifier);
        cs.setId(ConversationLibrary.generateUniqueIdentifier(ap2pc));

        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendInfo(cs);
        }
        String mid = cs.getId();
        for (int i = getUsers().size() - 1; i >= 0; i--) {
            removeUser(getUsers().get(i));
        }
        addUser(ap2pc.getMe().getIdentifier());
        log.put(mid, Calendar.getInstance().getTimeInMillis());
        cleanup();
        lastRefreshId = mid;
    }

    synchronized public void processReqConn(ConversationStanza cs) {
        String mid = cs.getId();
        if (!log.containsKey(mid)) {
            log.put(mid, Calendar.getInstance().getTimeInMillis());
            cleanup();
            if (cs.getFrom().equalsIgnoreCase(ap2pc.getMe().getIdentifier())) {
                return;
            }
            int rcon = Integer.parseInt(cs.getId());
            if (clients.size() < 2 && getUsers().size() > 2) {
                if (rcon < contention) {
                    ap2pc.getNet().rejoin(cs);
                }
            } else if (needsUsers()) {
                if (rcon < contention / 2) {
                    ap2pc.getNet().rejoin(cs);
                }
            } else if (canTakeUsers()) {
                if (rcon < contention / clients.size()) {
                    ap2pc.getNet().rejoin(cs);
                }
            } else {
                for (int i = 0; i < clients.size(); i++) {
                    clients.get(i).sendInfo(cs);
                }
            }
        }
    }

    synchronized public void processHi(ConversationStanza cr) {
//        if (needsUsers()) {
//            ConversationStanza cs = new ConversationStanza();
//            cs.setFrom(ap2pc.getMe().getIdentifier());
//            cs.setTo(this.identifier);
//            cs.setType("reqconn");
//            cs.setIp(clients.get(0).getLocalIP());
//            cs.setId(ConversationLibrary.generateUniqueIdentifier(ap2pc));
//            for (int i = 0; i < clients.size(); i++) {
//                clients.get(i).sendInfo(cs);
//            }
//        }
    }

    synchronized public ArrayList<String> getUsers() {
        return users;
    }

    public ConversationStanza generateAccept(String to) {
        ConversationStanza cs = new ConversationStanza();
        cs.setTo(to);
        cs.setFrom(ap2pc.getMe().getIdentifier());
        cs.setType("accept");
        cs.setName(name);
        cs.setTopic(topic);
        cs.setId(identifier);
        return cs;
    }

    private void addUser(String identifier) {
        if (users.contains(identifier.toLowerCase())) {
            return;
        }
        users.add(identifier.toLowerCase());
        visual.listNotify(users.size() - 1, ConversationVisual.CHANGETYPE.ADD);

        if (clients.size() < 2 && getUsers().size() > 2) {
            ConversationStanza cs = new ConversationStanza();
            cs.setFrom(ap2pc.getMe().getIdentifier());
            cs.setTo(this.identifier);
            cs.setType("reqconn");
            cs.setIp(clients.get(0).getLocalIP());
            int contention = generator.nextInt();
            this.contention = contention;
            cs.setId(contention + "");
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).sendInfo(cs);
            }
        }
    }

    synchronized public boolean needsUsers() {
        return Math.pow(2, clients.size() - 1) < getUsers().size();
    }

    synchronized public boolean canTakeUsers() {
        return Math.pow(2, clients.size() - 2) < getUsers().size();
    }

    synchronized public boolean recentlyCalledForReqConn() {
        return false; //(Calendar.getInstance().getTimeInMillis() - lastReqConn) < TIMEOUT_MIN * 60000;
    }

    private void removeUser(String identifier) {
        if (!users.contains(identifier.toLowerCase())) {
            return;
        }
        int index = users.indexOf(identifier);
        if (index == -1) {
            return;
        }
        users.remove(index);
        visual.listNotify(index, ConversationVisual.CHANGETYPE.REMOVE);
    }
}

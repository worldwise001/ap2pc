/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.main;

import ap2pc.main.visual.MainVisual;
import ap2pc.main.conversation.ConversationMessage;
import ap2pc.main.conversation.Conversation;
import ap2pc.main.conversation.ConversationLibrary;
import ap2pc.net.conn.XMPPClient;
import ap2pc.net.stanza.obj.PresenceStanza;
import ap2pc.net.stanza.obj.Stanza;
import ap2pc.net.thread.Client;
import ap2pc.net.thread.DiscoveryThread;
import ap2pc.net.thread.Server;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author sarah
 */
public class AP2PC {

    private HashMap<String, User> users = new HashMap<String, User>();
    private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
    private ConversationLibrary library = new ConversationLibrary(this);
    private MainVisual visual;
    private Network net = new Network(this);
    private boolean run = true;
    private Me me = new Me();
    public static final String LANG = "en";

    public AP2PC(MainVisual v) {
        visual = v;
    }

    public void init() {
        run = true;
        net.init();
    }

    synchronized public void terminate() {
        net.terminate();
        System.exit(0);
    }

    synchronized public void updateUser(PresenceStanza ps) {
        String identifier = ps.getFrom().toLowerCase();
        if (users.containsKey(identifier)) {
            if (ps.getType().equalsIgnoreCase("unavailable")) {
                int index = getIndex(identifier);
                users.remove(identifier);
                visual.contactListNotify(index, MainVisual.CHANGETYPE.REMOVE);
                log("User " + identifier + " has gone offline");
            } else {
                int index = getIndex(identifier);
                User u = users.get(identifier);
                u.setLastSeen(Calendar.getInstance().getTimeInMillis());
                if (Me.stringToShow(ps.getShow()) != u.getShow()) {
                    u.setShow(Me.stringToShow(ps.getShow()));
                    log("User " + identifier + " is now " + ps.getShow());
                    visual.contactListNotify(index, MainVisual.CHANGETYPE.MODIFY);
                }
            }
        } else {
            User u = new User();
            String[] s = identifier.split("@");
            u.setHost(s[1]);
            u.setIp(ps.getOriginator().getHostAddress());
            u.setLastSeen(Calendar.getInstance().getTimeInMillis());
            u.setUsername(s[0]);
            u.setShow(Me.stringToShow(ps.getShow()));
            u.setStatus(ps.getStatus());
            users.put(identifier, u);
            int index = getIndex(identifier);
            visual.contactListNotify(index, MainVisual.CHANGETYPE.ADD);
            log("User " + identifier + " has come online");
        }
    }

    synchronized public boolean running() {
        return run;
    }

    synchronized public void log(String s) {
        visual.updateProgramStatus(s);
    }

    public Me getMe() {
        return me;
    }

    public ConversationLibrary getLibrary() {
        return library;
    }

    public MainVisual getVisual() {
        return visual;
    }

    public Network getNet() {
        return net;
    }

    synchronized public HashMap<String, User> getUserHashMap() {
        return users;
    }

    synchronized private int getIndex(String identifier) {
        String[] ids = new String[users.size()];
        ids = users.keySet().toArray(ids);
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equalsIgnoreCase(identifier)) {
                return i;
            }
        }
        return -1;
    }
}

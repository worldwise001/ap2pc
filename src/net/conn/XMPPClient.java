/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.conn;

import ap2pc.main.AP2PC;
import ap2pc.main.Network;
import ap2pc.net.stanza.obj.ConversationStanza;
import ap2pc.net.stanza.obj.Stanza;
import ap2pc.net.stanza.obj.StanzaFormatter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author sarah
 */
public class XMPPClient {

    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private String identifier;
    private String endpoint;
    private boolean aux = false;
    private ConversationStanza acceptcs;

    public String getIdentifier() {
        return identifier;
    }

    public XMPPClient(String host) throws UnknownHostException, IOException {
        socket = new Socket(host, Network.TCP_PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public XMPPClient(Socket s) throws IOException {
        socket = s;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendStanza(Stanza s) {
        if (s == null) {
            System.err.println("Stanza is null!");
            return;
        }
//        System.err.println("T<< " + StanzaFormatter.getInstance().stanzaToXML(s));
        String str = StanzaFormatter.getInstance().stanzaToXML(s);

        try {
            output.write(str);
            output.flush();
        } catch (IOException e) {
        }
    }

    public Stanza receiveStanza() throws EOFException {
        try {
            String str = getXMLBlob();
            if (str.length() < 2) {
                throw new EOFException();
            } else {
                Stanza s = StanzaFormatter.getInstance().XMLToStanza(str);
//                System.err.println("T>> " + StanzaFormatter.getInstance().stanzaToXML(s));
                return s;
            }
        } catch (SocketException e) {
            terminate();
            throw new EOFException();
        } catch (EOFException e) {
            terminate();
            throw e;
        } catch (IOException e) {
            terminate();
            throw new EOFException();
        }
    }

    private String getXMLBlob() throws EOFException, IOException {
        String data = "", xmlInfo = "", tmp = "", tag = "", text = "";
        int c = 0;
        c = input.read();
        if (c == '<') {
            data += (char) c;
            c = input.read();
            if (c == '?') {
                xmlInfo = data;
                xmlInfo += (char) c;
                data = "";
                c = input.read();
                while (c != '>' && c != -1) {
                    xmlInfo += (char) c;
                    c = input.read();
                }
                if (c == -1) {
                    throw new IOException();
                }
                xmlInfo += (char) c;
                while (c != '<' && c != -1) {
                    c = input.read();
                }
                if (c == -1) {
                    throw new IOException();
                }
            }

            while (c != '>' && c != '/' && c != -1) {
                data += (char) c;
                c = input.read();
            }
            if (c == -1) {
                throw new IOException();
            }
            if (c == '/') {
                data += (char) c;
                c = input.read();
            }
            data += (char) c;
            if (data.substring(0, 2).equalsIgnoreCase("</")) {
                throw new EOFException(data.substring(2, data.length() - 1));
            }
            int pos_space = data.indexOf(' ');
            int pos_brack = data.indexOf('>');
            tag = data.substring(1, pos_space);
            if (pos_space == -1) {
                tag = data.substring(1, pos_brack);
            }
            if (data.charAt(data.length() - 2) != '/') {
                while (true) {
                    tmp = "";
                    c = input.read();
                    if (c == '<') {
                        data += text;
                        text = "";
                        tmp += (char) c;
                        c = input.read();
                        while (c != '>' && c != -1) {
                            tmp += (char) c;
                            c = input.read();
                        }
                        if (c == -1) {
                            throw new IOException();
                        }
                        tmp += (char) c;
                        data += tmp;
                        if (tmp.equalsIgnoreCase("</" + tag + ">")) {
                            break;
                        }
                    } else if (c == -1) {
                        throw new IOException();
                    } else {
                        text += (char) c;
                    }
                }
            }
        } else if (c == -1) {
            throw new IOException();
        }
        return xmlInfo + data;
    }

    //client calls this
    synchronized public boolean handshake(AP2PC ap2pc, String identifier, String endpoint, boolean rejoin) throws XMLStreamException {
        if (rejoin) {
            return rejoinhandshake(ap2pc, identifier, endpoint);
        }
        return normhandshake(ap2pc, identifier, endpoint);
    }

    private boolean normhandshake(AP2PC ap2pc, String identifier, String endpoint) throws XMLStreamException {
        char[] buff = new char[8];
        try {
            output.write("<stream>");
            output.flush();
            input.read(buff);
            if (!(new String(buff)).equalsIgnoreCase("<stream>")) {
                throw new XMLStreamException();
            }
            if (identifier != null) { //client
                ConversationStanza c = new ConversationStanza();
                c.setFrom(ap2pc.getMe().getIdentifier());
                c.setId(identifier);
                c.setType("hi");
                sendStanza(c);
                this.identifier = identifier;
                this.endpoint = endpoint;
                Stanza s = receiveStanza();
                if (!(s instanceof ConversationStanza)) {
                    error();
                    return false;
                }
                ConversationStanza cs = (ConversationStanza) s;
                if (cs.getType().equalsIgnoreCase("accept")) {
                    acceptcs = cs;
                    return true;
                } else {
                    return false;
                }
            } else { //server
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                String blob = getXMLBlob();
                Stanza s = StanzaFormatter.getInstance().XMLToStanza(blob);
                if (!(s instanceof ConversationStanza)) {
                    error();
                    return false;
                } else {
                    ConversationStanza c = (ConversationStanza) s;
                    if (!c.getType().equalsIgnoreCase("hi") && !c.getType().equalsIgnoreCase("resconn")) {
                        error();
                        return false;
                    } else {
                        this.identifier = c.getId();
                        this.endpoint = c.getFrom();
                        if (c.getType().equalsIgnoreCase("resconn")) {
                            aux = true;
                        }
                    }
                }
            }

        } catch (IOException e) {
        }
        return true;
    }

    private boolean rejoinhandshake(AP2PC ap2pc, String identifier, String endpoint) throws XMLStreamException {
        char[] buff = new char[8];
        try {
            output.write("<stream>");
            output.flush();
            input.read(buff);
            if (!(new String(buff)).equalsIgnoreCase("<stream>")) {
                throw new XMLStreamException();
            }
            ConversationStanza c = new ConversationStanza();
            c.setFrom(ap2pc.getMe().getIdentifier());
            c.setId(identifier);
            c.setType("resconn");
            sendStanza(c);
            this.identifier = identifier;
            this.endpoint = endpoint;
            Stanza s = receiveStanza();
            if (!(s instanceof ConversationStanza)) {
                error();
                return false;
            }
            ConversationStanza cs = (ConversationStanza) s;
            if (cs.getType().equalsIgnoreCase("accept")) {
                acceptcs = cs;
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
        }
        return true;
    }

    //server calls this
    public void handshake(AP2PC ap2pc) throws XMLStreamException {
        handshake(ap2pc, null, null, false);
    }

    private void error() {
        terminate();
    }

    public void terminate() {
        try {
            output.flush();
            output.write("</stream>");
            output.flush();
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
        }

    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public ConversationStanza getAcceptcs() {
        return acceptcs;
    }

    public String getRemoteIP() {
        return socket.getInetAddress().getHostAddress();
    }

    public boolean isAux() {
        return aux;
    }
}

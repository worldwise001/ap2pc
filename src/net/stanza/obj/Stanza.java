/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.net.stanza.obj;

import java.net.InetAddress;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author sarah
 */
public abstract class Stanza {

    protected abstract JAXBElement createJAXBElement();
    private InetAddress o;

    public InetAddress getOriginator() {
        return o;
    }

    public void putOriginator(InetAddress originator) {
        o = originator;
    }

    public String toString() {
        return StanzaFormatter.getInstance().stanzaToXML(this);
    }
}

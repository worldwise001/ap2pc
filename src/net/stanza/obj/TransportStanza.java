/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ap2pc.net.stanza.obj;

/**
 *
 * @author sarah
 */
public abstract class TransportStanza extends Stanza {

    public abstract String getFrom();
    public abstract String getTo();
    public abstract String getType();
    public abstract String getId();
    public abstract String getXmlLang();

}

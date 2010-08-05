/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ap2pc.net.stanza.obj;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author sarah
 */
public class StanzaFormatter
{
    private static final StanzaFormatter instance = new StanzaFormatter();
    private JAXBContext jaxbContext = null;
    private Unmarshaller unmarshaller = null;
    private Marshaller marshaller = null;
    private JAXBElement e = null;
    private ObjectFactory obf = null;

    private StanzaFormatter()
    {
        try {
            jaxbContext = JAXBContext.newInstance("ap2pc.net.stanza.obj");
            unmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
            obf = new ObjectFactory();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static StanzaFormatter getInstance()
    {
        return instance;
    }

    synchronized public Stanza XMLToStanza(String s)
    {
        Stanza p = null;
        try {
            Object uo = unmarshaller.unmarshal( new StreamSource( new StringReader( s ) ) );
            e = (JAXBElement)uo;
            Object o = e.getValue();
            if (!(o instanceof Stanza)) return null;
            p = (Stanza)o;
        } catch (JAXBException ex) { ex.printStackTrace(); }
        return p;
    }

    synchronized public String stanzaToXML(Stanza p)
    {
        e = p.createJAXBElement();
        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            public void write(int b) throws IOException { this.string.append((char) b ); }
            public String toString() { return this.string.toString();}
        };
        try {
            marshaller.marshal(e, output);
        } catch (JAXBException ex) { ex.printStackTrace(); }

        return output.toString();
    }

    public ObjectFactory getObjectFactory() { return obf; }
}

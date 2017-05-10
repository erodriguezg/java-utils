package cl.zeke.framework.utils;

/**
 * Created by takeda on 03-01-16.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XmlUtils {

    private final static Logger LOG = LoggerFactory.getLogger(XmlUtils.class);

    public final <T> String marshal(T generic) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(generic.getClass());
            Marshaller m = context.createMarshaller();
            m.marshal(generic, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            LOG.error("error marshall", ex);
            return null;
        }
    }

    public final <T> T unmarshal(String xml, Class<T> clazz) {
        if (xml == null || xml.isEmpty()) {
            return null;
        }
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            return (T) um.unmarshal(is);
        } catch (JAXBException ex) {
            LOG.error("error unmarshall", ex);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
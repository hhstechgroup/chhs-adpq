package com.engagepoint.cws.apqd.service.usps;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
public class JaxbUtils {

    public static String marshal(Object obj, Marshaller marshaller) throws Exception {
        StringWriter sw = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public static Object unmarshal(String xml, Unmarshaller unmarshaller) throws Exception {
        StringReader reader = new StringReader(xml);
        return unmarshaller.unmarshal(reader);
    }
}

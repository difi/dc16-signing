package no.difi.signing;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class GenerateManifestXML {

    public void simple() {
        try {
            JAXBContext jc = JAXBContext.newInstance("test.jaxb");
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(null, null):
        } catch (Exception e) {

        }
    }
}

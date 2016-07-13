import com.google.common.io.ByteStreams;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.Signature;
import no.digipost.signature.client.portal.SignatureStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortalSignedDocumentFetcher {
    private PortalJobPoller poller;
    private PortalClient client;
    PortalSignedDocumentFetcher(PortalJobPoller poller, PortalClient portalClient){
        this.poller = poller;
        this.client = portalClient;
    }
    public String getPades() throws IOException{
        if(poller.isPadesReady()){
            InputStream pAdESStream = client.getPAdES(poller.getStatusChange().getpAdESUrl());
            OutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + "pAdESTest.pdf");

            int read = 0;

            byte[] bytes = new byte[999999999];
            //byte[] buffer = new byte[pAdESStream.available()];

            while ((read = pAdESStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            //pAdESStream.read(buffer);
            //File targetFile = new File(System.getProperty("user.dir") + "pAdES.pdf");
            //OutputStream outStream = new FileOutputStream(targetFile);
            //outStream.write(buffer);
            return "pades retrieved";
        } else {
            return "pades not ready or failed";
        }
    }

    public String getXades() throws IOException {
        if (poller.isXadesReady()) {
            List<InputStream> inputStreams;
            inputStreams = poller.getStatusChange().getSignatures().stream()
                    .filter(z -> z.is(SignatureStatus.SIGNED))
                    .map(Signature::getxAdESUrl)
                    .map(client::getXAdES)
                    .collect(Collectors.toList());

            if (!inputStreams.isEmpty()) {

                for (InputStream inputStream : inputStreams) {
                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    File targetFile = new File(System.getProperty("user.dir") +inputStreams.indexOf(inputStream) +  "xAdES.xml");
                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);

                }

                return "got xades files";

            } else {
                return "no xades available";
            }




        }
        return "no xades available";
    }


}

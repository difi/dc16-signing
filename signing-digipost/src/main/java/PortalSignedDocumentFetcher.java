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
            byte[] buffer = new byte[pAdESStream.available()];
            pAdESStream.read(buffer);
            File targetFile = new File(System.getProperty("user.dir") + "pades.pdf");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
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
                    File targetFile = new File(System.getProperty("user.dir") +inputStreams.indexOf(inputStream) +  "targetFile2.pdf");
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

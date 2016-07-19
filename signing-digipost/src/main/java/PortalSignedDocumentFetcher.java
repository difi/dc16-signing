import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.Signature;
import no.digipost.signature.client.portal.SignatureStatus;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class PortalSignedDocumentFetcher {
    private PortalJobPoller poller;
    private PortalClient client;

    PortalSignedDocumentFetcher(PortalJobPoller poller, PortalClient portalClient) {
        this.poller = poller;
        this.client = portalClient;
    }

    //Uses poller to check if the pAdES is available, then reads it to a file. pAdES is only available
    //if all signers have signed.
    public String getPades() throws IOException {
        if (poller.isPadesReady()) {
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

    //Writes all xAdES files. Uses poller to check if it can be retrieved.
    // TODO: Asking for one specific signers xAdES file. Better outputs.
    //There is one xAdES file for each signer.
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
                    File targetFile = new File(System.getProperty("user.dir") + inputStreams.indexOf(inputStream) + "targetFile2.pdf");
                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);
                }
                return "got xAdES files";
            } else {
                return "no xAdES available";
            }
        }
        return "no xades available";
    }
}

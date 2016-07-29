package no.difi.signing.digipost;

import com.google.common.io.ByteStreams;
import no.digipost.signature.client.core.PAdESReference;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.Signature;
import no.digipost.signature.client.portal.SignatureStatus;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class PortalSignedDocumentFetcher {
    private PortalJobPoller poller;
    private PortalClient client;

       public PortalSignedDocumentFetcher(PortalJobPoller poller, PortalClient portalClient) {
        this.poller = poller;
        this.client = portalClient;
    }

    /**
     * Uses poller to check if the pAdES is available, then reads it to a file. pAdES is only available if all signers have signed.
     */
    public byte[] getPades() throws IOException {
        PAdESReference pAdESReference = null;

        if ((poller != null) && (poller.isPadesReady())) {
            pAdESReference = poller.getStatusChange().getpAdESUrl();
            return ByteStreams.toByteArray(client.getPAdES(pAdESReference));
        } else {
            return "".getBytes();
        }
    }

    /**
     * Writes all xAdES files. Uses poller to check if it can be retrieved. There is one xAdES file for each signer.
     * TODO: Asking for one specific signers xAdES file. Better outputs.
     */
    public String getXades() throws IOException {
        if ((poller != null) && (poller.isXadesReady())) {
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
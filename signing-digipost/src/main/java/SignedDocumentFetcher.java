import com.google.common.io.ByteStreams;
import no.digipost.signature.client.core.PAdESReference;
import no.digipost.signature.client.core.XAdESReference;
import no.digipost.signature.client.core.exceptions.TooEagerPollingException;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobStatus;
import no.digipost.signature.client.direct.DirectJobStatusResponse;

import java.io.*;

public class SignedDocumentFetcher {
    private DirectClient client;
    private DirectJobStatusResponse statusResponse;
    private StatusReader statusReader;

    SignedDocumentFetcher(DirectClient client, StatusReader statusReader) {
        this.client = client;
        try {
            this.statusResponse = client.getStatusChange();
        } catch (TooEagerPollingException eagerPollingException) {
            String nextAvailablePollingTime = eagerPollingException.getNextPermittedPollTime().toString();
            System.out.print(nextAvailablePollingTime);
        }
        this.statusReader = statusReader;

    }

    public InputStream getSignedDocuments(String format) {
        if (statusResponse.is(DirectJobStatus.SIGNED)) {
            if (format == "xades") {
                return client.getXAdES(statusResponse.getxAdESUrl());
            } else if (format == "pades") {
                return client.getPAdES(statusResponse.getpAdESUrl());
            }
        }
        return null;
    }

    public byte[] getPades() throws IOException {
        PAdESReference pAdESReference = null;
        try{
        DirectJobStatusResponse directJobStatusResponse = this.statusReader.getStatusResponse();

        if (directJobStatusResponse.is(directJobStatusResponse.getStatus().SIGNED)) {
            pAdESReference = directJobStatusResponse.getpAdESUrl();
            XAdESReference xAdESReference = directJobStatusResponse.getxAdESUrl();

            InputStream pAdESStream = client.getPAdES(pAdESReference);
            System.out.println(pAdESReference.getpAdESUrl());
            System.out.println(xAdESReference.getxAdESUrl());
            OutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + "pAdESTest.pdf");
            int read = 0;

            byte[] bytes = new byte[999999999];
            while ((read = pAdESStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        } catch (TooEagerPollingException eagerPollingException) {
                String nextAvailablePollingTime = eagerPollingException.getNextPermittedPollTime().toString();
                System.out.print(nextAvailablePollingTime);
                //return "Too frequent polling, please wait until " + nextAvailablePollingTime;
                return "".getBytes();
            }
            return ByteStreams.toByteArray(client.getPAdES(pAdESReference));
    }



    public byte[] getXades() throws IOException {
        XAdESReference xAdESReference = null;
        DirectJobStatusResponse directJobStatusResponse = this.statusReader.getStatusResponse();
        if (directJobStatusResponse.is(directJobStatusResponse.getStatus().SIGNED)) {
            xAdESReference = directJobStatusResponse.getxAdESUrl();
            InputStream xAdESStream = client.getXAdES(this.statusReader.getStatusResponse().getxAdESUrl());
            byte[] buffer = new byte[xAdESStream.available()];
            xAdESStream.read(buffer);

            File targetFile = new File(System.getProperty("user.dir") + "xAdES.xml");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            this.statusReader.confirmProcessedSignatureJob();

            return ByteStreams.toByteArray(client.getXAdES(xAdESReference));
        } else return "".getBytes();
    }


}

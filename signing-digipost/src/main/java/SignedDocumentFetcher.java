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

    public byte[] getPades() throws IOException {
        PAdESReference pAdESReference = null;
        if(this.statusReader != null){
            if(this.statusReader.getStatusResponse().isPresent()){
                DirectJobStatusResponse directJobStatusResponse = this.statusReader.getStatusResponse().get();
                if (directJobStatusResponse.is(directJobStatusResponse.getStatus().SIGNED)) {
                    pAdESReference = directJobStatusResponse.getpAdESUrl();

                    return ByteStreams.toByteArray(client.getPAdES(pAdESReference));
                }
            } return "".getBytes();

        }
        return "".getBytes();
    }

    public byte[] getXades() throws IOException {
        XAdESReference xAdESReference = null;
        if (this.statusReader != null) {
            DirectJobStatusResponse directJobStatusResponse = this.statusReader.getStatusResponse().get();
            if (directJobStatusResponse.is(directJobStatusResponse.getStatus().SIGNED)) {
                xAdESReference = directJobStatusResponse.getxAdESUrl();

                return ByteStreams.toByteArray(client.getXAdES(xAdESReference));
            }
        }
        return "".getBytes();
    }
}
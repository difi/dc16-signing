import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobStatus;
import no.digipost.signature.client.direct.DirectJobStatusResponse;

import java.io.InputStream;

public class SignedDocumentFetcher {
    private DirectClient client;
    private DirectJobStatusResponse statusResponse;

    SignedDocumentFetcher(DirectClient client){
        this.client = client;
        this.statusResponse = client.getStatusChange();

    }

    public InputStream getSignedDocuments(String format) {
        if(statusResponse.is(DirectJobStatus.SIGNED)){
            if(format == "xades"){
                return client.getXAdES(statusResponse.getxAdESUrl());
            } else if (format == "pades") {
                return client.getPAdES(statusResponse.getpAdESUrl());
            }
        }
        return null;
    }






}

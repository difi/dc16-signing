package no.difi.signing.api;

import java.io.IOException;

public interface SigningService {

    String initiateSigning(String conversationId, Document document, String pid) throws IOException;

    void fetchSignedResources(String conversationId, String queryToken);

}

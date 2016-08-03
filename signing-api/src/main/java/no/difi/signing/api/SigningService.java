package no.difi.signing.api;

import java.io.IOException;

public interface SigningService {

    String initiateSigning(ConversationStub conversation, Document document, String pid) throws IOException;

    void fetchSignedResources(ConversationStub conversation, String queryToken);

}

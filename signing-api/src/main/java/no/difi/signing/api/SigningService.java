package no.difi.signing.api;

import no.difi.signing.lang.SigningException;

import java.io.IOException;

public interface SigningService {

    String initiateSigning(ConversationStub conversation, Document document, String pid) throws IOException, SigningException;

    void fetchSignedResources(ConversationStub conversation, String queryToken, String pid) throws IOException, SigningException;

}

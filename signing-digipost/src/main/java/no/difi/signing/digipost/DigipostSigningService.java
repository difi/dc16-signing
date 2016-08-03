package no.difi.signing.digipost;

import no.difi.signing.api.ConversationStub;
import no.difi.signing.api.Document;
import no.difi.signing.api.SigningService;
import no.digipost.signature.client.direct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DigipostSigningService implements SigningService {

    private static Logger logger = LoggerFactory.getLogger(DigipostSigningService.class);

    @Value("${digipost.uri.completion}")
    private String uriCompletion;
    @Value("${digipost.uri.rejection}")
    private String uriRejection;
    @Value("${digipost.uri.error}")
    private String uriError;

    @Autowired
    private DirectClient directClient;

    @Override
    public String initiateSigning(ConversationStub conversation, Document document, String pid) throws IOException {
        logger.info("Initiate document '{}' for '{}'.", document.getToken(), pid);

        DirectDocument directDocument = DirectDocument.builder(document.getTitle(), String.format("%s.pdf", document.getToken()), document.getByteArray()).build();
        DirectSigner signer = DirectSigner.builder(pid).build();
        DirectJob directJob = new DirectJob.Builder(signer, directDocument,
                uriWithConversationId(uriCompletion, conversation.getIdentifier()),
                uriWithConversationId(uriRejection, conversation.getIdentifier()),
                uriWithConversationId(uriError, conversation.getIdentifier())).build();
        DirectJobResponse response = directClient.create(directJob);

        if (response == null) {
            logger.info("Unable to initiate job.");
            return null;
        } else {
            conversation.setDigipostSignatureJobId(response.getSignatureJobId());
            conversation.setDigipostRedirectUrl(response.getRedirectUrl());
            conversation.setDigipostStatusUrl(response.getStatusUrl());

            logger.info("Status url: {}", response.getStatusUrl());
            return response.getRedirectUrl();
        }
    }

    @Override
    public void fetchSignedResources(ConversationStub conversation, String queryToken) {
        DirectJobResponse response = new DirectJobResponse(
                conversation.getDigipostSignatureJobId(),
                conversation.getDigipostRedirectUrl(),
                conversation.getDigipostStatusUrl());

        DirectJobStatusResponse directJobStatusResponse = directClient
                .getStatus(StatusReference.of(response).withStatusQueryToken(queryToken));

        logger.info("{}", directJobStatusResponse);
    }

    private String uriWithConversationId(String uri, String conversationId) {
        return String.format("%s?conversation=%s", uri, conversationId);
    }
}

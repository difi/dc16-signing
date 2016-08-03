package no.difi.signing.digipost;

import com.google.common.io.ByteStreams;
import no.difi.signing.api.ConversationStub;
import no.difi.signing.api.Document;
import no.difi.signing.api.SigningService;
import no.difi.signing.lang.SigningException;
import no.digipost.signature.client.direct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is the class implementing functionality for Digipost as provider of a signing service.
 */
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

    @Autowired
    @Qualifier("storage")
    private Path storagePath;

    @Override
    public String initiateSigning(ConversationStub conversation, Document document, String pid) throws IOException, SigningException {
        logger.info("[{}] Initiate document '{}' for '{}'.", conversation.getIdentifier(), document.getToken(), pid);

        // Create document for signing.
        DirectDocument directDocument = DirectDocument.builder(
                document.getTitle(), String.format("%s.pdf", document.getToken()), document.getByteArray()).build();
        DirectSigner signer = DirectSigner.builder(pid).build();

        // Create signing job.
        DirectJob directJob = new DirectJob.Builder(signer, directDocument,
                uriWithConversationId(uriCompletion, conversation),
                uriWithConversationId(uriRejection, conversation),
                uriWithConversationId(uriError, conversation)).build();
        DirectJobResponse response = directClient.create(directJob);

        if (response == null)
            throw new SigningException("Unable to initiate job.");

        // Add relevant information to conversation until signing is completed.
        conversation.setDigipostSignatureJobId(response.getSignatureJobId());
        conversation.setDigipostRedirectUrl(response.getRedirectUrl());
        conversation.setDigipostStatusUrl(response.getStatusUrl());

        logger.info("Status url: {}", response.getStatusUrl());

        return response.getRedirectUrl();
    }

    @Override
    public void fetchSignedResources(ConversationStub conversation, String queryToken) throws IOException, SigningException {
        // Recreate DirectJobResponse to fetch status.
        DirectJobResponse response = new DirectJobResponse(
                conversation.getDigipostSignatureJobId(),
                conversation.getDigipostRedirectUrl(),
                conversation.getDigipostStatusUrl());

        // Fetch status
        DirectJobStatusResponse directJobStatusResponse =
                directClient.getStatus(StatusReference.of(response).withStatusQueryToken(queryToken));

        logger.info("[{}] {}", conversation.getIdentifier(), directJobStatusResponse);

        // Make sure status is as expected.
        if (!directJobStatusResponse.is(DirectJobStatus.SIGNED))
            throw new SigningException(String.format("Received status '%s'.", directJobStatusResponse.getStatus()));

        // Initiate storage folder.
        Path jobPath = storagePath.resolve(conversation.getIdentifier());
        Files.createDirectories(jobPath);

        // Download PAdES resource.
        try (OutputStream outputStream = Files.newOutputStream(jobPath.resolve("pades.pdf"))) {
            ByteStreams.copy(directClient.getPAdES(directJobStatusResponse.getpAdESUrl()), outputStream);
        }

        // Download XAdES resource.
        try (OutputStream outputStream = Files.newOutputStream(jobPath.resolve("xades.xml"))) {
            ByteStreams.copy(directClient.getXAdES(directJobStatusResponse.getxAdESUrl()), outputStream);
        }

        // Notify Digipost when resources are fetched.
        directClient.confirm(directJobStatusResponse);

        logger.info("[{}] Completed", conversation.getIdentifier());
    }

    private String uriWithConversationId(String uri, ConversationStub conversation) {
        return String.format("%s?conversation=%s", uri, conversation.getIdentifier());
    }
}

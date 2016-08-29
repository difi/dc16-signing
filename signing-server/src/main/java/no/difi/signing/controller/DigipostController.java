package no.difi.signing.controller;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.api.SigningService;
import no.difi.signing.lang.SigningException;
import no.difi.signing.model.Conversation;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.ConversationRepository;
import no.difi.signing.repository.SignatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/digipost")
public class DigipostController {

    private static Logger logger = LoggerFactory.getLogger(DigipostController.class);

    @Autowired
    private SigningService signingService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping("/completion")
    public String onCompletion(@RequestParam("conversation") String conversationId,
                               @RequestParam("status_query_token") String queryToken) throws IOException, SigningException {
        logger.info("[{}] Returned to 'completion'.", conversationId);

        Conversation conversation = getConversation(conversationId);

        Document document = documentRepository.findByToken(conversation.getDocumentToken());
        Signature signature = conversation.toSignature();
        signature.setDocumentTitle(document.getTitle());
        signature.setDocumentVersion(document.getVersion());

        signingService.fetchSignedResources(conversation, queryToken, httpServletRequest.getHeader("X-DifiProxy-pid"));

        signatureRepository.save(signature);
        conversationRepository.delete(conversation);

        Optional<String> redirectUri = Optional.ofNullable(conversation.getRedirectUri());
        redirectUri = redirectUri.map(s -> String.format("%s%ssignature=%s", s, s.contains("?") ? "&" : "?", signature.getIdentifier()));
        return String.format("redirect:%s", redirectUri.orElse("/overview"));
    }

    @RequestMapping("/rejection")
    public String onRejection(@RequestParam("conversation") String conversationId) throws SigningException {
        logger.info("[{}] Returned to 'rejection'.", conversationId);

        Conversation conversation = getConversation(conversationId);
        conversationRepository.delete(conversation);

        Optional<String> redirectUri = Optional.ofNullable(conversation.getRedirectUri());
        return String.format("redirect:%s", redirectUri.orElse("/overview"));
    }

    @RequestMapping("/error")
    public String onError(@RequestParam("conversation") String conversationId) throws SigningException {
        logger.info("[{}] Returned to 'error'.", conversationId);

        Conversation conversation = getConversation(conversationId);
        conversationRepository.delete(conversation);

        Optional<String> redirectUri = Optional.ofNullable(conversation.getRedirectUri());
        return String.format("redirect:%s", redirectUri.orElse("/overview"));
    }

    private Conversation getConversation(String conversationId) throws SigningException {
        return Optional.ofNullable(conversationRepository.findByIdentifier(conversationId))
                // Make sure pid is the same when returning as when initiating signing.
                .filter(c -> c.getPid().equals(httpServletRequest.getHeader("X-DifiProxy-pid")))
                .orElseThrow(() -> new SigningException(String.format("Unable to find conversation '%s'.", conversationId)));
    }
}

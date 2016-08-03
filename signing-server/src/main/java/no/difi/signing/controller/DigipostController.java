package no.difi.signing.controller;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.api.SigningService;
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
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/completion")
    @ResponseBody
    public String onCompletion(@RequestParam("conversation") String conversationId, @RequestParam("status_query_token") String queryToken) {
        Conversation conversation = conversationRepository.findByIdentifier(conversationId);

        Document document = documentRepository.findByToken(conversation.getDocumentToken());
        Signature signature = conversation.toSignature();
        signature.setDocumentTitle(document.getTitle());
        signature.setDocumentVersion(document.getVersion());

        signingService.fetchSignedResources(conversation, queryToken);

        signatureRepository.save(signature);
        conversationRepository.delete(conversation);

        return String.format("QueryToken: %s", queryToken);
    }

    @RequestMapping("/rejection")
    public void onRejection(@RequestParam("conversation") String conversationId) {

    }

    @RequestMapping("/error")
    public void onError(@RequestParam("conversation") String conversationId) {

    }

}

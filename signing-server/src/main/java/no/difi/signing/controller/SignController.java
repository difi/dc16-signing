package no.difi.signing.controller;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.api.SigningService;
import no.difi.signing.lang.SigningException;
import no.difi.signing.model.Conversation;
import no.difi.signing.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SigningService signingService;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @RequestMapping("/{token}")
    public String home(@PathVariable String token, @RequestParam(required = false) String redirectUri,
                       @RequestParam(required = false) String tag) throws IOException, SigningException {
        // Initiate conversation.
        Conversation conversation = Conversation.newInstance();
        conversation.setRedirectUri(redirectUri);

        // Fetch document metadata, store token in conversation.
        Document document = documentRepository.findByToken(token);
        conversation.setDocumentToken(document.getToken());

        // Fetch user identifier, store in conversation.
        String pid = httpServletRequest.getHeader("X-DifiProxy-pid");
        conversation.setPid(pid);

        // Set tag if available.
        conversation.setTag(tag);

        // Initiate signing.
        String signingRedirectUri = signingService.initiateSigning(conversation, document, pid);

        // Save conversation for later.
        conversationRepository.save(conversation);

        // Redirect user to signing service.
        return String.format("redirect:%s", signingRedirectUri);
    }
}

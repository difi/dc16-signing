package no.difi.signing.controller;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.api.SigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SigningService signingService;
    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/{token}")
    public String home(@PathVariable String token) throws IOException {
        String conversationId = UUID.randomUUID().toString();
        Document document = documentRepository.findByToken(token);
        String pid = httpServletRequest.getHeader("X-DifiProxy-pid");

        String redirectUri = signingService.initiateSigning(conversationId, document, pid);
        return String.format("redirect:%s", redirectUri);
    }
}

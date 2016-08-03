package no.difi.signing.controller;

import no.difi.signing.api.SigningService;
import no.difi.signing.model.Conversation;
import no.difi.signing.repository.ConversationRepository;
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

    @RequestMapping("/completion")
    @ResponseBody
    public String onCompletion(@RequestParam("conversation") String conversationId, @RequestParam("status_query_token") String queryToken) {
        Conversation conversation = conversationRepository.findByIdentifier(conversationId);

        signingService.fetchSignedResources(conversation, queryToken);

        return String.format("QueryToken: %s", queryToken);
    }

    @RequestMapping("/rejection")
    public void onRejection(@RequestParam("conversation") String conversationId) {

    }

    @RequestMapping("/error")
    public void onError(@RequestParam("conversation") String conversationId) {

    }

}

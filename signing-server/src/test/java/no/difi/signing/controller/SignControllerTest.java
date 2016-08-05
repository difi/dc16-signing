package no.difi.signing.controller;

import no.difi.signing.TestApplication;
import no.difi.signing.lang.SigningException;
import no.difi.signing.mock.DigipostServerMock;
import no.difi.signing.model.Conversation;
import no.difi.signing.repository.ConversationRepository;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

@SpringBootTest(classes = TestApplication.class)
public class SignControllerTest extends AbstractTestNGSpringContextTests {

    private DigipostServerMock digipostServerMock;

    @BeforeClass
    public void beforeClass() throws Exception {
        digipostServerMock = new DigipostServerMock();
        digipostServerMock.start();
    }

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    public void simple() throws IOException, SigningException {
        SignController signController = new SignController();

        Conversation conversation = Conversation.newInstance();
        conversation.setId(0);
        conversation.setPid("12345678901234");
        conversation.setRedirectUri("http://...");
        conversation.setDocumentToken("document1");
        conversation.setDigipostSignatureJobId(1337);
        conversation.setDigipostRedirectUrl("http://digipost");
        conversation.setDigipostStatusUrl("http://status");
        conversation.setTag("test");
        conversationRepository.save(conversation);

        //String s = signController.home(conversation.getDocumentToken(),conversation.getRedirectUri(), conversation.getTag());

    }


}

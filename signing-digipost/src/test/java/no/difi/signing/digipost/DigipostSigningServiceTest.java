package no.difi.signing.digipost;

import no.difi.signing.TestApplication;
import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.mock.DigipostServerMock;
import no.difi.signing.model.Conversation;
import no.difi.signing.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest(classes = TestApplication.class)
public class DigipostSigningServiceTest extends AbstractTestNGSpringContextTests{

    private DigipostServerMock digipostServerMock;

    @Autowired
    private DigipostSigningService digipostSigningService;
    @Autowired
    private DocumentRepository documentRepository;

    @BeforeClass
    public void beforeClass() throws Exception {
        digipostServerMock = new DigipostServerMock();
        digipostServerMock.start();
    }

    @AfterClass
    public void afterClass() {
        digipostServerMock.shutDown();
    }

    @Autowired
    private DigipostSigningService signingService;

    @Test
    public void simple() {
        System.out.println(signingService);
    }

    @Test
    public void inititatSigning_returns_redirectUrl() throws IOException {
        Conversation conversation = Conversation.newInstance();
        Document document = documentRepository.findByToken("document1");
        String pid = "17079493538";

        String rederictUrl = digipostSigningService.initiateSigning(conversation, document, pid);

        Assert.assertEquals(rederictUrl, "http://localhost:8082/mockSigning.html");
    }

    @Test
    public void fetchSignedResources_initiates_jobResponse(){

    }

}

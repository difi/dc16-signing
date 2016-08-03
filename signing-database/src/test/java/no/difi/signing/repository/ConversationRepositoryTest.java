package no.difi.signing.repository;

import no.difi.signing.TestApplication;
import no.difi.signing.model.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestApplication.class)
public class ConversationRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    public void simple() {
        Conversation conversation = Conversation.newInstance();
        conversation.setId(0);
        conversation.setPid("12345678901234");
        conversation.setRedirectUri("http://...");
        conversation.setDocumentToken("document1");
        conversation.setDigipostSignatureJobId(1337);
        conversation.setDigipostRedirectUrl("http://digipost");
        conversation.setDigipostStatusUrl("http://status");

        conversationRepository.save(conversation);

        Conversation freshConversation = conversationRepository.findByIdentifier(conversation.getIdentifier());

        Assert.assertNotNull(freshConversation);

        Assert.assertTrue(freshConversation.getId() != 0);
        Assert.assertNotNull(freshConversation.getIdentifier());
        Assert.assertEquals(freshConversation.getPid(), "12345678901234");
        Assert.assertEquals(freshConversation.getRedirectUri(), "http://...");
        Assert.assertEquals(freshConversation.getDocumentToken(), "document1");
        Assert.assertEquals(freshConversation.getDigipostSignatureJobId(), 1337);
        Assert.assertEquals(freshConversation.getDigipostRedirectUrl(), "http://digipost");
        Assert.assertEquals(freshConversation.getDigipostStatusUrl(), "http://status");
        Assert.assertNotNull(freshConversation.getTimestamp());
    }
}

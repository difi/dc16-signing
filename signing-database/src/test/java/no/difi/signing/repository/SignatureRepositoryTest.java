package no.difi.signing.repository;

import no.difi.signing.TestApplication;
import no.difi.signing.model.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

@SpringBootTest(classes = TestApplication.class)
public class SignatureRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SignatureRepository signatureRepository;

    @Test
    public void simple() {
        Signature signature = Signature.newInstance();
        signature.setId(0);
        signature.setPid("12345678901234");
        signature.setDocumentTitle("Document 1");
        signature.setDocumentToken("document1");
        signature.setDocumentVersion("1");
        signature.setTimestamp(new Date());

        signatureRepository.save(signature);

        Signature freshSignature = signatureRepository.findByIdentifier(signature.getIdentifier());

        Assert.assertNotNull(freshSignature);

        Assert.assertTrue(freshSignature.getId() != 0);
        Assert.assertEquals(freshSignature.getPid(), "12345678901234");
        Assert.assertEquals(freshSignature.getDocumentTitle(), "Document 1");
        Assert.assertEquals(freshSignature.getDocumentToken(), "document1");
        Assert.assertEquals(freshSignature.getDocumentVersion(), "1");
        Assert.assertNotNull(freshSignature.getTimestamp());
    }

}

package no.difi.signing.docs;

import no.difi.signing.TestApplication;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.lang.SigningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@SpringBootTest(classes = TestApplication.class)
public class DirectoryDocumentRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void simple() throws SigningException {
        Assert.assertNotNull(documentRepository);

        Assert.assertEquals(documentRepository.allDocuments().size(), 1);
        Assert.assertNotNull(documentRepository.findByToken("document1"));
    }

    @Test(expectedExceptions = SigningException.class)
    public void documentNotFound() throws SigningException {
        documentRepository.findByToken("not-found");
    }
}

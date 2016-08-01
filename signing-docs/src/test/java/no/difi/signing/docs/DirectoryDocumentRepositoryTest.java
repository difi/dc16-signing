package no.difi.signing.docs;

import no.difi.signing.TestApplication;
import no.difi.signing.api.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestApplication.class)
public class DirectoryDocumentRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DocumentRepository documentRepository;

    @Test(groups = "ide")
    public void simple() {
        Assert.assertNotNull(documentRepository);

        Assert.assertEquals(documentRepository.allDocuments().size(), 1);
        Assert.assertNotNull(documentRepository.findByToken("document1"));
    }
}

package no.difi.signing.docs;

import no.difi.signing.api.DocumentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class DirectoryDocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void simple() {
        Assert.assertNotNull(documentRepository);

        Assert.assertEquals(1, documentRepository.allDocuments().size());
        Assert.assertNotNull(documentRepository.findByToken("document1"));
    }
}

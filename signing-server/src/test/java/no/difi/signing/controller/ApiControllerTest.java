package no.difi.signing.controller;

import no.difi.signing.TestApplication;
import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.repository.SignatureRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;

import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ApiControllerTest {

    @MockBean
    private DocumentRepository documentRepository;
    @MockBean
    private SignatureRepository signatureRepository;

    @Autowired
    private ApiController apiController;

    @Test
    public void testListDocuments() throws Exception {
        Document document = Mockito.mock(Document.class);
        Mockito.doReturn("My document").when(document).toString();

        Mockito.doReturn(Collections.singletonList(document)).when(documentRepository).allDocuments();

        List<String> documents = apiController.listDocs();
        Assert.assertEquals(documents.size(), 1);
        Assert.assertEquals(documents.get(0), "My document");
    }
}

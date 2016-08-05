package no.difi.signing.controller;

import no.difi.signing.TestApplication;
import no.difi.signing.api.ConversationStub;
import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.api.SigningService;
import no.difi.signing.repository.ConversationRepository;
import no.difi.signing.repository.SignatureRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest
@ContextConfiguration(classes = TestApplication.class)
public class SignControllerTest {

    @MockBean
    private SignatureRepository signatureRepository;
    @MockBean
    private ConversationRepository conversationRepository;
    @MockBean
    private SigningService signingService;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void simple() throws Exception {
        Mockito.when(signingService.initiateSigning(Mockito.any(ConversationStub.class), Mockito.any(Document.class), Mockito.eq("12345678901234")))
                .thenReturn("http://somewhere.to.service/");

        mvc.perform(MockMvcRequestBuilders.get("/sign/document1").header("X-DifiProxy-pid", "12345678901234"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://somewhere.to.service/"));
    }
}

package no.difi.signing.controller;

import no.difi.signing.TestApplication;
import no.difi.signing.model.Signature;
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

import java.util.Collections;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest
@ContextConfiguration(classes = TestApplication.class)
public class OverviewControllerTest {

    @MockBean
    private SignatureRepository signatureRepository;
    @MockBean
    private ConversationRepository conversationRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void printAllSignatures() throws Exception {
        Signature signature = Signature.newInstance();
        signature.setDocumentTitle("Document 1");
        signature.setDocumentToken("document1");
        signature.setDocumentVersion("1");
        signature.setTimestamp(new Date());

        Mockito.when(signatureRepository.findByPid("12345678901234"))
                .thenReturn(Collections.singletonList(signature));

        mvc.perform(MockMvcRequestBuilders.get("/overview").header("X-DifiProxy-pid", "12345678901234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("overview"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("signatures"));
    }
}

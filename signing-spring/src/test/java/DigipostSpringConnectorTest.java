import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by camp-nto on 14.07.2016.
 */


public class DigipostSpringConnectorTest {

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();;
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getHello() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello")))
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"));
        Assert.assertNotNull(digipostSpringConnector.test());
        Assert.assertEquals(digipostSpringConnector.test(), digipostSpringConnector.test());

    }

    @Test
    public void testCoverageTest(){
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        Assert.assertNotNull(digipostSpringConnector);
    }

    @Test
    public void testMakeAsice_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/asice"))
                .andExpect(status().isFound());
        Assert.assertNotNull(digipostSpringConnector.makeAsice());
    }

    @Test
    public void testWhenSigningComplete_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/onCompletion"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void whenUserRejects_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/onRejection"))
                .andExpect(status().isBadRequest());
        Assert.assertNotNull(mvc);

    }

    @Test
    public void testwhenSigningFails_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/onError"))
                .andExpect(status().isBadRequest());
        Assert.assertNotNull(mvc);
    }

    @Test
    public void getSignedDocument_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getDocument"))
                .andExpect(status().isBadRequest());
        Assert.assertNotNull(mvc);
        //Assert.assertEquals(digipostSpringConnector.getSignedDocument("xades"), "failed2");
    }

    @Test
    public void getPades_returns_unable_to_Fetch_Pade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getPades"))
                .andExpect(status().isOk());
        Assert.assertEquals(digipostSpringConnector.getPades(), "Unable to fetch Pade");
    }

    @Test
    public void getPades_returns_signed_document_fetcher() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        SignedDocumentFetcher signedDocumentFetcher = Mockito.mock(SignedDocumentFetcher.class);

        mvc.perform(MockMvcRequestBuilders.get("/getPades"))
                .andExpect(status().isOk());
        digipostSpringConnector.setSignedDocumentFetcher(signedDocumentFetcher);
        Assert.assertEquals(digipostSpringConnector.getPades(), null);
    }

    @Test
    public void getXades_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getXades"))
                .andExpect(status().isOk());
        Assert.assertEquals(digipostSpringConnector.getXades(), "Unable to fetch Xade");
    }

    @Test
    public void getJobStatus_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getJobStatus"))
                .andExpect(status().isOk());
        Assert.assertEquals(digipostSpringConnector.getJobStatus(), null);
    }



}

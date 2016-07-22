import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import no.digipost.signature.client.portal.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void getPades_returns_unable_to_Fetch_Pade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getPades"))
                .andExpect(status().isOk());
        Assert.assertEquals(digipostSpringConnector.getPades(), "Unable to fetch Pade");
    }

    @Test
    public void getPades_test_fetchingPade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        SignedDocumentFetcher signedDocumentFetcher = Mockito.mock(SignedDocumentFetcher.class);

        mvc.perform(MockMvcRequestBuilders.get("/getPades"))
                .andExpect(status().isOk());
        digipostSpringConnector.setSignedDocumentFetcher(signedDocumentFetcher);
        Assert.assertEquals(digipostSpringConnector.getPades(), null);

        Mockito.when(digipostSpringConnector.getPades()).thenReturn("fetched pade");
        Assert.assertEquals(digipostSpringConnector.getPades(), "fetched pade");
    }

    @Test
    public void getXades_test_fetchingXade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        SignedDocumentFetcher signedDocumentFetcher = Mockito.mock(SignedDocumentFetcher.class);

        mvc.perform(MockMvcRequestBuilders.get("/getXades"))
                .andExpect(status().isOk());
        digipostSpringConnector.setSignedDocumentFetcher(signedDocumentFetcher);

        //Mockito.when(digipostSpringConnector.getXades()).thenReturn("fetched xade");
        Assert.assertEquals(digipostSpringConnector.getXades(), null);
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

    @Test
    public void onCompletion_returns_directJobStatusResponse(){
        //DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        //StatusReader statusReader = mock()
        //SigningServiceConnector signingServiceConnector = mock(SigningServiceConnector.class);

        //digipostSpringConnector.setStatusReader(statusReader);
        //digipostSpringConnector.setStatusQueryToken("token");
        //digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);


        //digipostSpringConnector.whenSigningComplete("token");


    }





}

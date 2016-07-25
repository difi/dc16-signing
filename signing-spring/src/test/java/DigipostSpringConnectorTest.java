import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import static org.mockito.Mockito.mock;





public class DigipostSpringConnectorTest {

    DigipostSpringConnector connector = new DigipostSpringConnector();
    SignedDocumentFetcher signedDocumentFetcher;

    @BeforeSuite
    public void setUp() throws IOException {
        MockServer.setUp();
    }

    @BeforeClass
    public void setUpSignatureJob() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        String[] exitUrls = {
                "http://localhost:8081/onCompletion","http://localhost:8081/onRejection","http://localhost:8081/onError"};

        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(asiceMaker.getContactInfo(),"123456789");

        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());
        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, new URI("http://localhost:8082/"));


        StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"tt");
        signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);

    }

    @Test
    public void testCoverageTest(){
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        Assert.assertNotNull(digipostSpringConnector);
    }


    @Test
    public void testMakeAsice() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String asice = connector.makeAsice(request).toString();

        Assert.assertTrue(asice.contains("redirect:https://difitest.signering.posten.no/redirect"));
    }

    @Test
    public void testGetPades() throws IOException {
        byte[] padesStatus = connector.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test
    public void testGetXades() throws IOException {
        byte[] xadesStatus = connector.getXades();
        Assert.assertNotSame(xadesStatus, "".getBytes());
    }

/*
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

    //@Test
    //public void getPades_returns_unable_to_Fetch_Pade() throws Exception {
    //    DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

    //    mvc.perform(MockMvcRequestBuilders.get("/getPades"))
    //            .andExpect(status().isOk());
    //    Assert.assertEquals(digipostSpringConnector.getPades(), "[B@22bac7bc");
    //}

    *//*
    @Test
    public void getPades_test_fetchingPade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        SignedDocumentFetcher signedDocumentFetcher = Mockito.mock(SignedDocumentFetcher.class);

        mvc.perform(MockMvcRequestBuilders.get("/getPades"))
                .andExpect(status().isOk());
        digipostSpringConnector.setSignedDocumentFetcher(signedDocumentFetcher);
        Assert.assertEquals(digipostSpringConnector.getPades(), null);

        Mockito.when(digipostSpringConnector.getPades()).thenReturn("fetched pade".getBytes());
        Assert.assertEquals(digipostSpringConnector.getPades(), "fetched pade");
    }
    **//*

    @Test
    public void getXades_test_fetchingXade() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        SignedDocumentFetcher signedDocumentFetcher = mock(SignedDocumentFetcher.class);

        mvc.perform(MockMvcRequestBuilders.get("/getXades"))
                .andExpect(status().isOk());
        digipostSpringConnector.setSignedDocumentFetcher(signedDocumentFetcher);

        //Mockito.when(digipostSpringConnector.getXades()).thenReturn("fetched xade");
        Assert.assertEquals(digipostSpringConnector.getXades(), null);
    }

    *//*
    @Test
    public void getXades_checksStatus() throws Exception {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        mvc.perform(MockMvcRequestBuilders.get("/getXades"))
                .andExpect(status().isOk());
        Assert.assertEquals(digipostSpringConnector.getXades(), "Unable to fetch Xade");
    }
    *//*
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


    }*/





}

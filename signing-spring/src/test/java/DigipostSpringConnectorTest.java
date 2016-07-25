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

    @BeforeSuite
    public void setUp() throws IOException {
        MockServer.setUp();
    }

    @BeforeClass
    public void setUpSignatureJob() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {


    }

    @Test
    public void testCoverageTest(){
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        Assert.assertNotNull(digipostSpringConnector);
    }


    @Test
    public void testMakeAsice() throws Exception {
        DigipostSpringConnector connector = new DigipostSpringConnector();

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
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);

        HttpServletRequest request = mock(HttpServletRequest.class);
        String asice = connector.makeAsice(request).toString();

        Assert.assertTrue(asice.contains("redirect:https://difitest.signering.posten.no/redirect"));
    }

    @Test
    public void testGetPadesIfSignedDocumentFetcherNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        DigipostSpringConnector connector = new DigipostSpringConnector();

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
        SignedDocumentFetcher signedDocumentFetcher1 = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);


        byte[] padesStatus = connector.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test
    public void testGetPadesIfSigningServiceConnectorNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        DigipostSpringConnector connector = new DigipostSpringConnector();

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
        //SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);

        byte[] padesStatus = connector.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test
    public void testGetXades() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        DigipostSpringConnector connector = new DigipostSpringConnector();

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
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);

        byte[] xadesStatus = connector.getXades();
        Assert.assertNotSame(xadesStatus, "".getBytes());
    }




}

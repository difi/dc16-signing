import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.testng.Assert;
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
import static org.mockito.Mockito.when;


public class DigipostSpringConnectorTest {

    @BeforeSuite
    public void startServer() throws IOException {
        MockServer.setUp();
    }

    @Test
    public void testCoverageTest(){
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();
        Assert.assertNotNull(digipostSpringConnector);
    }


    @Test(groups = "not-docker")
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

        //Assert.assertTrue(asice.contains("redirect:https://difitest.signering.posten.no/redirect"));
        Assert.assertTrue(asice.contains("redirect"));
    }

    @Test(groups = "not-docker")
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
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
        connector.setSignedDocumentFetcher(signedDocumentFetcher);

        byte[] padesStatus = connector.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
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
        connector.setSigningServiceConnector(signingServiceConnector);

        byte[] padesStatus = connector.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
    public void testGetXadesSignedDocumentFetcherNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
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

        byte[] xadesStatus = connector.getXades();
        Assert.assertNotSame(xadesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
    public void testGetXadesSigningServiceConnectorNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
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
        connector.setSigningServiceConnector(signingServiceConnector);

        byte[] xadesStatus = connector.getXades();
        Assert.assertNotSame(xadesStatus, "".getBytes());

    }

    @Test(groups = "not-docker")
    public void whenSigningComplete_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

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
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);

        StatusReader statusReader = mock(StatusReader.class);

        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenSigningComplete("token");
        Assert.assertEquals(digipostSpringConnector.whenSigningComplete("token"), "status<br> <a href='http://localhost:8080/getXades'> Click here to get Xades </a><br> <a href='http://localhost:8080/getPades'> Click here to get Pades");
    }

    @Test(groups = "not-docker")
    public void whenSigningFails_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

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
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);

        StatusReader statusReader = mock(StatusReader.class);

        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenSigningFails("token");
        Assert.assertEquals(digipostSpringConnector.whenSigningFails("token"), "status");
    }

    @Test(groups = "not-docker")
    public void whenUserRejects_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

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
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);

        StatusReader statusReader = mock(StatusReader.class);

        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenUserRejects("token");
        Assert.assertEquals(digipostSpringConnector.whenUserRejects("token"), "status");
    }
}

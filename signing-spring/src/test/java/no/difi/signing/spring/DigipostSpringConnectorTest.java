package no.difi.signing.spring;

import no.difi.signing.digipost.*;
import no.difi.signing.mockserver.MockServer;
import no.difi.signing.spring.DigipostSpringConnector;
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

    private DigipostSpringConnector digipostSpringConnector;
    private AsiceMaker asiceMaker;
    private String[] exitUrls;
    private SetupClientConfig setupClientConfig;
    private DocumentBundle preparedAsice;
    private SignatureJob signatureJob;
    private KeyStoreConfig keyStoreConfig;
    private SigningServiceConnector signingServiceConnector;
    private StatusReader statusReader;
    private SignedDocumentFetcher signedDocumentFetcher;

    @BeforeSuite
    public void setup() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        MockServer.setUp();

        String[] exitUrls = {"http://localhost:8081/onCompletion","http://localhost:8081/onRejection","http://localhost:8081/onError"};
        this.exitUrls = exitUrls;
        this.asiceMaker = new AsiceMaker();
        this.setupClientConfig = new SetupClientConfig("Direct");
        this.setupClientConfig.initialize(asiceMaker.getContactInfo(),"123456789");
        this.preparedAsice = asiceMaker.createAsice("17079493538","123456789",exitUrls, setupClientConfig.getClientConfiguration());
        this.signatureJob = asiceMaker.getSignatureJob();
        this.keyStoreConfig = setupClientConfig.getKeyStoreConfig();
        this.signingServiceConnector = new SigningServiceConnector();
        this.signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, new URI("http://localhost:8082/"));
        this.statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"tt");
        this.signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);

    }

    @Test
    public void testCoverageTest(){
        DigipostSpringConnector digipostSpringConnector = new DigipostSpringConnector();

        Assert.assertNotNull(digipostSpringConnector);
    }


    @Test(groups = "not-docker")
    public void testMakeAsice() throws Exception {
        this.digipostSpringConnector = new DigipostSpringConnector();
        HttpServletRequest request = mock(HttpServletRequest.class);
        String asice = digipostSpringConnector.makeAsice(request).toString();

        Assert.assertTrue(asice.contains("redirect"));
    }

    @Test(groups = "not-docker")
    public void testGetPadesIfSignedDocumentFetcherNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        this.digipostSpringConnector = new DigipostSpringConnector();
        this.digipostSpringConnector.setSignedDocumentFetcher(this.signedDocumentFetcher);
        byte[] padesStatus = this.digipostSpringConnector.getPades();

        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
    public void testGetPadesIfSigningServiceConnectorNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        this.digipostSpringConnector = new DigipostSpringConnector();
        this.digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);
        byte[] padesStatus = digipostSpringConnector.getPades();

        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
    public void testGetXadesSignedDocumentFetcherNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        this.digipostSpringConnector = new DigipostSpringConnector();
        byte[] xadesStatus = this.digipostSpringConnector.getXades();

        Assert.assertNotSame(xadesStatus, "".getBytes());
    }

    @Test(groups = "not-docker")
    public void testGetXadesSigningServiceConnectorNotNull() throws IOException, URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        this.digipostSpringConnector = new DigipostSpringConnector();
        this.digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);
        byte[] xadesStatus = this.digipostSpringConnector.getXades();

        Assert.assertNotSame(xadesStatus, "".getBytes());

    }

    @Test(groups = "not-docker")
    public void whenSigningComplete_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);
        StatusReader statusReader = mock(StatusReader.class);
        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenSigningComplete("token");

        Assert.assertEquals(digipostSpringConnector.whenSigningComplete("token"), "status<br> <a href='http://localhost:8080/getXades'> Click here to get Xades </a><br> <a href='http://localhost:8080/getPades'> Click here to get Pades");
    }

    @Test(groups = "not-docker")
    public void whenSigningFails_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);
        StatusReader statusReader = mock(StatusReader.class);
        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenSigningFails("token");

        Assert.assertEquals(digipostSpringConnector.whenSigningFails("token"), "status");
    }

    @Test(groups = "not-docker")
    public void whenUserRejects_return_status() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        digipostSpringConnector.setSigningServiceConnector(signingServiceConnector);
        StatusReader statusReader = mock(StatusReader.class);
        digipostSpringConnector.setStatusReader(statusReader);
        when(statusReader.getStatus()).thenReturn("status");
        digipostSpringConnector.whenUserRejects("token");

        Assert.assertEquals(digipostSpringConnector.whenUserRejects("token"), "status");
    }
}

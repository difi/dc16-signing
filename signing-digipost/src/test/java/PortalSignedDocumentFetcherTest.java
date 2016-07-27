import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalSigner;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PortalSignedDocumentFetcherTest {
    private PortalSignedDocumentFetcher signedDocumentFetcher;
    private PortalSignedDocumentFetcher failedSignedDocumentFetcher;
    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    @BeforeSuite
    public void setupServer() throws IOException{
        MockServer.setUp();
    }
    @BeforeClass
    public void setUp() throws IOException, URISyntaxException {
        setUpWithCorrectXadesAndPades();
        setUpWithFailedXadesAndPades();

    }

    public void setUpWithCorrectXadesAndPades() throws IOException, URISyntaxException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso2@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso2@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso2@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());


        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);

        SigningServiceConnector connector = new SigningServiceConnector();
        connector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), new URI("http://localhost:8082/"));
        poller.poll();

        this.signedDocumentFetcher = new PortalSignedDocumentFetcher(poller, portalClient);

    }

    public void setUpWithFailedXadesAndPades() throws URISyntaxException, IOException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("11111111111",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("22222222222",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());

        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);

        SigningServiceConnector connector = new SigningServiceConnector();
        connector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), new URI("http://localhost:8082/"));

        this.failedSignedDocumentFetcher = new PortalSignedDocumentFetcher(poller, portalClient);
    }

    @Test
    public void getPadesTest() throws IOException {
        byte[] padesStatus = signedDocumentFetcher.getPades();
        Assert.assertNotEquals(padesStatus, "".getBytes() );
    }

    @Test
    public void getXadesTest() throws IOException {
        String xadesStatus = signedDocumentFetcher.getXades();
        Assert.assertEquals(xadesStatus, "got xAdES files");
    }

    @Test
    public void getFailedPadesTest() throws IOException {
        byte[] padesStatus = failedSignedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus, "".getBytes() );
    }

    @Test
    public void getFailedXadesTest() throws IOException {
        String xadesStatus = failedSignedDocumentFetcher.getXades();
        Assert.assertEquals(xadesStatus, "no xades available");
    }

    @AfterTest
    public void stopServer(){
        MockServer.shutDown();
    }

}

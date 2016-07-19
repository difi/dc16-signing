import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PortalSignedDocumentFetcherTest {
    private PortalSignedDocumentFetcher signedDocumentFetcher;
    private PortalSignedDocumentFetcher failedSignedDocumentFetcher;
    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    @BeforeClass
    public void setUp() throws IOException, URISyntaxException {
        setUpWithCorrectXadesAndPades();
        setUpWithFailedXadesAndPades();

    }

    public void setUpWithCorrectXadesAndPades() throws IOException, URISyntaxException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("123456789");

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, exitUrls , clientConfig.getClientConfiguration());


        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);

        SigningServiceConnector connector = new SigningServiceConnector();
        connector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig());
        poller.poll();

        this.signedDocumentFetcher = new PortalSignedDocumentFetcher(poller, portalClient);


    }

    public void setUpWithFailedXadesAndPades() throws URISyntaxException, IOException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("7");

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("11111111111",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("22222222222",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, exitUrls , clientConfig.getClientConfiguration());

        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);

        SigningServiceConnector connector = new SigningServiceConnector();
        connector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig());
        //poller.poll();

        this.failedSignedDocumentFetcher = new PortalSignedDocumentFetcher(poller, portalClient);

    }

    @Test
    public void getPadesTest() throws IOException {
        String padesStatus = signedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus, "pades retrieved" );
    }

    @Test
    public void getXadesTest() throws IOException {
        String xadesStatus = signedDocumentFetcher.getXades();
        Assert.assertEquals(xadesStatus, "got xAdES files");
    }

    @Test
    public void getFailedPadesTest() throws IOException {
        String padesStatus = failedSignedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus, "pades not ready or failed" );
    }

    @Test
    public void getFailedXadesTest() throws IOException {
        String xadesStatus = failedSignedDocumentFetcher.getXades();
        Assert.assertEquals(xadesStatus, "no xades available");
    }


}

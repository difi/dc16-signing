import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalSigner;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PortalSignedDocumentFetcherTest {
    private PortalSignedDocumentFetcher signedDocumentFetcher;
    private PortalSignedDocumentFetcher failedSignedDocumentFetcher;
    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    @BeforeClass
    public void setUp() throws IOException {
        setUpIsPadesReady();

    }

    public void setUpIsPadesReady() throws IOException {
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

        this.signedDocumentFetcher = new PortalSignedDocumentFetcher(poller, portalClient);


    }

    @Test
    public void getPadesTest() throws IOException {
        String padesStatus = signedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus, "pades retrieved" );
    }

}

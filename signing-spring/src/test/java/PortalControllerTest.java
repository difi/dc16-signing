import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalSigner;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PortalControllerTest {

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();
    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    @Test
    public void getPortalXades_returns_getXades() throws IOException {
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);

        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);;
        Assert.assertEquals(portalController.getPortalXades(), null);
    }

    @Test
    public void getPortalXades_returnes_getXades_if_signedDocumentFetcher_is_null() throws IOException, URISyntaxException {
        PortalController portalController = new PortalController();
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

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), new URI("http://localhost:8082/"));

        portalController.setSigningServiceConnector(signingServiceConnector);

        portalController.getPortalXades();
        Assert.assertEquals(portalController.getPortalXades(), "no xades available");

    }

    @Test
    public void getPortalPades_returns_getPades() throws IOException{
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);
        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);

        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);
        Assert.assertEquals(portalController.getPortalPades(), null);
    }

    @Test
    public void getPortalPades_returns_getPades_if_signedDocumentFetcher_is_null() throws URISyntaxException, IOException {
        PortalController portalController = new PortalController();
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());

        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), new URI("http://localhost:8082/"));

        portalController.setSigningServiceConnector(signingServiceConnector);

        portalController.getPortalPades();
        Assert.assertEquals(portalController.getPortalXades(), "no xades available");
    }

    @Test
    public void getPortalPades_returnes_getPades_if_signedDocumentFetcher_is_null() throws IOException {
        PortalController portalController = new PortalController();

        PortalJobPoller portalJobPoller = Mockito.mock(PortalJobPoller.class);
        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setPortalJobPoller(portalJobPoller);
        portalController.setSigningServiceConnector(signingServiceConnector);
        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);
        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);

        Assert.assertEquals(portalController.getPortalPades(), null);
    }

    @Test
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_not_null() throws IOException, URISyntaxException {
        PortalController portalController = new PortalController();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        portalController.setSigningServiceConnector(signingServiceConnector);
        portalController.startPortalJob();


    }

    @Test
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_is_null() throws IOException, URISyntaxException {
        PortalController portalController = new PortalController();
        portalController.startPortalJob();


    }

    @Test
    public void poll_returns_status() throws URISyntaxException, IOException {
        PortalController portalController = new PortalController();
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

        SigningServiceConnector signingServiceConnectorconnector = new SigningServiceConnector();
        signingServiceConnectorconnector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), new URI("http://localhost:8082/"));

        portalController.setPortalJobPoller(poller);
        portalController.setSigningServiceConnector(signingServiceConnectorconnector);

        portalController.poll();
        Assert.assertEquals(portalController.poll(), "IN_PROGRESS");
    }

}

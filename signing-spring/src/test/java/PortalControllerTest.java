import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalSigner;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PortalControllerTest {

    private TypesafeServerConfig serverConfig;
    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;
    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();
    private String[] exitUrls;
    private String relativeDocumentPath;
    private String keystorefile;
    private PortalJobPoller poller;

    @BeforeClass
    public void setUp() throws URISyntaxException, IOException {
        MockServer.setUp();
        Config configFile = ConfigFactory.load("signing");
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.relativeDocumentPath = documentConfig.getRelativeDocumentPath();

        this.keystoreConfigProvider = new  TypesafeKeystoreConfigProvider(configFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        this.keystorefile = keystoreConfig.getKeystore();

        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        exitUrls = new String[]{serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};

        PortalController portalController = new PortalController();
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");
        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        List<String> configSigners = documentConfig.getSigners();
        portalSigners.add( PortalSigner.builder(configSigners.get(0), Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder(configSigners.get(1),Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder(configSigners.get(2),Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsice = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());

        PortalClient portalClient = new PortalClient(clientConfig.getClientConfiguration());
        PortalJobPoller poller = new PortalJobPoller(portalClient);
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), serverConfig.getServiceUri());
        portalController.setSigningServiceConnector(signingServiceConnector);

        getPortalXades_returnes_getXades_if_signedDocumentFetcher_is_null(portalController);
        getPortalPades_returns_getPades_if_signedDocumentFetcher_is_null(portalController);
        poll_returns_status(portalController, portalAsiceMaker, clientConfig);
    }

    @Test
    public void getPortalXades_returns_getXades() throws IOException {
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);

        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);
        Assert.assertEquals(portalController.getPortalXades(), null);
    }

    @Test(groups = "not-docker")
    public void getPortalXades_returnes_getXades_if_signedDocumentFetcher_is_null(PortalController portalController) throws IOException, URISyntaxException {
        portalController.getPortalXades();
        Assert.assertEquals(portalController.getPortalXades(), "no xades available");

    }

    @Test(groups = "not-docker")
    public void getPortalPades_returns_getPades_if_signedDocumentFetcher_is_null(PortalController portalController) throws URISyntaxException, IOException {
        portalController.getPortalPades();
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


    @Test(groups = "not-docker")
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

    @Test(groups = "not-docker")
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_not_null() throws IOException, URISyntaxException {
        PortalController portalController = new PortalController();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        portalController.setSigningServiceConnector(signingServiceConnector);
        portalController.startPortalJob();


    }

    @Test(groups = "not-docker")
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_is_null() throws IOException, URISyntaxException {
        PortalController portalController = new PortalController();
        portalController.startPortalJob();
    }

    @Test(groups = "not-docker")
    public void poll_returns_status(PortalController portalController, PortalAsiceMaker portalAsiceMaker, SetupClientConfig clientConfig) throws URISyntaxException, IOException {
        SigningServiceConnector signingServiceConnectorconnector = new SigningServiceConnector();
        signingServiceConnectorconnector.sendPortalRequest(portalAsiceMaker.getPortalJob(), clientConfig.getKeyStoreConfig(), serverConfig.getServiceUri());

        portalController.setPortalJobPoller(poller);
        portalController.setSigningServiceConnector(signingServiceConnectorconnector);

        portalController.poll();
        Assert.assertEquals(portalController.poll(), "IN_PROGRESS");
    }

}

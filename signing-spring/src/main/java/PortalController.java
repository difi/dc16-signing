import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@RestController
public class PortalController {

    private PortalSignedDocumentFetcher portalSignedDocumentFetcher;
    private PortalJobPoller portalJobPoller;
    private SigningServiceConnector signingServiceConnector;

    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;

    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;

    private String[] exitUrls;

    @Autowired
    private void setupConfig()throws URISyntaxException{
        Config configFile = ConfigFactory.load("signing");
        documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.serverConfig = serverConfigProvider.getByName("default");
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        exitUrls = new String[] {serverConfig.getCompletionUri().toString(),serverConfig.getRejectionUri().toString(),serverConfig.getErrorUri().toString()};

    }
    @RequestMapping("/portalXades")
    public String getPortalXades() throws IOException {

        if (portalSignedDocumentFetcher != null) {
            return portalSignedDocumentFetcher.getXades();
        } else {
            this.portalSignedDocumentFetcher = new PortalSignedDocumentFetcher(portalJobPoller,signingServiceConnector.getPortalClient().get());
            return portalSignedDocumentFetcher.getXades();
        }

    }

    @RequestMapping("/portalPades")
    public byte[] getPortalPades() throws IOException {
        if (portalSignedDocumentFetcher != null) {
            return portalSignedDocumentFetcher.getPades();
        } else {

            this.portalSignedDocumentFetcher = new PortalSignedDocumentFetcher(portalJobPoller,signingServiceConnector.getPortalClient().get());
            return portalSignedDocumentFetcher.getPades();
        }
    }

    @RequestMapping("/portal")
    public void startPortalJob() throws IOException, URISyntaxException {
        setupConfig();
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();
        List<PortalSigner> portalSigners = new ArrayList<>();
        for(String signer : documentConfig.getSigners()){
            portalSigners.add(PortalSigner.builder(signer, Notifications.builder().withEmailTo(documentConfig.getEmail()).build()).build());
        }

        portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());

        PortalJob portalJob = portalAsiceMaker.getPortalJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        if (this.signingServiceConnector != null) {
            signingServiceConnector.sendPortalRequest(portalJob, keyStoreConfig);
        } else {
            signingServiceConnector = new SigningServiceConnector();
            signingServiceConnector.sendPortalRequest(portalJob, keyStoreConfig);
        }
    }

    @RequestMapping("/poll")

    public String poll(){
        if(this.portalJobPoller == null){
            this.portalJobPoller = new PortalJobPoller(signingServiceConnector.getPortalClient().get()); //added extra line, before without "if" //Lage sjekk
        }
        String status = this.portalJobPoller.poll();
        return status;
    }

    public void setPortalSignedDocumentFetcher(PortalSignedDocumentFetcher portalSignedDocumentFetcher){
        this.portalSignedDocumentFetcher = portalSignedDocumentFetcher;
    }

    public void setPortalJobPoller(PortalJobPoller portalJobPoller){
        this.portalJobPoller = portalJobPoller;
    }

    public void setSigningServiceConnector(SigningServiceConnector signingServiceConnector){
        this.signingServiceConnector = signingServiceConnector;
    }
}

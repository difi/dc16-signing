import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
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
    private PortalJobPoller poller;
    private SigningServiceConnector signingServiceConnector;

    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    @RequestMapping("/portalXades")
    public String getPortalXades() throws IOException {
        if(portalSignedDocumentFetcher != null){
            return portalSignedDocumentFetcher.getXades();
        } else {
            this.portalSignedDocumentFetcher = new PortalSignedDocumentFetcher(poller,signingServiceConnector.getPortalClient());
            return portalSignedDocumentFetcher.getXades();
        }

    }

    @RequestMapping ("/portalPades")
    public String getPortalPades() throws IOException{
        if(portalSignedDocumentFetcher != null){
            return portalSignedDocumentFetcher.getPades();
        } else {
            this.portalSignedDocumentFetcher = new PortalSignedDocumentFetcher(poller,signingServiceConnector.getPortalClient());
            return portalSignedDocumentFetcher.getPades();
        }
    }
    @RequestMapping("/portal")
    public void startPortalJob() throws IOException, URISyntaxException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("991825827");
        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());

        portalAsiceMaker.createPortalAsice(portalSigners,exitUrls,clientConfig.getClientConfiguration());

        PortalJob portalJob = portalAsiceMaker.getPortalJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        if(this.signingServiceConnector != null){
            signingServiceConnector.sendPortalRequest(portalJob,keyStoreConfig);
        } else {
            signingServiceConnector = new SigningServiceConnector();
            signingServiceConnector.sendPortalRequest(portalJob,keyStoreConfig);

        }

    }

    @RequestMapping("/poll")
    public String poll(){
        this.poller = new PortalJobPoller(signingServiceConnector.getPortalClient());
        String status = poller.poll();
        return status;
    }
}

import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;
import no.digipost.signature.client.security.KeyStoreConfig;
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

    private String[] exitUrls = {
            "http://localhost:8081/onCompletion", "http://localhost:8081/onRejection", "http://localhost:8081/onError"
    };

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
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Portal");

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("991825827");
        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add(PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add(PortalSigner.builder("17079493457", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add(PortalSigner.builder("17079493295", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());

        portalAsiceMaker.createPortalAsice(portalSigners, exitUrls, clientConfig.getClientConfiguration());

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
        if(this.portalJobPoller != null){
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

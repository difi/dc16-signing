import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.portal.Notifications;
import no.digipost.signature.client.portal.PortalSigner;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class PortalAsiceMakerTest {

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    @Test
    public void defaultSignableDocumentNotNull(){
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        File file = portalAsiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void findsFileAtGivenPath(){
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        File file = portalAsiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void signatureJobExistsAfterCreatingAsic() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        Config configFile = ConfigFactory.load();
        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        String[] exitUrls = {serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");

        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(portalAsiceMaker.getContactInfo(), documentConfig.getSender());

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("documentConfig.getSigner()", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsic = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());
        Assert.assertNotNull(portalAsiceMaker.getPortalJob());
    }
}

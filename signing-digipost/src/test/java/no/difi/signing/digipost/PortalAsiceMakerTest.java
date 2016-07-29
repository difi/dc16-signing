package no.difi.signing.digipost;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.config.TypesafeDocumentConfig;
import no.difi.signing.config.TypesafeDocumentConfigProvider;
import no.difi.signing.digipost.PortalAsiceMaker;
import no.difi.signing.digipost.SetupClientConfig;
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
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");

        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(portalAsiceMaker.getContactInfo(), documentConfig.getSender());

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        List<PortalSigner> portalSigners = new ArrayList<>();
        List<String> configSigners = documentConfig.getSigners();
        portalSigners.add( PortalSigner.builder(configSigners.get(0), Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder(configSigners.get(1),Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder(configSigners.get(2),Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsic = portalAsiceMaker.createPortalAsice(portalSigners, clientConfig.getClientConfiguration());
        Assert.assertNotNull(portalAsiceMaker.getPortalJob());
    }
}

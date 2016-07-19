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


    @Test
    public void defaultSignableDocumentNotNull(){
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        File file = portalAsiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void findsFileAtGivenPath(){
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker("Documents//Dokument til signering 5.pdf");
        File file = portalAsiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void signatureJobExistsAfterCreatingAsic() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        PortalAsiceMaker portalAsiceMaker = new PortalAsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(portalAsiceMaker.getContactInfo(),"123456789");

        clientConfig.setupKeystoreConfig(portalAsiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("123456789");
        String[] exitUrls = {
                "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
        };
        List<PortalSigner> portalSigners = new ArrayList<>();
        portalSigners.add( PortalSigner.builder("17079493538", Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493457",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        portalSigners.add( PortalSigner.builder("17079493295",Notifications.builder().withEmailTo("eulverso@gmail.com").build()).build());
        DocumentBundle preparedAsic = portalAsiceMaker.createPortalAsice(portalSigners,exitUrls,clientConfig.getClientConfiguration());
        Assert.assertNotNull(portalAsiceMaker.getPortalJob());
        //Assert.assertNotNull(preparedAsic);
    }
}

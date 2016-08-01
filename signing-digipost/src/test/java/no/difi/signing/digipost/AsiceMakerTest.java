package no.difi.signing.digipost;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.config.TypesafeDocumentConfig;
import no.difi.signing.config.TypesafeDocumentConfigProvider;
import no.difi.signing.config.TypesafeServerConfig;
import no.difi.signing.config.TypesafeServerConfigProvider;
import no.digipost.signature.client.asice.DocumentBundle;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class AsiceMakerTest {

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    @Test
    public void defaultSignableDocumentNotNull() throws URISyntaxException, IOException {
        AsiceMaker asiceMaker = new AsiceMaker("document1");
        InputStream file = asiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void findsFileAtGivenPath() throws URISyntaxException, IOException {
        AsiceMaker asiceMaker = new AsiceMaker("document1");
        InputStream file = asiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    /**
     * Checks that both the documentbundle and the signature job exist after calling createAsice.
     */
    @Test
    public void signatureJobExistsAfterCreatingAsic() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        Config configFile = ConfigFactory.load();
        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        String[] exitUrls = {serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};

        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        AsiceMaker asiceMaker = new AsiceMaker("document1");
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(asiceMaker.getContactInfo(), documentConfig.getSender());

        DocumentBundle preparedAsic = asiceMaker.createAsice(documentConfig.getSigner(), documentConfig.getSender(),exitUrls, clientConfig.getClientConfiguration());
        Assert.assertNotNull(asiceMaker.getSignatureJob());
        Assert.assertNotNull(preparedAsic);
    }



}

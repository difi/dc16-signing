package no.difi.signing.digipost;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.config.TypesafeDocumentConfig;
import no.difi.signing.config.TypesafeDocumentConfigProvider;
import no.difi.signing.config.TypesafeServerConfig;
import no.difi.signing.config.TypesafeServerConfigProvider;
import no.difi.signing.digipost.AsiceDumper;
import no.difi.signing.digipost.AsiceMaker;
import no.difi.signing.digipost.SetupClientConfig;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class AsiceDumperTest {

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;

    @Test
    public void testThatDocumentbundleIsDumpedToDisk() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        Config configFile = ConfigFactory.load();
        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        String[] exitUrls = {serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};

        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        TypesafeDocumentConfigProvider documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        TypesafeDocumentConfig documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        DocumentBundle preparedAsic = asiceMaker.createAsice(documentConfig.getSigner(), documentConfig.getSender(), exitUrls, clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();

        Assert.assertEquals(AsiceDumper.dumper(preparedAsic,signatureJob),true);

    }

}

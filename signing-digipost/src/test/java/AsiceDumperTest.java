import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class AsiceDumperTest {

@Test
    public void testThatDocumentbundleIsDumpedToDisk() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
    String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };

    AsiceMaker asiceMaker = new AsiceMaker();
    SetupClientConfig clientConfig = new SetupClientConfig("Direct");
    clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
    clientConfig.setupClientConfiguration("123456789");

    DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());

    SignatureJob signatureJob = asiceMaker.getSignatureJob();


    Assert.assertEquals(AsiceDumper.dumper(preparedAsic,signatureJob),true);

    }

}

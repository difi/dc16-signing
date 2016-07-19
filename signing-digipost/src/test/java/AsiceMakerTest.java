import no.digipost.signature.client.asice.DocumentBundle;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class AsiceMakerTest {

    @Test
    public void defaultSignableDocumentNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        File file = asiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    @Test
    public void findsFileAtGivenPath(){
        AsiceMaker asiceMaker = new AsiceMaker("Documents//Dokument til signering 5.pdf");
        File file = asiceMaker.getDokumentTilSignering();
        Assert.assertNotNull(file);
    }

    /**
     * Checks that both the documentbundle and the signature job exist after calling createAsice.
     */
    @Test
    public void signatureJobExistsAfterCreatingAsic() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(asiceMaker.getContactInfo(),"123456789");
        String[] exitUrls = {
                "http://localhost:8081/onCompletion","http://localhost:8081/onRejection","http://localhost:8081/onError"
        };
        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());
        Assert.assertNotNull(asiceMaker.getSignatureJob());
        Assert.assertNotNull(preparedAsic);
    }



}

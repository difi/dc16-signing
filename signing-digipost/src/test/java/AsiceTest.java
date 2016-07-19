import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceTest {
    /**
     * Main method for testing without using spring boot.
     * @param args
     */
    public static void main(String[] args) throws KeyStoreException, java.security.cert.CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException, URISyntaxException {

        //TODO: Read in test configuration here instead of having constants.

        String[] exitUrls = {
                "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
        };
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("123456789");

        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig);

        //StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient(), signingServiceConnector.getDirectJobResponse(),  )
    }
}

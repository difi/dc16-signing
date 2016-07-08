import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceTest {
    /**
     * Main method for testing without using spring boot.
     * @param args
     */
    public static void main(String[] args) throws KeyStoreException, java.security.cert.CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        //TODO: Read in test configuration here instead of having constants.

        String[] exitUrls = {
                "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
        };

        SetupClientConfig clientConfig = new SetupClientConfig();
        clientConfig.setupClientConfiguration("123456789");

        File kontaktInfo = new File("kontaktinfo-client-test.jks");

        clientConfig.setupKeystoreConfig(kontaktInfo);

        AsiceMaker asiceMaker = new AsiceMaker();

        DocumentBundle preparedAsic = asiceMaker.createAsice("1707949358","123456789",exitUrls, clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig);

        //Ta tak i redirect-url, sett det i Spring
    }
}

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



        GenerateAsice generateAsice = new GenerateAsice();
        generateAsice.setupKeystoreConfig();
        generateAsice.createAsice("1707949358",exitUrls,);
        SignatureJob signatureJob = generateAsice.getSignatureJob();
        KeyStoreConfig keyStoreConfig = generateAsice.getKeyStoreConfig();
        SendHTTPRequest sendHTTPRequest = new SendHTTPRequest();
        sendHTTPRequest.sendRequest(signatureJob, keyStoreConfig);

        //Ta tak i redirect-url, sett det i Spring
    }
}

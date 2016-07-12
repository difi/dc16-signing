
import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class SigningServiceConnectorTest {

    @Test
    public void sendRequestReturnsTrue() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {

        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig();

        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("123456789");

        String[] exitUrls = {
                "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
        };

        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        boolean sendRequest = signingServiceConnector.sendRequest(signatureJob, keyStoreConfig);
        Assert.assertEquals(sendRequest, true);
    }



}

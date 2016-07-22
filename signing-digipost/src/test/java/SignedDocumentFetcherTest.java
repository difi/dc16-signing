import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.security.KeyStoreConfig;
import no.motif.IO;
import org.mockito.Mock;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public class SignedDocumentFetcherTest {

    private SignedDocumentFetcher signedDocumentFetcher;
    private SignedDocumentFetcher failedSignedDocumentFetcher;

    @Mock
    StatusReader statusReader;

    @BeforeClass
    public void setUp() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        //Force using localhost as server somehow?
        MockServer.setUp();
        //setUpDocumentFetcherAbleToRetrieve();
        //setUpDocumentFetcherUnableToRetrieve();

    }
    /*
    public void setUpDocumentFetcherUnableToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        String[] exitUrls = {
                "http://localhost:8082/onCompletion","http://localhost:8082/onRejection","http://localhost:8082/onError"
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
        StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"???");
        //statusReader.getStatus();
        this.failedSignedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
    }

    public void setUpDocumentFetcherAbleToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        String[] exitUrls = {
                "http://localhost:8082/onCompletion","http://localhost:8082/onRejection","http://localhost:8082/onError"
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
        //StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"Completion_token");
        StatusReader statusReader = mock(StatusReader.class);
        when(statusReader.getStatus()).thenReturn("SIGNED");

        //statusReader.getStatus();
        this.signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
    }

    @Test
    public void getPadesReturnedFetchedPade() throws IOException{

        byte[] padesStatus = signedDocumentFetcher.getPades();
        Assert.assertNotSame(padesStatus, "".getBytes());
    }

    @Test
    public void getPadesReturnedFailed() throws IOException{
        byte[] padesStatus = failedSignedDocumentFetcher.getPades();
        Assert.assertNotEquals(padesStatus,"".getBytes());
    }


}
*/
}

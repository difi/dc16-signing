import com.google.common.io.ByteStreams;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Optional;

public class SignedDocumentFetcherTest {

    private SignedDocumentFetcher signedDocumentFetcher;
    private SignedDocumentFetcher failedSignedDocumentFetcher;

    @Mock
    StatusReader statusReader;

    @BeforeClass
    public void setUp() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {

        Optional<SignedDocumentFetcher> optSignedDocumentFetcher = setUpDocumentFetcherAbleToRetrieve();
        if(optSignedDocumentFetcher.isPresent()){
            this.signedDocumentFetcher = optSignedDocumentFetcher.get();
        }

        Optional<SignedDocumentFetcher> optFailedSignedDocumentFetcher = setUpDocumentFetcherUnableToRetrieve();
        if(optFailedSignedDocumentFetcher.isPresent()){
            this.failedSignedDocumentFetcher = optFailedSignedDocumentFetcher.get();
        }
    }

    public Optional<SignedDocumentFetcher> setUpDocumentFetcherUnableToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        String[] exitUrls = {
                "http://localhost:8081/onCompletion","http://localhost:8081/onRejection","http://localhost:8081/onError"
        };
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();
        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());
        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, new URI("http://localhost:8082/"));

        if(signingServiceConnector.getDirectClient().isPresent()){
            StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"???");
            failedSignedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
            return Optional.ofNullable(failedSignedDocumentFetcher);
        } else {
                return Optional.empty();
        }
    }

    public Optional<SignedDocumentFetcher> setUpDocumentFetcherAbleToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        String[] exitUrls = {
                "http://localhost:8081/onCompletion","http://localhost:8081/onRejection","http://localhost:8081/onError"
        };
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(asiceMaker.getContactInfo(),"123456789");

        DocumentBundle preparedAsic = asiceMaker.createAsice("17079493538","123456789",exitUrls, clientConfig.getClientConfiguration());
        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, new URI("http://localhost:8082/"));


        if(signingServiceConnector.getDirectClient().isPresent() && signingServiceConnector.getDirectJobResponse().isPresent()){
            StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"tt");

            signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
            return Optional.ofNullable(signedDocumentFetcher);
        } else {
            return Optional.empty();
        }
    }
    @Test
    public void getPadesReturnedFetchedPade() throws IOException{
        byte[] padesStatus = signedDocumentFetcher.getPades();
        byte[] comparisonStatus = ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/pAdES.pdf"));
        Assert.assertNotSame(padesStatus, "".getBytes());
    }
    @Test
    public void getPadesReturnedFailed() throws IOException{
        byte[] padesStatus = failedSignedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus,"".getBytes());
    }

    @AfterTest
    public void stopServer(){
        MockServer.shutDown();
    }
}


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
    @BeforeClass
    public void setUp() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        //Force using localhost as server somehow?

        setUpDocumentFetcherAbleToRetrieve();
        setUpDocumentFetcherUnableToRetrieve();

    }

    public void setUpDocumentFetcherUnableToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
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
        StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient(),signingServiceConnector.getDirectJobResponse(),"???");
        this.failedSignedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient(),statusReader);
    }

    public void setUpDocumentFetcherAbleToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
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
        StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient(),signingServiceConnector.getDirectJobResponse(),"Completion_token");
        this.signedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient(),statusReader);
    }

    @Test
    public void getPadesReturnedFetchedPade() throws IOException{

        String padesStatus = signedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus, "fetched pade");
    }

    @Test
    public void getPadesReturnedFailed() throws IOException{
        String padesStatus = failedSignedDocumentFetcher.getPades();
        Assert.assertEquals(padesStatus,"failed");
    }

    @Test
    public void signedDocumentFetcherInitializedProperly(){
        Assert.assertNotNull(signedDocumentFetcher);
    }
    //Still not working
    //@Test
    //public void getPadesReturnesFetchedPade() throws IOException {
    //    SetupClientConfig clientConfig = new SetupClientConfig();
    //    clientConfig.setupClientConfiguration("123456789");


    //    DirectClient client = new DirectClient(clientConfig.getClientConfiguration());
    //    AsiceMaker asiceMaker = new AsiceMaker();
    //    SignatureJob signatureJob = asiceMaker.getSignatureJob();

    //    String statusQueryToken = "0A3BQ54C";
    //    DirectJobResponse directJobResponse = client.create((DirectJob)signatureJob);
    //    DirectJobStatusResponse directJobStatusResponse = client.getStatus(StatusReference.of(directJobResponse).withStatusQueryToken(statusQueryToken));

    //    StatusReader statusReader = new StatusReader(client, directJobResponse, statusQueryToken);
    //    SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(client, statusReader);
    //    Assert.assertEquals(signedDocumentFetcher.getPades(), "fetched pade");

    //}
}

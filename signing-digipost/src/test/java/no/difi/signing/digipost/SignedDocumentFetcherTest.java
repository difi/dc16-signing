package no.difi.signing.digipost;

import com.google.common.io.ByteStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.config.TypesafeDocumentConfig;
import no.difi.signing.config.TypesafeDocumentConfigProvider;
import no.difi.signing.config.TypesafeServerConfig;
import no.difi.signing.config.TypesafeServerConfigProvider;
import no.difi.signing.digipost.*;
import no.difi.signing.mockserver.MockServer;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Optional;

public class SignedDocumentFetcherTest {

    private SignedDocumentFetcher signedDocumentFetcher;
    private SignedDocumentFetcher failedSignedDocumentFetcher;

    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;
    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;
    String[] exitUrls;

    @Mock
    StatusReader statusReader;

    @BeforeClass
    public void setUp() throws URISyntaxException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
        Config configFile = ConfigFactory.load();
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");

        Optional<SignedDocumentFetcher> optSignedDocumentFetcher = setUpDocumentFetcherAbleToRetrieve();
        if(optSignedDocumentFetcher.isPresent()){
            this.signedDocumentFetcher = optSignedDocumentFetcher.get();
        }

        Optional<SignedDocumentFetcher> optFailedSignedDocumentFetcher = setUpDocumentFetcherUnableToRetrieve();
        if(optFailedSignedDocumentFetcher.isPresent()){
            this.failedSignedDocumentFetcher = optFailedSignedDocumentFetcher.get();
        }


        exitUrls = new String[]{serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};
        System.out.print("LOOOOOOOOOOOOOOOOOOOOOOL" +documentConfig.getSender());
        System.out.print(documentConfig.getSigner());
        System.out.print(exitUrls);
    }

    public Optional<SignedDocumentFetcher> setUpDocumentFetcherUnableToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration();

        DocumentBundle preparedAsic = asiceMaker.createAsice(documentConfig.getSigner(),documentConfig.getSender(),exitUrls, clientConfig.getClientConfiguration());
        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();
        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, serverConfig.getServiceUri());

        if(signingServiceConnector.getDirectClient().isPresent()){
            StatusReader statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(),signingServiceConnector.getDirectJobResponse().get(),"???");
            failedSignedDocumentFetcher = new SignedDocumentFetcher(signingServiceConnector.getDirectClient().get(),statusReader);
            return Optional.ofNullable(failedSignedDocumentFetcher);
        } else {
                return Optional.empty();
        }
    }

    public Optional<SignedDocumentFetcher> setUpDocumentFetcherAbleToRetrieve() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException, URISyntaxException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");
        clientConfig.initialize(asiceMaker.getContactInfo(),"123456789");

        DocumentBundle preparedAsic = asiceMaker.createAsice(documentConfig.getSigner(),documentConfig.getSender(),exitUrls, clientConfig.getClientConfiguration());
        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        SigningServiceConnector signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig, serverConfig.getServiceUri());


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
        byte[] comparisonStatus = ByteStreams.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream("__files/pAdES.pdf"));
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


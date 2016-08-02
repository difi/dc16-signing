package no.difi.signing.digipost;

import com.google.common.io.ByteStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.api.Document;
import no.difi.signing.config.*;
import no.difi.signing.docs.DirectoryDocument;
import no.difi.signing.docs.DirectoryDocumentRepository;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.CreateASiCE;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectDocument;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectSigner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceMaker {

    private TypesafeServerConfig serverConfig;
    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;
    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private CreateASiCE createASiCE;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private SignatureJob signatureJob;
    private InputStream dokumentTilSignering;
    private File kontaktInfoClientTest;
    private String relativeDocumentPath;
    private String keystorefile;
    private String[] exitUrls;
    private Path path;

    /**
     * Creates classLoader to load file, Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks and sets the document for signing.
     * TODO: Set through a config file?
     * @param documentToken
     */

    public AsiceMaker(String documentToken) throws URISyntaxException, IOException {
        //DocumentRepository directoryDocumentRepository = new DirectoryDocumentRepository();
        path = Paths.get("C:\\Users\\camp-mlo\\Documents\\GitHub\\dc16-signing\\signing-test\\src\\main\\resources\\docs\\" + documentToken);
        DirectoryDocument document = new DirectoryDocument(path);


        Config configFile = ConfigFactory.load("signing");
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        //this.relativeDocumentPath = documentConfig.getRelativeDocumentPath();

        this.keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        this.keystorefile = keystoreConfig.getKeystore();

        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        exitUrls = new String[]{serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};

        ClassLoader classLoader = getClass().getClassLoader();
        kontaktInfoClientTest = new File(classLoader.getResource(keystorefile).getFile());

        dokumentTilSignering = document.getInputStream();

    }
    public AsiceMaker() throws URISyntaxException, IOException {
        //for testing

        DirectoryDocumentRepository directoryDocumentRepository = new DirectoryDocumentRepository();
        Document document = directoryDocumentRepository.findByToken("document1");

        Config configFile = ConfigFactory.load("signing");
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
       // this.relativeDocumentPath = documentConfig.getRelativeDocumentPath();

        this.keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        this.keystorefile = keystoreConfig.getKeystore();

        this.serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.serverConfig = serverConfigProvider.getByName("default");
        exitUrls = new String[]{serverConfig.getCompletionUri().toString(), serverConfig.getRejectionUri().toString(), serverConfig.getErrorUri().toString()};

        ClassLoader classLoader = getClass().getClassLoader();
        kontaktInfoClientTest = new File(classLoader.getResource(keystorefile).getFile());
        dokumentTilSignering = document.getInputStream();
    }
    /**
     * Creates an asice package. Uses current keystore and a hardcoded document.
     *
     */
    public DocumentBundle createAsice(String signerId, String ss, String[] exitUrls, ClientConfiguration clientConfiguration) throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, IOException, java.security.cert.CertificateException {
        createASiCE = new CreateASiCE(manifestCreator, clientConfiguration);
        DirectDocument document = DirectDocument.builder("Subject", "document.pdf", ByteStreams.toByteArray(dokumentTilSignering)).build();
        DirectSigner signer = createDirectSigner(signerId);
        this.signatureJob = createSignatureJob(signer, document, this.exitUrls);

        return createASiCE.createASiCE(signatureJob);
    }

    public DirectSigner createDirectSigner(String signerId) {
        return DirectSigner.builder(signerId).build();
    }

    public SignatureJob createSignatureJob(DirectSigner signer, DirectDocument document, String[] exitUrls) {
        return new DirectJob.Builder(signer, document, exitUrls[0], exitUrls[1], exitUrls[2]).build();
    }

    public File getContactInfo() {
        return kontaktInfoClientTest;
    }

    public SignatureJob getSignatureJob() {
        return this.signatureJob;
    }

    public InputStream getDokumentTilSignering() {
        return dokumentTilSignering;
    }

}

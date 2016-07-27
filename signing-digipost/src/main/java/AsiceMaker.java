import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceMaker {

    private TypesafeKeystoreConfig keystoreConfig;
    private TypesafeKeystoreConfigProvider keystoreConfigProvider;

    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private CreateASiCE createASiCE;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private SignatureJob signatureJob;
    private File dokumentTilSignering;
    private File kontaktInfoClientTest;
    private String relativeDocumentPath;
    private String keystorefile;

    /**
     * Creates classLoader to load file, Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks and sets the document for signing.
     * TODO: Set through a config file?
     */

    public AsiceMaker() {
        Config configFile = ConfigFactory.load();
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.relativeDocumentPath = documentConfig.getRelativeDocumentPath();

        this.keystoreConfigProvider = new  TypesafeKeystoreConfigProvider(configFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        this.keystorefile = keystoreConfig.getKeystore();

        ClassLoader classLoader = getClass().getClassLoader();
        kontaktInfoClientTest = new File(classLoader.getResource(keystorefile).getFile());
        dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

    }
    /**
     * Creates an asice package. Uses current keystore and a hardcoded document.
     *
     */
    public DocumentBundle createAsice(String signerId, String ss, String[] exitUrls, ClientConfiguration clientConfiguration) throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, IOException, java.security.cert.CertificateException {
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(dokumentTilSignering).toString();
        createASiCE = new CreateASiCE(manifestCreator, clientConfiguration);
        DirectSigner signer = createDirectSigner(signerId);
        DirectDocument document = DocumentHandler.pdfToDirectDocument(PDFPath);
        this.signatureJob = createSignatureJob(signer, document, exitUrls);

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

    public File getDokumentTilSignering() {
        return dokumentTilSignering;
    }

}

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.CreateASiCE;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.portal.PortalDocument;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PortalAsiceMaker {

    private TypesafeDocumentConfig documentConfig;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;
    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private CreateASiCE createASiCE;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private PortalJob portalJob;
    private File dokumentTilSignering;
    private File kontaktInfoClientTest;
    private String relativeDocumentPath;
    private String keystorefile;

    /**
     * Creates classLoader to load file, Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks and sets the document for signing.
     * TODO: Set through a config file?
     */
    public PortalAsiceMaker() {
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

    public PortalJob createSignatureJobPortal(List<PortalSigner> signers, PortalDocument document) {
        return PortalJob.builder(document, signers).build();
    }

    /**
     * Makes a portal document bundle. Instead of just one signer this function needs a list of signers.
     *  @param signers             List of signers.
     * @param clientConfiguration
     */
    public DocumentBundle createPortalAsice(List<PortalSigner> signers, ClientConfiguration clientConfiguration) throws IOException {
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(dokumentTilSignering).toString();
        createASiCE = new CreateASiCE(manifestCreator, clientConfiguration);
        PortalDocument document = DocumentHandler.pdfToPortalDocument(PDFPath);
        this.portalJob = createSignatureJobPortal(signers, document);
        return null;
    }

    public File getContactInfo() {
        return kontaktInfoClientTest;
    }

    public PortalJob getPortalJob() {
        return this.portalJob;
    }

    public File getDokumentTilSignering() {
        return dokumentTilSignering;
    }
}

import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.CreateASiCE;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.portal.PortalDocument;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalSigner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PortalAsiceMaker {
    private CreateASiCE createASiCE;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private SignatureJob signatureJob;
    private PortalJob portalJob;
    private File dokumentTilSignering;
    private File kontaktInfoClientTest;
    private String relativeDocumentPath = "Documents//Dokument til signering 3.pdf";



    public PortalAsiceMaker() {
        //Creates classLoader to load file
        ClassLoader classLoader = getClass().getClassLoader();
        //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
        kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile());
        //Sets the document for signing //TODO: Set through a config file?
        dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

    }

    public PortalAsiceMaker(String relativeDocumentPath) {
        //Creates classLoader to load file
        ClassLoader classLoader = getClass().getClassLoader();
        //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
        kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile());
        //Sets the document for signing //TODO: Set through a config file?
        dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

    }

    public PortalJob createSignatureJobPortal(List<PortalSigner> signers, PortalDocument document, String[] exitUrls) {
        System.out.println(PortalJob.builder(document,signers).build().toString());
        return PortalJob.builder(document,signers).build();
    }

    public DocumentBundle createPortalAsice(List<PortalSigner> signers, String[] exitUrls, ClientConfiguration clientConfiguration) throws IOException {
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(dokumentTilSignering).toString();
        createASiCE = new CreateASiCE(manifestCreator, clientConfiguration);
        PortalDocument document = DocumentHandler.pdfToPortalDocument(PDFPath);
        this.portalJob = createSignatureJobPortal(signers,document,exitUrls);

        return null;
        //return createASiCE.createASiCE(this.portalJob);

    }

    public File getContactInfo() {return kontaktInfoClientTest;}
    public PortalJob getPortalJob() { return this.portalJob;}
}

import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;

import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceMaker {

    private CreateASiCE createASiCE;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private SignatureJob signatureJob;
    private File dokumentTilSignering;
    private File kontaktInfoClientTest;
    private String relativeDocumentPath ="Documents//Dokument til signering 3.pdf";

    public AsiceMaker(){
        //Creates classLoader to load file
        ClassLoader classLoader = getClass().getClassLoader();
        //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
        kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile());
        //Sets the document for signing //TODO: Set through a config file?
        dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

    }

    public AsiceMaker(String relativeDocumentPath){
        //Creates classLoader to load file
        ClassLoader classLoader = getClass().getClassLoader();
        //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
        kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile());
        //Sets the document for signing //TODO: Set through a config file?
        dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

    }

    /**
     * Creates an asice package. Uses current keystore and a hardcoded document.
     * @return
     */
    public DocumentBundle createAsice(String signerId, String sender, String[] exitUrls, ClientConfiguration clientConfiguration) throws KeyStoreException, NoSuchAlgorithmException,NoSuchProviderException, IOException,java.security.cert.CertificateException {
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(dokumentTilSignering).toString();
        //Initializes an asic creator with the configuration and the standard manifestCreator.
        createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);

        DirectSigner signer = createDirectSigner(signerId);
        DirectDocument document = DocumentHandler.pdfToDocument(PDFPath);
        signatureJob = createSignatureJob(signer,document,exitUrls);
        return createASiCE.createASiCE(signatureJob);
    }

    public DirectSigner createDirectSigner(String signerId){
        return DirectSigner.builder(signerId).build();
    }

    public SignatureJob createSignatureJob(DirectSigner signer, DirectDocument document, String[] exitUrls){
        return new DirectJob.Builder(signer,document,exitUrls[0],exitUrls[1],exitUrls[2]).build();
    }


    public SignatureJob getSignatureJob(){
        return this.signatureJob;
    }


}

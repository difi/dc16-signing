import com.google.common.io.ByteStreams;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.core.Document;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AsiceMaker {

    private CreateASiCE createASiCE;
    private ClientConfiguration clientConfiguration;
    private KeyStore keyStore;
    private KeyStoreConfig keyStoreConfig;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private File kontaktInfoClientTest;
    private SignatureJob signatureJob;
    private File dokumentTilSignering;
    private Document documentToBeSigned;
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
     *
     * @return StringBuilder-object which is used to find the path to "dokumentTilSignering"
     */
    public StringBuilder setAbsolutePathToPDF(File dokumentTilSignering){
        StringBuilder stringBuilder = new StringBuilder(dokumentTilSignering.getAbsolutePath());

        for(int i=0; i < stringBuilder.length(); i++){
            if(stringBuilder.charAt(i) == '%'){
                stringBuilder.replace(i, i+3, " ");
            }
        }
        return stringBuilder;
    }

    /**
     * Setups the keystore and keystoreconfig
     */
    public void setupKeystoreConfig(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load((new FileInputStream(kontaktInfoClientTest)),"changeit".toCharArray());
            keyStoreConfig = KeyStoreConfig.fromKeyStore(new FileInputStream(kontaktInfoClientTest)
                    ,keyStore.aliases().nextElement(),"changeit","changeit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a DirectDocument from a given pdf
     * @param pdfPath Path to pdf

     */
    public DirectDocument pdfToDocument(String pdfPath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(pdfPath)) {
            return DirectDocument.builder("Subject", "document.pdf", ByteStreams.toByteArray(inputStream)).build();
        }
    }

    /**
     * Creates an asice package. Uses current keystore and a hardcoded document.
     * @return
     */
    public DocumentBundle createAsice(String signerId, String sender, String[] exitUrls) throws KeyStoreException, NoSuchAlgorithmException,NoSuchProviderException, FileNotFoundException, IOException,java.security.cert.CertificateException {

        //Creates a client configuration
        clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .globalSender(new Sender(sender))
                .build();

        String PDFPath = this.setAbsolutePathToPDF(dokumentTilSignering).toString();

        //Initializes an asic creator with the configuration and the standard manifestCreator.
        createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);

        //
        DirectSigner signer = createDirectSigner(signerId);
        DirectDocument document = pdfToDocument(PDFPath);
        signatureJob = createSignatureJob(signer,document,exitUrls);
        DocumentBundle asice = createASiCE.createASiCE(signatureJob);
        return asice;
    }

    public DirectSigner createDirectSigner(String signerId){
        return DirectSigner.builder(signerId).build();
    }

    public SignatureJob createSignatureJob(DirectSigner signer, DirectDocument document, String[] exitUrls){
        return new DirectJob.Builder(signer,document,exitUrls[0],exitUrls[1],exitUrls[2]).build();
    }

    public KeyStoreConfig getKeyStoreConfig(){
        return this.keyStoreConfig;
    }

    public SignatureJob getSignatureJob(){
        return this.signatureJob;
    }


}

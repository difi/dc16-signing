import com.google.common.io.ByteStreams;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.*;

public class GenerateAsice {

    private CreateASiCE createASiCE;
    private ClientConfiguration clientConfiguration;
    private KeyStore keyStore;
    private KeyStoreConfig keyStoreConfig;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private File kontaktInfoClientTest;
    private SignatureJob signatureJob;
    private File dokumentTilSignering;

    public GenerateAsice(){
        ClassLoader classLoader = getClass().getClassLoader(); //Creates classLoader to load file
        this.kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile()); //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
        this.dokumentTilSignering = new File(classLoader.getResource("Documents//Dokument til signering 3.pdf").getFile());

    }

    /**
     *
     * @return StringBuilder-object which is used to find the path to "dokumentTilSignering"
     */
    public StringBuilder setAbsolutePathToPDF(){
        StringBuilder stringBuilder = new StringBuilder(this.dokumentTilSignering.getAbsolutePath());

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
            this.keyStore = KeyStore.getInstance("JKS");
            this.keyStore.load((new FileInputStream(this.kontaktInfoClientTest)),"changeit".toCharArray());
            this.keyStoreConfig = KeyStoreConfig.fromKeyStore(new FileInputStream(this.kontaktInfoClientTest)
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
    public DocumentBundle createAsice() throws KeyStoreException, NoSuchAlgorithmException,NoSuchProviderException, FileNotFoundException, IOException,java.security.cert.CertificateException {


        this.clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .globalSender(new Sender("123456789"))
                .build();

        String getPDFPath = this.setAbsolutePathToPDF().toString();
        this.createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);

        DirectSigner signer = DirectSigner.builder("12345678910").build();
        DirectDocument document = pdfToDocument(getPDFPath);
        this.signatureJob = new DirectJob.Builder(signer,document,"http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError").build();

        DocumentBundle asice = createASiCE.createASiCE(signatureJob);
       // dumper(asice,signatureJob);

        return asice;
    }
    public KeyStoreConfig getKeyStoreConfig(){
        return this.keyStoreConfig;
    }

    public SignatureJob getSignatureJob(){
        return this.signatureJob;
    }

    public DirectClient getDirectClient(){
        DirectClient directClient = new DirectClient(this.clientConfiguration);
        return directClient;
    }

    /**
     * Dumps a zip file of the document bundle to disk.
     * @param documentBundle
     * @param signatureJob
     * @throws IOException
     */
    public void dumper(DocumentBundle documentBundle, SignatureJob signatureJob) throws IOException {
        Path path = FileSystems.getDefault().getPath("","");
        DumpDocumentBundleToDisk dumper = new DumpDocumentBundleToDisk(path);
        InputStream asiceInputStream = documentBundle.getInputStream();
        dumper.process(signatureJob,asiceInputStream);

    }


    public static void main(String[] args) throws KeyStoreException, java.security.cert.CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        GenerateAsice generateAsice = new GenerateAsice();
        generateAsice.setupKeystoreConfig();

        generateAsice.createAsice();

        CreateRequest createRequest = new CreateRequest();
        SignatureJob signatureJob = generateAsice.getSignatureJob();
        KeyStoreConfig keyStoreConfig = generateAsice.getKeyStoreConfig();

        SendHTTPRequest sendHTTPRequest = new SendHTTPRequest();
        sendHTTPRequest.sendRequest(signatureJob, keyStoreConfig);

        //Ta tak i redirec-url, sett det i Spring
    }

}

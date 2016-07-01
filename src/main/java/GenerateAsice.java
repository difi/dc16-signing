import no.digipost.signature.api.xml.thirdparty.asice.ASiCManifest;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.archive.CreateZip;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.Manifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.asice.signature.*;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.*;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static no.motif.Strings.bytes;

public class GenerateAsice {


    private CreateASiCE createASiCE;
    private Manifest manifest;
    private Signature signature;
    private ClientConfiguration clientConfiguration;
    private KeyStore keyStore;
    private KeyStoreConfig keyStoreConfig;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    List<ASiCEAttachable> files = new ArrayList<>();

    GenerateAsice(){};

    public void setupKeystoreConfig(){
        try {
            this.keyStore = KeyStore.getInstance("JKS");
            this.keyStore.load(new FileInputStream("C:\\Users\\camp-eul\\Documents\\GitHub\\dc16-signing\\src\\main\\resources\\kontaktinfo-client-test.jks"),"changeit".toCharArray());
            this.keyStoreConfig = KeyStoreConfig.fromKeyStore(new FileInputStream("C:\\Users\\camp-eul\\Documents\\GitHub\\dc16-signing\\src\\main\\resources\\kontaktinfo-client-test.jks")
                    ,keyStore.aliases().nextElement(),"changeit","changeit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DirectDocument pdfToDocument(String pdfPath) {

        byte[] pdfBytes = new byte[(int)pdfPath.length()];
        try {
            new DataInputStream(new FileInputStream(pdfPath)).readFully(pdfBytes);
            DirectDocument document = DirectDocument.builder("Subject", "Dokument til signering.pdf", pdfBytes).build();
            return document;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public DocumentBundle createAsice() throws KeyStoreException, NoSuchAlgorithmException,NoSuchProviderException, FileNotFoundException, IOException,java.security.cert.CertificateException {


        this.clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .globalSender(new Sender("123456789"))
                .build();

        this.createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);

        DirectSigner signer = DirectSigner.builder("12345678910").build();
        DirectDocument document = pdfToDocument("C:\\Users\\camp-eul\\Documents\\GitHub\\dc16-signing\\src\\main\\resources\\Dokument til signering.pdf");
        SignatureJob signatureJob = new DirectJob.Builder(signer,document,"http://sender.org/onCompletion","http://sender.org/onRejection","http://sender.org/onError").build();

        DocumentBundle asice = createASiCE.createASiCE(signatureJob);
        dumper(asice,signatureJob);


        return asice;
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

    }





}

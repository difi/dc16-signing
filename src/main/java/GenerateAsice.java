import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import no.digipost.signature.api.xml.thirdparty.asice.ASiCManifest;
import no.digipost.signature.api.xml.thirdparty.xmldsig.Signature;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.archive.CreateZip;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.Manifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.asice.signature.CreateSignature;
import no.digipost.signature.client.core.Document;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.core.exceptions.CertificateException;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateAsice {


    private CreateASiCE createASiCE;
    private Manifest manifest;
    private Signature signature;
    private ClientConfiguration clientConfiguration;
    private KeyStore keyStore;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    private File kontaktInfoClientTest;

    public GenerateAsice(){
        ClassLoader classLoader = getClass().getClassLoader(); //Creates classLoader to load file
        this.kontaktInfoClientTest = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile()); //Sets field kontaktInfoClientTest to file kontaktinfo-client-test.jks
    }


    public void createAsice() throws KeyStoreException, NoSuchAlgorithmException,NoSuchProviderException, FileNotFoundException, IOException,java.security.cert.CertificateException {
        InputStream inputStream = new ByteArrayInputStream("example".getBytes());

        KeyStore KS = KeyStore.getInstance("JKS");
        KS.load(new FileInputStream(this.kontaktInfoClientTest),"changeit".toCharArray());
        System.out.println(KS.toString());

        String nextAlias = KS.aliases().nextElement();
        System.out.println(nextAlias);
        //for (KS.aliases().nextElement(); KS.aliases().hasMoreElements();)
        //    System.out.println(KS.aliases().nextElement());




        List<ASiCEAttachable> files = new ArrayList<>();
        CreateSignature signatureC = new CreateSignature();
        Manifest manifest = new Manifest("Example".getBytes());

        byte[] documentBytes = "Example".getBytes();
        this.clientConfiguration = ClientConfiguration.builder(KeyStoreConfig.fromKeyStore(new FileInputStream(this.kontaktInfoClientTest)
                ,nextAlias,"changeit","changeit"))
                .globalSender(new Sender("123456789"))
                .build();
        System.out.println(clientConfiguration.toString());

        this.createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);
        System.out.println(this.createASiCE.toString());
        DirectSigner signer = DirectSigner.builder("12345678910").build();

        DirectDocument document = DirectDocument.builder("Subject", "Dokument til signering.pdf", documentBytes).build();

        DirectClient directClient = new DirectClient(clientConfiguration);


        SignatureJob signatureJob = new DirectJob.Builder(signer,document,"http://sender.org/onCompletion","http://sender.org/onRejection","http://sender.org/onError").build();

        DirectJobResponse directJobResponse = directClient.create((DirectJob)signatureJob);
        System.out.println(directJobResponse.getStatusUrl());


        files.add(manifest);


        System.out.println(document.getFileName().toString());


        ExitUrls exitUrls = ExitUrls.of(
                "http://sender.org/onCompletion",
                "http://sender.org/onRejection",
                "http://sender.org/onError"
        );
        ASiCManifest asicManifest = new ASiCManifest();




        DirectJob directJob = DirectJob.builder(DirectSigner.builder("12345678910").build(), document, exitUrls).build();
        File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "asic-sample-default.zip");
        CreateZip createZip = new CreateZip();
        documentBytes = createZip.zipIt(files);


        CreateSignature createSignature = new CreateSignature();

        createSignature.createSignature(files,KeyStoreConfig.fromKeyStore(inputStream,"","",""));




    }

    public static void main(String[] args) throws KeyStoreException, java.security.cert.CertificateException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        GenerateAsice generateAsice = new GenerateAsice();
        generateAsice.createAsice();

    }





}

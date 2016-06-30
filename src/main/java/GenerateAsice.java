import no.digipost.signature.api.xml.thirdparty.asice.ASiCManifest;
import no.digipost.signature.api.xml.thirdparty.xmldsig.Signature;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.asice.*;
import no.digipost.signature.client.asice.manifest.CreateDirectManifest;
import no.digipost.signature.client.asice.manifest.Manifest;
import no.digipost.signature.client.asice.manifest.ManifestCreator;
import no.digipost.signature.client.asice.signature.CreateSignature;
import no.digipost.signature.client.core.Document;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectDocument;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectSigner;
import no.digipost.signature.client.direct.ExitUrls;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.List;

public class GenerateAsice {


    private CreateASiCE createASiCE;
    private Manifest manifest;
    private Signature signature;
    private ClientConfiguration clientConfiguration;
    private KeyStore keyStore;
    private ManifestCreator manifestCreator = new CreateDirectManifest();
    GenerateAsice(){};

    public void createAsice() throws KeyStoreException{
        InputStream inputStream = new ByteArrayInputStream("example".getBytes());
        this.keyStore = KeyStore.getInstance("JKS");
        List<ASiCEAttachable> files = new ArrayList<>();
        CreateSignature signatureC = new CreateSignature();
        Manifest manifest = new Manifest("Example".getBytes());


        this.clientConfiguration = ClientConfiguration.builder(KeyStore.)
                .globalSender(new Sender("123456789"))
                .build();

        this.createASiCE = new CreateASiCE(manifestCreator,clientConfiguration);

        System.out.println(this.createASiCE.toString());

        files.add(manifest);


        byte[] documentBytes = "Example".getBytes();
        DirectDocument document = DirectDocument.builder("Subject", "Dokument til signering.pdf", documentBytes).build();
        System.out.println(document.getFileName().toString());


        ExitUrls exitUrls = ExitUrls.of(
                "http://sender.org/onCompletion",
                "http://sender.org/onRejection",
                "http://sender.org/onError"
        );
        ASiCManifest asicManifest = new ASiCManifest();




        DirectJob directJob = DirectJob.builder(DirectSigner.builder("12345678910").build(), document, exitUrls).build();
        File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "asic-sample-default.zip");

        CreateSignature createSignature = new CreateSignature();


        createSignature.createSignature(files,KeyStoreConfig.fromKeyStore(inputStream,"","",""));




    }

    public static void main(String[] args) throws KeyStoreException {
        GenerateAsice generateAsice = new GenerateAsice();
        generateAsice.createAsice();

    }





}

package no.difi.signing;

import no.digipost.signature.client.asice.CreateASiCE;
import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.asice.manifest.Manifest;
import no.digipost.signature.client.asice.signature.Signature;
import no.digipost.signature.client.core.Document;
import no.digipost.signature.client.direct.DirectDocument;

public class GenerateAsice {
    private Signature signature;
    private Manifest manifest;
    public Document document;
    public DocumentBundle documentPackage;
    public DirectDocument doc;
    public ClientConfiguration client;

    public GenerateAsice (){
        manifest = new createManifest(job, sender);

        document = new CreateASiCE(manifest, client);

    }
}

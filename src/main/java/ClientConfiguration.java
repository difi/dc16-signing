/**
 * Created by camp-mlo on 30.06.2016.
 */

import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client;
import no.digipost.signature.client.asice;
import  no.digipost.signature.client.asice.archive;
import no.digipost.signature.client.asice.manifest;
import no.digipost.signature.client.asice.signature;
import no.digipost.signature.client.core:
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.exceptions;
import no.digipost.signature.client.core.internal;
import no.digipost.signature.client.core.internal.http;
import no.digipost.signature.client.core.internal.security;
import no.digipost.signature.client.core.internal.xml;
import no.digipost.signature.client.direct;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.portal;
import no.digipost.signature.client.security;
import no.digipost.signature.client.security.KeyStoreConfig;

public class ClientConfiguration {
    public void createClientConfig(){
        KeyStoreConfig keystoreConfig = KeyStoreConfig.fromKeyStore(keyStore,
                certificateAlias, keyStorePassword, privateKeyPassword);

        ClientConfiguration clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
            .globalSender(new Sender("123456789"))
            .build();
    }

    ClientConfiguration clientConfiguration = createClientConfig(); // As initialized earlier
    DirectClient client = new DirectClient(clientConfiguration);

    byte[] documentBytes = ...;
    DirectDocument document = DirectDocument.builder("Subject", "document.pdf", documentBytes).build();

    ExitUrls exitUrls = ExitUrls.of(
            "http://sender.org/onCompletion",
            "http://sender.org/onRejection",
            "http://sender.org/onError"
    );

    DirectJob directJob =
            DirectJob.builder(DirectSigner.builder("12345678910").build(), document, exitUrls).build();

    DirectJobResponse directJobResponse = client.create(directJob);

}

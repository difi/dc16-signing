import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client;
import no.digipost.signature.client.asice;
import  no.digipost.signature.client.asice.archive;
import no.digipost.signature.client.asice.manifest;
import no.digipost.signature.client.asice.signature;
import no.digipost.signature.client.core:
import no.digipost.signature.client.core.exceptions;
import no.digipost.signature.client.core.internal;
import no.digipost.signature.client.core.internal.http;
import no.digipost.signature.client.core.internal.security;
import no.digipost.signature.client.core.internal.xml;
import no.digipost.signature.client.direct;
import no.digipost.signature.client.portal;
import no.digipost.signature.client.security;


public class Test {
    KeyStoreConfig keystoreConfig = KeyStoreConfig.fromKeyStore(keyStore,
            certificateAlias, keyStorePassword, privateKeyPassword);

    ClientConfiguration clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
            .globalSender(new Sender("123456789"))
            .build();
}

package no.difi.signing;

import no.digipost.signature.client.asice.ASiCEConfiguration;
import no.digipost.signature.client.asice.DocumentBundleProcessor;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.security.KeyStoreConfig;
import no.motif.single.Optional;

public class ClientConfiguration implements ASiCEConfiguration {
    public void createClientConfig(){
        KeyStoreConfig keystoreConfig = KeyStoreConfig.fromKeyStore(keyStore,
                certificateAlias, keyStorePassword, privateKeyPassword);

        ClientConfiguration clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
            .globalSender(new Sender("123456789"))
            .build();
    }

    @java.lang.Override
    public KeyStoreConfig getKeyStoreConfig() {
        return null;
    }

    @java.lang.Override
    public Optional<Sender> getGlobalSender() {
        return null;
    }

    @java.lang.Override
    public java.lang.Iterable<DocumentBundleProcessor> getDocumentBundleProcessors() {
        return null;
    }
}

import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

public class SetupClientConfig {
    private KeyStore keyStore;
    private KeyStoreConfig keyStoreConfig;
    private ClientConfiguration clientConfiguration;
    private String type;


    SetupClientConfig(String type) {
        this.type = type;
    }

    /**
     * Setups the keystore and keystoreconfig
     */
    public void initialize(File kontaktinfo, String sender) throws URISyntaxException{
        setupKeystoreConfig(kontaktinfo);
        setupClientConfiguration(sender);
    }

    public void setupKeystoreConfig(File kontaktInfo) {
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load((new FileInputStream(kontaktInfo)), "changeit".toCharArray());
            keyStoreConfig = KeyStoreConfig.fromKeyStore(new FileInputStream(kontaktInfo)
                    , keyStore.aliases().nextElement(), "changeit", "changeit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setupClientConfiguration(String sender) throws URISyntaxException{
        //Creates a client configuration
        //clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
        //        .globalSender(new Sender(sender))
        //        .build();

        clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(new URI("http://localhost:8082/"))
                //.serviceUri(ServiceUri.DIFI_TEST)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public KeyStoreConfig getKeyStoreConfig() {
        return keyStoreConfig;
    }

    public void setKeyStoreConfig(KeyStoreConfig keyStoreConfig) {
        this.keyStoreConfig = keyStoreConfig;
    }

    public ClientConfiguration getClientConfiguration() {
        return this.clientConfiguration;
    }

    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }
}

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
        setupClientConfiguration();
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

    /**
     * Sets up the client configuration using a keystore and a truststore.
     */
    public void setupClientConfiguration() throws URISyntaxException{
        clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(new URI("http://localhost:8082/"))
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();
    }

    public KeyStoreConfig getKeyStoreConfig() {
        return keyStoreConfig;
    }
    public ClientConfiguration getClientConfiguration() {
        return this.clientConfiguration;
    }

}

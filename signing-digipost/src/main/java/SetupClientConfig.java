import com.sun.jndi.toolkit.url.Uri;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
    private TypesafeKeystoreConfig typeSafeKeystoreConfig;
    private TypesafeKeystoreConfigProvider typesafeKeystoreConfigProvider;
    private TypesafeServerConfig typesafeServerConfig;
    private TypesafeServerConfigProvider typesafeServerConfigProvider;
    private TypesafeDocumentConfig typesafeDocumentConfig;
    private TypesafeDocumentConfigProvider typesafeDocumentConfigProvider;



    SetupClientConfig(String type) throws URISyntaxException {
        this.type = type;

        Config configFile = ConfigFactory.load("signing");
        this.typesafeKeystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);
        this.typeSafeKeystoreConfig = typesafeKeystoreConfigProvider.getByName("default");
        this.typesafeServerConfigProvider = new TypesafeServerConfigProvider(configFile);
        this.typesafeServerConfig = typesafeServerConfigProvider.getByName("default");
        this.typesafeDocumentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        this.typesafeDocumentConfig = typesafeDocumentConfigProvider.getByEmail("eulverso2@gmail.com");

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
            keyStore.load((new FileInputStream(kontaktInfo)), typeSafeKeystoreConfig.getPassword().toCharArray());
            keyStoreConfig = KeyStoreConfig.fromKeyStore(new FileInputStream(kontaktInfo)
                    , keyStore.aliases().nextElement(), typeSafeKeystoreConfig.getPassword(), typeSafeKeystoreConfig.getPassword());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the client configuration using a keystore and a truststore.
     */
    public void setupClientConfiguration() throws URISyntaxException{
        clientConfiguration = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(typesafeServerConfig.getServiceUri())
                .trustStore(Certificates.TEST)
                .globalSender(new Sender(this.typesafeDocumentConfig.getSender()))
                .build();
    }

    public KeyStoreConfig getKeyStoreConfig() {
        return keyStoreConfig;
    }
    public ClientConfiguration getClientConfiguration() {
        return this.clientConfiguration;
    }

}

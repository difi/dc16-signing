package no.difi.signing.config;

import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:keystore.properties")
@SuppressWarnings("all")
public class KeystoreConfiguration {

    @Value("${keystore.filename}")
    private String filename;
    @Value("${keystore.password}")
    private String password;
    @Value("${keystore.key.alias}")
    private String keyAlias;
    @Value("${keystore.key.password}")
    private String keyPassword;

    @Bean
    public KeyStoreConfig getKeyStoreConfig() {
        return KeyStoreConfig.fromKeyStore(getClass().getResourceAsStream(filename), keyAlias, password, keyPassword);
    }
}

package no.difi.signing.config;

import no.digipost.signature.client.security.KeyStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.security.KeyStore;
import java.util.Collections;

@Configuration
@PropertySource("classpath:keystore.properties")
@SuppressWarnings("all")
public class KeystoreConfiguration {

    private static Logger logger = LoggerFactory.getLogger(KeystoreConfiguration.class);

    @Value("${keystore.filename}")
    private String filename;
    @Value("${keystore.password}")
    private String password;
    @Value("${keystore.key.alias}")
    private String keyAlias;
    @Value("${keystore.key.password}")
    private String keyPassword;

    @Bean
    public KeyStoreConfig getKeyStoreConfig() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(getClass().getResourceAsStream(filename), password.toCharArray());
        return new KeyStoreConfig(keyStore, keyAlias, password, keyPassword);
    }
}

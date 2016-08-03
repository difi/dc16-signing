package no.difi.signing.config;

import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.URI;

@Configuration
@PropertySource("classpath:digipost.properties")
@SuppressWarnings("all")
public class DigipostConfiguration {

    @Value("${digipost.mode}")
    private Certificates certificates;
    @Value("${digipost.serviceUri}")
    private ServiceUri serviceUri;
    @Value("${digipost.sender}")
    private String sender;

    @Autowired
    private KeyStoreConfig keyStoreConfig;

    @Bean
    public ClientConfiguration getClientConfiguration() {
        return ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(URI.create("http://localhost:8082/"))
                //.serviceUri(serviceUri)
                .trustStore(certificates)
                .globalSender(new Sender(sender))
                .build();
    }

    @Bean
    public DirectClient getDirectClient(ClientConfiguration clientConfiguration) {
        return new DirectClient(clientConfiguration);
    }
}

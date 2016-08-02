package no.difi.signing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ServerConfiguration {

    @Bean(name = "docs")
    public Path getDocsPath() {
        return Paths.get("docs");
    }
}

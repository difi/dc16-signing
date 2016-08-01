package no.difi.signing.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

@Configuration
public class TestConfiguration {

    @Bean(name = "docs")
    public Path getDocsPath() throws URISyntaxException {
        return new File(getClass().getResource("/.index").toURI()).toPath().getParent().resolve("docs");
    }
}

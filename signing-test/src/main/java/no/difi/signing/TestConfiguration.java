package no.difi.signing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

@Configuration
@SuppressWarnings("unused")
public class TestConfiguration {

    @Bean(name = "docs")
    public Path getDocsPath() throws URISyntaxException {
        return new File(getClass().getResource("/.index").toURI()).toPath().getParent().resolve("docs");
    }

    @Bean(name = "storage")
    public Path getStoragePath() throws URISyntaxException {
        return new File(getClass().getResource("/.index").toURI()).toPath().getParent().resolve("storage");
    }
}

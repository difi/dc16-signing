package no.difi.signing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@SuppressWarnings("unused")
public class ServerConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    @Bean(name = "docs")
    public Path getDocsPath() {
        Path path = Paths.get("docs");
        logger.info("Directory 'docs': {}", path.toAbsolutePath());

        return path;
    }

    @Bean(name = "storage")
    public Path getStoragePath() {
        Path path = Paths.get("storage");
        logger.info("Directory 'storage': {}", path.toAbsolutePath());

        return path;
    }
}

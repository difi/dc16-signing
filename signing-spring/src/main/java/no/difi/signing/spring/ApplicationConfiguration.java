package no.difi.signing.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ApplicationConfiguration {

    @Bean(name = "docs")
    public Path getDocsPath() {
        System.out.println(Paths.get("docs").toAbsolutePath());
        return Paths.get("docs");
    }

}

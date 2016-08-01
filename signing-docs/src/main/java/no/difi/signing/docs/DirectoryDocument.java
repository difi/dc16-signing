package no.difi.signing.docs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.api.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryDocument implements Document {

    private String token;
    private Config config;
    private Path path;

    public DirectoryDocument(Path configPath) {
        config = ConfigFactory.parseFile(configPath.toFile());
        token = configPath.toFile().getName().replace(".conf", "");
        path = configPath.getParent().resolve(String.format("%s.pdf", token));
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getTitle() {
        return config.getString("title");
    }

    @Override
    public String getVersion() {
        return config.getString("version");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public String toString() {
        return "DirectoryDocument{" +
                "token='" + getToken() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", version='" + getVersion() + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

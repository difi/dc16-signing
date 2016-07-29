package no.difi.signing.docs;

import com.typesafe.config.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
public class DocumentReader {

    private String title;
    private String version;
    private byte[] document;

    public DocumentReader(Config documentConfig) throws IOException {
        this.title = documentConfig.getString("title");
        this.version = documentConfig.getString("version");

        Path path = Paths.get("C:\\Users\\camp-nto\\dc16-signing\\signing-docs\\src\\main\\resources\\docs\\document.pdf");
        this.document = Files.readAllBytes(path);
    }

    public String getTitle(){
        return this.title;
    }

    public String getVersion(){
        return this.version;
    }

    public byte[] getDocument() {
        return document;
    }
}

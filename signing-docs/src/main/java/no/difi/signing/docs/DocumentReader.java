package no.difi.signing.docs;

import com.typesafe.config.Config;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class DocumentReader {

    private String title;
    private String version;
    private File file;

    public DocumentReader(Config documentConfig) throws IOException {
        this.title = documentConfig.getString("title");
        this.version = documentConfig.getString("version");

        ClassLoader classLoader = getClass().getClassLoader();
        this.file = new File(classLoader.getResource(this.title).getFile());
    }

    public String getTitle(){
        return this.title;
    }

    public String getVersion(){
        return this.version;
    }

    public File getFile() {
        return file;
    }

}

package no.difi.signing.docs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.difi.signing.config.TypesafeDocumentpairConfig;
import no.difi.signing.config.TypesafeDocumentpairConfigProvider;

import java.io.File;

public class Documentpair {
    private String title;
    private String version;
    private TypesafeDocumentpairConfig documentpairConfig;
    private TypesafeDocumentpairConfigProvider documentpairConfigProvider;
    private File documentpairPdf;

    public Documentpair(String name) {
        Config configFile = ConfigFactory.load(name);
        this.documentpairConfig = new TypesafeDocumentpairConfig(configFile);
        this.title = documentpairConfig.getTitle();
        this.version = documentpairConfig.getVersion();

        ClassLoader classLoader = getClass().getClassLoader();
        this.documentpairPdf = new File(classLoader.getResource(this.title).getFile());

    }
}

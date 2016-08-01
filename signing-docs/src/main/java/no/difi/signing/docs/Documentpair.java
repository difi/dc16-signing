package no.difi.signing.docs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class Documentpair {
    private String title;
    private String version;
    private TypesafeDocumentpairConfig documentpairConfig;
    private TypesafeDocumentpairConfigProvider documentpairConfigProvider;
    private File documentpairPdf;

    public Documentpair(String name) {

        Config configFile = ConfigFactory.load("docs/" + name);
        System.out.println(configFile.entrySet().toString());
        this.documentpairConfigProvider = new TypesafeDocumentpairConfigProvider(configFile);
        this.documentpairConfig = documentpairConfigProvider.getDocumentConfig(name);

        this.title = documentpairConfig.getTitle();
        this.version = documentpairConfig.getVersion();

        ClassLoader classLoader = getClass().getClassLoader();
        this.documentpairPdf = new File(classLoader.getResource("docs/" + title + ".pdf").getFile());

    }
}

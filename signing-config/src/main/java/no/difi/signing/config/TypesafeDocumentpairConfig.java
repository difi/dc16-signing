package no.difi.signing.config;

import com.typesafe.config.Config;

public class TypesafeDocumentpairConfig {
    private String title;
    private String version;

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public TypesafeDocumentpairConfig(Config documentpairConfig){
        this.title = documentpairConfig.getString("title");
        this.version = documentpairConfig.getString("version");

    }
}

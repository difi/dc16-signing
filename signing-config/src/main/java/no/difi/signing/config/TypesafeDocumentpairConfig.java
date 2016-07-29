package no.difi.signing.config;

import com.typesafe.config.Config;

import java.util.Map;

public class TypesafeDocumentpairConfig {
    private String title;
    private String version;
    private Map<String, TypesafeDocumentConfig> documents;

    public TypesafeDocumentpairConfig(Config documentpairConfig){
        this.title = documentpairConfig.getString("title");
        this.version = documentpairConfig.getString("version");
    }

    public String getTitle() {
        return this.title;
    }

    public String getVersion() {
        return this.version;
    }

}

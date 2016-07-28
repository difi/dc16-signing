package no.difi.signing.config;

import com.typesafe.config.Config;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeDocumentConfigProvider {

    private Map<String, TypesafeDocumentConfig> documents;

    public TypesafeDocumentConfigProvider(Config config){
        documents = config.getObject("document").keySet().stream()
                .map(key -> config.getConfig(String.format("document.%s",key)))
                .map(c -> new TypesafeDocumentConfig(c))
                .collect(Collectors.toMap(TypesafeDocumentConfig::getEmail, Function.identity()));
    }

    public TypesafeDocumentConfig getByEmail(String email){
        return documents.get("eulverso2@gmail.com");
    }

}

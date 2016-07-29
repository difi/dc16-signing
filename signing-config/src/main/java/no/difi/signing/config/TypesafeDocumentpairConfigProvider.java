package no.difi.signing.config;

import com.typesafe.config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeDocumentpairConfigProvider {

    private Map<String, TypesafeDocumentpairConfig> documentpairs;

    public TypesafeDocumentpairConfigProvider(List<Config> configList)
    {
        configList.stream().forEach(System.out::println);
        documentpairs = new HashMap<>();
        for (Config config : configList){
            Map<String, TypesafeDocumentpairConfig> newDocumentpairs;
            config.entrySet().stream().forEach(System.out::println);
            config.getObject("document").keySet().stream().forEach(System.out::println);
            newDocumentpairs = config.getObject("document").keySet().stream()
                    .map(z -> config.getConfig(String.format("document.%s",z)))
                    .map(z -> new TypesafeDocumentpairConfig(z))
                    .collect(Collectors.toMap(TypesafeDocumentpairConfig::getTitle, Function.identity()));
            documentpairs.putAll(newDocumentpairs);
        }

        System.out.println(documentpairs.toString());
    }

    public TypesafeDocumentpairConfig getByTitle(String title){
        return documentpairs.get(title);
    }
}

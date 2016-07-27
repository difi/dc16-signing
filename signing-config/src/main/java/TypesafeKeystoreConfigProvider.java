import com.typesafe.config.Config;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeKeystoreConfigProvider {

    private Map<String, TypesafeKeystoreConfig> documents;

    public TypesafeKeystoreConfigProvider(Config config){
        documents = config.getObject("keystore").keySet().stream()
                .map(key -> config.getConfig(String.format("keystore.%s",key)))
                .map(c -> new TypesafeKeystoreConfig(c))
                .collect(Collectors.toMap(TypesafeKeystoreConfig::getName, Function.identity()));
    }

    public TypesafeKeystoreConfig getByName(String name){
        return documents.get("default");
    }
}

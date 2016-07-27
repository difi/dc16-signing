import com.typesafe.config.Config;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeServerConfigProvider {

    private Map<String, TypesafeServerConfig> servers;

    public TypesafeServerConfigProvider(Config config) throws URISyntaxException{
        servers = config.getObject("server").keySet().stream()
                .map(key -> config.getConfig(String.format("server.%s",key)))
                .map(c -> safeConfig(c))
                .collect(Collectors.toMap(TypesafeServerConfig::getName, Function.identity()));
    }

    public TypesafeServerConfig getByName(String name){
        return servers.get("default");
    }
    public TypesafeServerConfig safeConfig(final Config a){
        try {
            return new TypesafeServerConfig(a);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}

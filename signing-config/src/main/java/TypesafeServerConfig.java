import com.typesafe.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class TypesafeServerConfig {
    private String id;
    private String name;
    private URI completionUri;
    private URI rejectionUri;
    private URI errorUri;
    private URI serviceUri;

    public TypesafeServerConfig(Config serverConfig) throws URISyntaxException{
        System.out.println(serverConfig.entrySet().toString());
        this.id = UUID.randomUUID().toString();
        this.name = serverConfig.getString("name");
        this.completionUri = new URI(serverConfig.getString("completion_uri"));
        this.rejectionUri = new URI(serverConfig.getString("rejection_uri"));
        this.errorUri = new URI(serverConfig.getString("error_uri"));
        this.serviceUri = new URI(serverConfig.getString("serviceURI"));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public URI getCompletionUri() {
        return completionUri;
    }

    public URI getRejectionUri() {
        return rejectionUri;
    }

    public URI getErrorUri() {
        return errorUri;
    }

    public URI getServiceUri() {
        return serviceUri;
    }
}

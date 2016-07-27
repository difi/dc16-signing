import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TypesafeServerConfigTest {
    private TypesafeServerConfig serverConfig;
    private TypesafeServerConfigProvider serverConfigProvider;

    @BeforeClass
    public void setup() throws URISyntaxException{
        Config typesafeServerFile = ConfigFactory.load();
        Config serverConfig = typesafeServerFile.getConfig("server");
        this.serverConfigProvider = new TypesafeServerConfigProvider(typesafeServerFile);
        this.serverConfig = serverConfigProvider.getByName("default");

    }

    @Test
    public void serverConfigHasCorrectName(){
        Assert.assertEquals(serverConfig.getName(),"default");
    }

    @Test
    public void serverConfigHasCorrectCompletionUri() throws URISyntaxException{
        Assert.assertEquals(serverConfig.getCompletionUri(), new URI("http://localhost:8081/onCompletion"));
    }
    @Test
    public void serverConfigHasCorrectRejectionUri() throws URISyntaxException{
        Assert.assertEquals(serverConfig.getRejectionUri(), new URI("http://localhost:8081/onRejection"));
    }
    @Test
    public void serverConfigHasCorrectErrorUri() throws URISyntaxException{
        Assert.assertEquals(serverConfig.getErrorUri(),new URI("http://localhost:8081/onError"));
    }

    @Test
    public void serverConfigHasCorrectServiceUri() throws URISyntaxException{
        Assert.assertEquals(serverConfig.getServiceUri(),new URI("http://localhost:8082/"));
    }

}

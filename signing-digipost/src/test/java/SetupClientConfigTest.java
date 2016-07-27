import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

public class SetupClientConfigTest {


    @Test
    public void setupKeystoreConfigNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        Assert.assertNotNull(setupClientConfig);
    }

    @Test
    public void setUpClientConfigurationNotNull() throws URISyntaxException{
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupClientConfiguration();
        Assert.assertNotNull(setupClientConfig);
    }
}

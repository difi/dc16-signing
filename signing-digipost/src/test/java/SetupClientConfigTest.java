
import org.testng.Assert;
import org.testng.annotations.Test;

public class SetupClientConfigTest {


    @Test
    public void setupKeystoreConfigNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        Assert.assertNotNull(setupClientConfig);

    }

    @Test
    public void setUpClientConfigurationNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupClientConfiguration("123456789");
        Assert.assertNotNull(setupClientConfig);
    }
}
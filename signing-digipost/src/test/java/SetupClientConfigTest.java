
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-nto on 12.07.2016.
 */
public class SetupClientConfigTest {


    @Test
    public void setupKeystoreConfigNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig();
        setupClientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        Assert.assertNotNull(setupClientConfig);

    }

    @Test
    public void setUpClientConfigurationNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig();
        setupClientConfig.setupClientConfiguration("123456789");
        Assert.assertNotNull(setupClientConfig);
    }
}
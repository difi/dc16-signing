<<<<<<< HEAD
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-nto on 12.07.2016.
 */
=======

import org.testng.Assert;
import org.testng.annotations.Test;

>>>>>>> e98348cca14c8d5d257670929b49b74729ec36fe
public class SetupClientConfigTest {


    @Test
    public void setupKeystoreConfigNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
<<<<<<< HEAD
        SetupClientConfig setupClientConfig = new SetupClientConfig();
=======
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
>>>>>>> e98348cca14c8d5d257670929b49b74729ec36fe
        setupClientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        Assert.assertNotNull(setupClientConfig);

    }

    @Test
    public void setUpClientConfigurationNotNull(){
        AsiceMaker asiceMaker = new AsiceMaker();
<<<<<<< HEAD
        SetupClientConfig setupClientConfig = new SetupClientConfig();
        setupClientConfig.setupClientConfiguration("123456789");
        Assert.assertNotNull(setupClientConfig);
    }
}
=======
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupClientConfiguration("123456789");
        Assert.assertNotNull(setupClientConfig);
    }
}
>>>>>>> e98348cca14c8d5d257670929b49b74729ec36fe

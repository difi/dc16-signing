package no.difi.signing.digipost;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class SetupClientConfigTest {


    @Test
    public void setupKeystoreConfigNotNull() throws URISyntaxException, IOException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        Assert.assertNotNull(setupClientConfig);
    }

    @Test
    public void setUpClientConfigurationNotNull() throws URISyntaxException, IOException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig setupClientConfig = new SetupClientConfig("Direct");
        setupClientConfig.setupClientConfiguration();
        Assert.assertNotNull(setupClientConfig);
    }
}

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by camp-nto on 27.07.2016.
 */
public class TypesafeKeystoreConfigTest {

    private TypesafeKeystoreConfig keystoreConfig;
    private TypesafeKeystoreConfigProvider keystoreConfigProvider;


    @BeforeClass
    public void setup(){
        Config keystoreConfigFile = ConfigFactory.load();
        Config specificConfig = keystoreConfigFile.getConfig("keystore");
        this.keystoreConfigProvider = new TypesafeKeystoreConfigProvider(keystoreConfigFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
    }


    @Test
    public void keystoreConfig_has_correct_name(){
        Assert.assertEquals(keystoreConfig.getName(),"default");
    }

    @Test
    public void keystoreConfig_has_correct_keystore(){
        Assert.assertEquals(keystoreConfig.getKeystore(),"kontaktinfo-client-test.jks");
    }

    @Test
    public void keystoreConfig_has_correct_password(){
        Assert.assertEquals(keystoreConfig.getPassword(),"changeit");
    }

}

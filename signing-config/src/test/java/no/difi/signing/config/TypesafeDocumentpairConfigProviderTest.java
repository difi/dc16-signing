package no.difi.signing.config;

import com.typesafe.config.Config;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TypesafeDocumentpairConfigProviderTest {

    private TypesafeDocumentpairConfigProvider documentpairConfigProvider;
    @BeforeClass
    public void setup(){
        System.out.println(System.getProperty("user.dir"));
        List<Config> configList = new ArrayList<>();

        File dir = new File(System.getProperty("user.dir"));
        File[] configListing = dir.listFiles();

        if(configListing != null){
            for(File config : configListing){
                    String extension = config.getName().split(".")[1];
                    System.out.println(extension);
            }
        }


    }
    @Test
    public void checkCorrectReadingOfAllConfigs(){

    }
}

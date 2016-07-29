package no.difi.signing.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TypesafeDocumentpairConfigTest {

    private TypesafeDocumentpairConfig documentpairConfig;
    private TypesafeDocumentpairConfigProvider documentpairConfigProvider;
    @BeforeClass
    public void setup(){
        System.out.println(System.getProperty("user.dir"));
        List<Config> configList = new ArrayList<>();
        System.out.println(System.getProperty("user.dir").concat("\\src\\test\\resources\\docs"));
        File dir = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\docs"));
        File[] configListing = dir.listFiles();


        if(configListing != null){
            for(File config : configListing){
                String filename = config.getName();
                String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
                //String extension = config.getName().split(".")[0];
                System.out.println(extension);
                if(extension.contains("conf")){
                    System.out.println("extension equaled conf");
                    System.out.println(config.getAbsolutePath());
                    Config configFilename = ConfigFactory.parseFile(config);
                    configFilename.entrySet().stream().forEach(System.out::println);
                    configList.add(configFilename);

                }
            }
        }
        this.documentpairConfigProvider = new TypesafeDocumentpairConfigProvider();
        this.documentpairConfig = documentpairConfigProvider.getByTitle("document");


    }

    @Test
    public void documentpairConfigHasCorrectVersion(){
        Assert.assertEquals(documentpairConfig.getVersion(),"1.0");
    }

    @Test
    public void documentpairConfigHasCorrectTitle(){
        Assert.assertEquals(documentpairConfig.getTitle(),"document");
    }

    @Test
    public void documentpairConfigExists(){
        //
    }

}

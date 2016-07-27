import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TypesafeDocumentConfigTest {
    private TypesafeDocumentConfig documentConfig;
    private TypesafeDocumentConfigProvider documentConfigProvider;
    private List<String> signerList = new ArrayList<>();


    @BeforeClass
    public void setup(){
        Config documentConfigFile = ConfigFactory.load();
        Config specificConfig = documentConfigFile.getConfig("document");
        this.documentConfigProvider = new TypesafeDocumentConfigProvider(documentConfigFile);
        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        signerList.add("17079493538");
        signerList.add("17079493457");
        signerList.add("17079493295");
    }

    @Test
    public void documentConfigHasCorrectSigner(){
        Assert.assertEquals(documentConfig.getSigner(),"17079493538");
    }

    @Test
    public void documentConfigHasCorrectSender(){
        Assert.assertEquals(documentConfig.getSender(),"991825827");
    }

    @Test
    public void documentConfigHasCorrectSignersList(){
        Assert.assertEquals(documentConfig.getSigners(),this.signerList);
    }

    @Test
    public void documentConfigHasCorrectEmail(){
        Assert.assertEquals(documentConfig.getEmail(),"eulverso2@gmail.com");
    }

    @Test
    public void documentConfigHasCorrectPath(){
        Assert.assertEquals(documentConfig.getRelativeDocumentPath(),"Documents//Dokument til signering 3.pdf");
    }

}

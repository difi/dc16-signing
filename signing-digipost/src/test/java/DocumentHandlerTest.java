
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.direct.DirectDocument;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class DocumentHandlerTest {


    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;

    @Test
    public void documentTitleAndNameCorrect() throws IOException, URISyntaxException {
        Config configFile = ConfigFactory.load();
        this.keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);
        this.keystoreConfig = keystoreConfigProvider.getByName("default");

        ClassLoader classLoader = getClass().getClassLoader();
        DocumentHandler documentHandler = new DocumentHandler();
        File pathToFile = new File(classLoader.getResource(keystoreConfig.getKeystore()).getFile());
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(pathToFile).toString();

        DirectDocument directDocument = DocumentHandler.pdfToDirectDocument(PDFPath);

        Assert.assertEquals(directDocument.getTitle(), "Subject");
        Assert.assertEquals(directDocument.getFileName(), "document.pdf");
        Assert.assertNotNull(documentHandler);

    }

}

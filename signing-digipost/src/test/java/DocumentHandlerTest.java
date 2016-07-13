/**
 * Created by camp-nto on 12.07.2016.
 */
import no.digipost.signature.client.direct.DirectDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class DocumentHandlerTest {


    @Test
    public void simple() throws IOException{

        ClassLoader classLoader = getClass().getClassLoader();
        File pathToFile = new File(classLoader.getResource("kontaktinfo-client-test.jks").getFile());
        String PDFPath = DocumentHandler.setAbsolutePathToPDF(pathToFile).toString();

        DirectDocument directDocument = DocumentHandler.pdfToDirectDocument(PDFPath);

        Assert.assertEquals(directDocument.getTitle(), "Subject");
        Assert.assertEquals(directDocument.getFileName(), "document.pdf");

    }
}
package no.difi.signing.docs;

import no.difi.signing.api.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class DirectoryDocumentTest {

    @Test
    public void simple() throws URISyntaxException {
        Path path = new File(getClass().getResource("/docs/document1.conf").toURI()).toPath();
        Document document = new DirectoryDocument(path);

        Assert.assertEquals(document.getToken(), "document1");
        Assert.assertEquals(document.getTitle(), "Document 1");
        Assert.assertEquals(document.getVersion(), "1");
    }
}

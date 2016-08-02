package no.difi.signing.digipost;

import no.difi.signing.TestApplication;
import no.difi.signing.mock.DigipostServerMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestApplication.class)
public class DigipostSigningServiceTest extends AbstractTestNGSpringContextTests{

    private DigipostServerMock digipostServerMock;

    @BeforeClass
    public void beforeClass() throws Exception {
        digipostServerMock = new DigipostServerMock();
        digipostServerMock.start();
    }

    @AfterClass
    public void afterClass() {
        digipostServerMock.shutDown();
    }

    @Autowired
    private DigipostSigningService signingService;

    @Test
    public void simple() {
        System.out.println(signingService);
    }

}

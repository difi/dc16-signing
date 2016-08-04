package no.difi.signing.controller;

import no.difi.signing.TestApplication;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;

@SpringBootTest(classes = TestApplication.class)
public class OverviewControllerTest  extends AbstractTestNGSpringContextTests {

    @Autowired
    SignatureRepository signatureRepository;

    @Test
    public void printAllSignatures(){
        Signature signature = Signature.newInstance();
        signature.setId(0);
        signature.setPid("12345678901234");
        signature.setDocumentTitle("Document 1");
        signature.setDocumentToken("document1");
        signature.setDocumentVersion("1");
        signature.setTimestamp(new Date());
        signatureRepository.save(signature);

        OverviewController overviewController = new OverviewController();

    }
}

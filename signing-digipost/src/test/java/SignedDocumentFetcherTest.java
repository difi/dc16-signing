import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.junit.Before;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testng.annotations.Test;

import java.io.IOException;


public class SignedDocumentFetcherTest {

    @Test
    public void getSignedDocuments_returns_document_inputStram(){
        StatusReader statusReader = mock(StatusReader.class);
        DirectClient directClient = mock(DirectClient.class);
        DirectJobStatusResponse directJobStatusResponse = mock(DirectJobStatusResponse.class);
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(directClient, statusReader);

        signedDocumentFetcher.setDirectJobStatusResponse(directJobStatusResponse);
        signedDocumentFetcher.getSignedDocuments("xades");
        signedDocumentFetcher.getSignedDocuments("pades");

        Assert.assertEquals(signedDocumentFetcher.getSignedDocuments("pade"), null);
    }

    @Test
    public void getPades_returns_string() throws IOException {
        DirectJobResponse directJobResponse = mock(DirectJobResponse.class);
        DirectClient directClient = mock(DirectClient.class);
        StatusReader statusReader = new StatusReader(directClient, directJobResponse, "token");
        DirectJobStatusResponse directJobStatusResponse = mock(DirectJobStatusResponse.class);
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(directClient, statusReader);

        //signedDocumentFetcher.getPades();
        //Assert.assertEquals(signedDocumentFetcher.getPades(), "failed");
    }

   


}

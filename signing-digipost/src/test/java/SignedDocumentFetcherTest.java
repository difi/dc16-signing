import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.portal.PortalClient;
import org.mockito.Mock;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignedDocumentFetcherTest {

    @Mock private static DirectClient client;
    @Mock private static StatusReader statusReader;
    @Mock private static DirectJobStatusResponse statusResponse;
    @Mock private static InputStream inputStream;


    @BeforeClass
    public static void setUp() throws IOException{
        client = mock(DirectClient.class);
        statusReader = mock(StatusReader.class);
        statusResponse = mock(DirectJobStatusResponse.class);
        inputStream = new ByteArrayInputStream("....".getBytes());



        when(client.getStatusChange()).thenReturn(statusResponse);
        when(statusReader.getStatusResponse()).thenReturn(statusResponse);
        when(client.getPAdES(statusReader.getStatusResponse().getpAdESUrl())).thenReturn(inputStream);


    }

    @Test
    public void checkOutputStreamEqualsInputStream()throws IOException{
        SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(client,statusReader);
        signedDocumentFetcher.getPades();
        System.out.println(System.getProperty("user.dir") + "/pAdESTest.pdf");
        InputStream inputStreamTest = new FileInputStream(System.getProperty("user.dir") + "/pAdESTest.pdf");

        Assert.assertEquals(inputStream,inputStreamTest);
    }
    //Still not working
    //@Test
    //public void getPadesReturnesFetchedPade() throws IOException {
    //    SetupClientConfig clientConfig = new SetupClientConfig();
    //    clientConfig.setupClientConfiguration("123456789");


    //    DirectClient client = new DirectClient(clientConfig.getClientConfiguration());
    //    AsiceMaker asiceMaker = new AsiceMaker();
    //    SignatureJob signatureJob = asiceMaker.getSignatureJob();

    //    String statusQueryToken = "0A3BQ54C";
    //    DirectJobResponse directJobResponse = client.create((DirectJob)signatureJob);
    //    DirectJobStatusResponse directJobStatusResponse = client.getStatus(StatusReference.of(directJobResponse).withStatusQueryToken(statusQueryToken));

    //    StatusReader statusReader = new StatusReader(client, directJobResponse, statusQueryToken);
    //    SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(client, statusReader);
    //    Assert.assertEquals(signedDocumentFetcher.getPades(), "fetched pade");

    //}
}

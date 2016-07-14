import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.portal.PortalClient;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

public class SignedDocumentFetcherTest {

    @Mock private static DirectClient client;
    @Mock private static StatusReader statusReader;
    @Mock private static DirectJobStatusResponse statusResponse;

    @BeforeClass
    public static void setUp(){
        client = mock(DirectClient.class);
        statusReader = mock(StatusReader.class);
        statusResponse = mock(DirectJobStatusResponse.class);


        when(client.getStatusChange()).thenReturn(statusResponse);
        when(statusReader.getStatusResponse()).thenReturn(statusResponse);
        when(client.getPAdES(statusReader.getStatusResponse().getpAdESUrl()));

    }

    //Still not working
    //@Test
    //public void getPadesReturnesFetchedPade() throws IOException {
    //    SetupClientConfig clientConfig = new SetupClientConfig();
    //    clientConfig.setupClientConfiguration("123456789");


    //    DirectClient client = new DirectClient(clientConfig.getClientConfiguration());
    //    AsiceMaker asiceMaker = new AsiceMaker();
    //    SignatureJob signatureJob = asiceMaker.getSignatureJob();
    //    DirectJobResponse directJobResponse = client.create((DirectJob)signatureJob);

    //    StatusReader statusReader = new StatusReader(client, directJobResponse, "token");
    //    SignedDocumentFetcher signedDocumentFetcher = new SignedDocumentFetcher(client, statusReader);

    //}
}

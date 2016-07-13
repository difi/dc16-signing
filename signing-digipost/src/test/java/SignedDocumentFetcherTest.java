import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by camp-nto on 12.07.2016.
 */
public class SignedDocumentFetcherTest {

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
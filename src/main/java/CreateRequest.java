import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;

public class CreateRequest {

    private DirectClient directClient;
    private ExitUrls exitUrls;
    public void configure(DirectClient directClient){
        exitUrls = ExitUrls.of(
                "http://sender.org/onCompletion",
                "http://sender.org/onRejection",
                "http://sender.org/onError"
        );
        this.directClient = directClient;


    }

    public void sendRequest(SignatureJob signatureJob){
        DirectJobResponse directJobResponse = directClient.create((DirectJob)signatureJob);
        String statusQueryToken = "";

        DirectJobStatusResponse directJobStatusResponse = directClient.getStatus(StatusReference.of(directJobResponse).withStatusQueryToken(statusQueryToken));
    }
}

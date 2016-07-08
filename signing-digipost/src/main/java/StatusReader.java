import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.direct.StatusReference;

public class StatusReader {
    private DirectClient client;
    private DirectJobStatusResponse statusResponse;
    private DirectJobResponse jobResponse;
    private String token;

    StatusReader(DirectClient client, DirectJobResponse jobResponse, String token){
        this.client = client;
        this.jobResponse = jobResponse;
        this.token = token;
    }

    public void getStatus(){
        statusResponse = client.getStatus(StatusReference.of(jobResponse).withStatusQueryToken(token));
        statusResponse.getStatus();
    }


}

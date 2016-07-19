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

    public String getStatus(){
        this.statusResponse = client.getStatus(StatusReference.of(jobResponse).withStatusQueryToken(token));
        System.out.println(this.statusResponse.getStatus().toString());
        System.out.println(statusResponse.getStatus().toString());
        return statusResponse.getStatus().toString();
    }

    public void confirmProcessedSignatureJob(){
        this.client.confirm(statusResponse);
    }

    public DirectJobStatusResponse getStatusResponse(){
        return this.statusResponse;
    }




}

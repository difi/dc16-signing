import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.direct.StatusReference;

public class StatusReader {
    private DirectClient client;
    private DirectJobStatusResponse statusResponse;
    private DirectJobResponse jobResponse;
    private String token;

    StatusReader(DirectClient client, DirectJobResponse jobResponse, String token) {
        this.client = client;
        this.jobResponse = jobResponse;
        this.token = token;
    }

    public String getStatus(){
        //La til if, må kanskje fjernes
            this.statusResponse = client.getStatus(StatusReference.of(jobResponse).withStatusQueryToken(token));
            return statusResponse.getStatus().toString();
    }

    public void setDirectClient(DirectClient client){
        this.client = client;
    }

    public void setJobResponse(DirectJobResponse jobResponse){
        this.jobResponse = jobResponse;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void confirmProcessedSignatureJob() {
        this.client.confirm(statusResponse);
    }

    public DirectJobStatusResponse getStatusResponse() {
        return this.statusResponse;
    }


}

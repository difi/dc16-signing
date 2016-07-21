import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.direct.StatusReference;

import java.util.Optional;

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

    public String getStatus() {
        this.statusResponse = client.getStatus(StatusReference.of(jobResponse).withStatusQueryToken(token));
        return statusResponse.getStatus().toString();
    }

    public void confirmProcessedSignatureJob() {
        this.client.confirm(statusResponse);
    }

    public Optional<DirectJobStatusResponse> getStatusResponse() {
        return Optional.of(statusResponse);
    }


}

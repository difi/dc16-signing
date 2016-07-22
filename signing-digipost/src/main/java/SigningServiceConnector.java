import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalJobResponse;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Class responsible for contacting the signing service.
 */
public class SigningServiceConnector {
    private ClientConfiguration client;
    private String redirectUrl;
    private String statusUrl;
    private String cancellationUrl;
    //Response and client objects
    private DirectJobResponse directJobResponse;
    private DirectClient directClient;

    private PortalClient portalClient;

    public SigningServiceConnector() throws IOException {

    }


    /**
     * Check the status of a job. Currently just prints out information regarding the job.
     *
     * @return
     */

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public String getStatusUrl() {
        return this.statusUrl;
    }

    /**
     * Sends a request to difi_test based on a signaturejob and a keyconfig.
     * Returns false if no response was received, true otherwise.
     */
    public boolean sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig) throws URISyntaxException {

        //Both the serviceUri and the truststore are constants taken from the api library signature-api-client-java
        client = ClientConfiguration.builder(keyStoreConfig)
                //.serviceUri(new URI("http://localhost:8082/"))
                .serviceUri(ServiceUri.DIFI_TEST)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();

        directClient = new DirectClient(client);
        directJobResponse = directClient.create((DirectJob) signatureJob);

        redirectUrl = directJobResponse.getRedirectUrl();
        statusUrl = directJobResponse.getStatusUrl();

        if (directJobResponse != null) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

    //Added for testing
    public void setDirectClient(KeyStoreConfig keyStoreConfig){
        client = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(ServiceUri.DIFI_TEST)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();

        this.directClient = new DirectClient(client);
    }

    public void setDirectJobResponse(SignatureJob signatureJob){
        this.directJobResponse = directClient.create((DirectJob)signatureJob);
    }

    public boolean sendPortalRequest(PortalJob portalJob, KeyStoreConfig keyStoreConfig) throws URISyntaxException {
        client = ClientConfiguration.builder(keyStoreConfig)
                //.serviceUri(new URI("http://localhost:8082/"))
                .serviceUri(ServiceUri.DIFI_TEST)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();

        portalClient =  new PortalClient(client);
        PortalJobResponse portalJobResponse = portalClient.create(portalJob);

        this.cancellationUrl = portalJobResponse.getCancellationUrl().toString();

        if (portalJobResponse != null) {
            return true;
        } else {
            return false;
        }


    }

    public Optional<DirectClient> getDirectClient() {
        return Optional.of(directClient);
    }

    public Optional<DirectJobResponse> getDirectJobResponse() {
        return Optional.of(directJobResponse);
    }

    public void setDirectClient(DirectClient client){
        this.directClient = client;
    }


    public Optional<PortalClient> getPortalClient() {

        return Optional.ofNullable(portalClient);

    }
}



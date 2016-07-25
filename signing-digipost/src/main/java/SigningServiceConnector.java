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
import org.apache.catalina.Server;

import java.io.IOException;
import java.net.URI;
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
    private Optional<DirectJobResponse> directJobResponse;
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

    public void setPortalClient(PortalClient portalClient){
        this.portalClient = portalClient;                            //Added later
    }

    /**
     * Sends a request to difi_test based on a signaturejob and a keyconfig.
     * Returns false if no response was received, true otherwise.
     */
    public Optional<DirectJobResponse> sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig, URI... server) throws URISyntaxException {

        URI ServerURI = URI.create("https://api.difitest.signering.posten.no/api");
        if(server.length != 0){
            ServerURI = server[0];
        }
        //Both the serviceUri and the truststore are constants taken from the api library signature-api-client-java
        client = ClientConfiguration.builder(keyStoreConfig)
                //.serviceUri(new URI("http://localhost:8082/"))
                .serviceUri(ServerURI)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();

        directClient = new DirectClient(client);
        directJobResponse = Optional.ofNullable(directClient.create((DirectJob) signatureJob));

        if(directJobResponse.isPresent()){
            redirectUrl = directJobResponse.get().getRedirectUrl();
            statusUrl = directJobResponse.get().getStatusUrl();
        }

        return directJobResponse;
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
        this.directJobResponse = Optional.ofNullable(directClient.create((DirectJob)signatureJob));
    }

    public Optional<PortalJobResponse> sendPortalRequest(PortalJob portalJob, KeyStoreConfig keyStoreConfig, URI... server) throws URISyntaxException {
        URI ServerURI = URI.create("https://api.difitest.signering.posten.no/api");
        if(server.length != 0){
            ServerURI = server[0];
        }
        client = ClientConfiguration.builder(keyStoreConfig)
                //.serviceUri()
                .serviceUri(ServerURI)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender("991825827"))
                .build();

        portalClient =  new PortalClient(client);
        Optional<PortalJobResponse> portalJobResponse = Optional.ofNullable(portalClient.create(portalJob));

        if(portalJobResponse.isPresent()){
            this.cancellationUrl = portalJobResponse.get().getCancellationUrl().toString();
        } else {
            this.cancellationUrl = "No cancellation url due to no response";
        }

        return portalJobResponse;


    }

    public Optional<DirectClient> getDirectClient() {
        return Optional.ofNullable(directClient);
    }

    public Optional<DirectJobResponse> getDirectJobResponse() {
        return directJobResponse;
    }

    public void setDirectClient(DirectClient client){
        this.directClient = client;
    }


    public Optional<PortalClient> getPortalClient() {

        return Optional.ofNullable(portalClient);

    }
}



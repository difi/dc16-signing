import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
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
    private Optional<DirectJobResponse> directJobResponse;
    private DirectClient directClient;
    private PortalClient portalClient;

    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;

    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;

    public SigningServiceConnector() throws IOException, URISyntaxException {
        Config configFile = ConfigFactory.load("application.conf");
        documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);

        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.serverConfig = serverConfigProvider.getByName("default");
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
    }

    /**
     * Check the status of a job. Currently just prints out information regarding the job.
     * @return
     */


    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    /**
     * Sends a request to difi_test based on a signaturejob and a keyconfig.
     * Returns false if no response was received, true otherwise.
     */
    public Optional<DirectJobResponse> sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig, URI... server) throws URISyntaxException {

        //URI ServerURI = URI.create("https://api.difitest.signering.posten.no/api");
        URI ServerURI = serverConfig.getServiceUri();

        if(server.length != 0){
            ServerURI = server[0];
        }
        //Both the serviceUri and the truststore are constants taken from the api library signature-api-client-java
        client = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(ServerURI)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender(documentConfig.getSender()))
                .build();

        directClient = new DirectClient(client);
        directJobResponse = Optional.ofNullable(directClient.create((DirectJob) signatureJob));

        if(directJobResponse.isPresent()){
            redirectUrl = directJobResponse.get().getRedirectUrl();
            statusUrl = directJobResponse.get().getStatusUrl();
        }

        return directJobResponse;
    }


    /**
     * Sends a request for portal signing difi_test based on a signaturejob and a keyconfig.
     * Returns false if no response was received, true otherwise.
     * @param portalJob
     * @param keyStoreConfig
     * @param server
     * @return
     * @throws URISyntaxException
     */
    public Optional<PortalJobResponse> sendPortalRequest(PortalJob portalJob, KeyStoreConfig keyStoreConfig, URI... server) throws URISyntaxException {
        URI ServerURI = serverConfig.getServiceUri();

        if(server.length != 0){
            ServerURI = server[0];
        }
        client = ClientConfiguration.builder(keyStoreConfig)
                .serviceUri(ServerURI)
                .trustStore(Certificates.TEST)
                .globalSender(new Sender(documentConfig.getSender()))
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

    public Optional<PortalClient> getPortalClient() {
        return Optional.ofNullable(portalClient);
    }
}



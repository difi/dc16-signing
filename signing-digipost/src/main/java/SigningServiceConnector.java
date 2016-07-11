import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalDocument;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalJobResponse;
import no.digipost.signature.client.security.KeyStoreConfig;

import java.io.File;
import java.io.IOException;

/**
 * Class responsible for contacting the signing service.
 */
public class SigningServiceConnector {
    private ClientConfiguration client;
    private String redirectUrl;
    private String statusUrl;

    //Response and client objects
    private DirectJobResponse directJobResponse;
    private DirectClient directClient;
    private DirectJobStatusResponse directJobStatusResponse;

    public SigningServiceConnector() throws IOException {

    }

    /**
     * Check the status of a job. Currently just prints out information regarding the job.
     * @return
     */
        public String checkStatus(){
            String statusUrl = directJobResponse.getStatusUrl().toString();
            directJobStatusResponse = directClient.getStatusChange();
            System.out.println(directJobStatusResponse.toString());
            DirectJobStatus directJobStatus = directJobStatusResponse.getStatus();
            System.out.println(directJobStatus.toString());
            return directJobStatus.toString();
        }

        public String getRedirectUrl(){
            return this.redirectUrl;
        }

        public String getStatusUrl() { return this.statusUrl;
        }
        /**
         *     Sends a request to difi_test based on a signaturejob and a keyconfig.
         *     Returns false if no response was received, true otherwise.
         */
        public boolean sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig) {

            //Both the serviceUri and the truststore are constants taken from the api library signature-api-client-java
            client = ClientConfiguration.builder(keyStoreConfig)
                    .serviceUri(ServiceUri.DIFI_TEST)
                    .trustStore(Certificates.TEST)
                    .globalSender(new Sender("991825827"))
                    .build();

            directClient = new DirectClient(client);
            directJobResponse = directClient.create((DirectJob)signatureJob);

            redirectUrl = directJobResponse.getRedirectUrl();
            statusUrl = directJobResponse.getStatusUrl();

            if(directJobResponse != null){
                return true;
            } else {
                return false;
            }
        }

    public void sendPortalRequest(PortalJob portalJob, KeyStoreConfig keyStoreConfig){
        client = ClientConfiguration.builder(keyStoreConfig)
            .serviceUri(ServiceUri.DIFI_TEST)
            .trustStore(Certificates.TEST)
            .globalSender(new Sender("991825827"))
            .build();
        PortalClient portalClient =  new PortalClient(client);

        PortalJobResponse portalJobResponse = portalClient.create(portalJob);
    }

    public DirectClient getDirectClient(){
        return this.directClient;
    }

    public DirectJobResponse getDirectJobResponse(){
        return this.directJobResponse;
    }
    }


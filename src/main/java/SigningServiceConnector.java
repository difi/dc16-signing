import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.PAdESReference;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.core.XAdESReference;
import no.digipost.signature.client.direct.*;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

/**
 * Created by camp-mlo on 01.07.2016.
 */
public class SigningServiceConnector {
    private File filePath;
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

        public XAdESReference getXades() {
            directJobStatusResponse = directClient.getStatusChange();
            return directJobStatusResponse.getxAdESUrl();
        }

        public PAdESReference getPades(){
            directJobStatusResponse = directClient.getStatusChange();
            return directJobStatusResponse.getpAdESUrl();
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

            redirectUrl = directJobResponse.getRedirectUrl().toString();
            statusUrl = directJobResponse.getStatusUrl().toString();

            if(directJobResponse != null){
                return true;
            } else {
                return false;
            }
        }
    }


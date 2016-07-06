import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
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
public class SendHTTPRequest {
    private File filePath;
    private ClientConfiguration client;
    private String redirectUrl;
    private String statusUrl;
    private DirectJobResponse directJobResponse;
    private DirectClient directClient;
    public SendHTTPRequest() throws IOException {

    }

    /**
     * Check the status of a job. Currently just prints out information regarding the job.
     * @return
     */
        public String checkStatus(){
            String statusUrl = directJobResponse.getStatusUrl().toString();
            DirectJobStatusResponse statusChange = directClient.getStatusChange();
            System.out.println(statusChange.toString());
            System.out.println(statusChange.getpAdESUrl());
            System.out.println(statusChange.getxAdESUrl());
            return statusChange.toString();
        }

        public String getRedirectUrl(){
            return this.redirectUrl;
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

            this.directClient = new DirectClient(client);
            this.directJobResponse = directClient.create((DirectJob)signatureJob);
            this.redirectUrl = directJobResponse.getRedirectUrl().toString();
            this.statusUrl = directJobResponse.getStatusUrl().toString();

            if(directJobResponse != null){
                return true;
            } else {
                return false;
            }
        }
    }


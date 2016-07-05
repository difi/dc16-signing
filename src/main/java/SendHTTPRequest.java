import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectJobResponse;
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

    public SendHTTPRequest() throws IOException {

    }


        /**
         *     Sends a request to difi_test based on a signaturejob and a keyconfig.
         */
        public boolean sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig) {

            //Both the serviceUri and the truststore are taken from the api library signature-api-client-java
            client = ClientConfiguration.builder(keyStoreConfig)
                    .serviceUri(ServiceUri.DIFI_TEST)
                    .trustStore(Certificates.TEST)
                    .globalSender(new Sender("991825827"))
                    .build();

            DirectClient directClient = new DirectClient(client);

            DirectJobResponse directJobResponse = directClient.create((DirectJob)signatureJob);

            //Test statements
            System.out.println(directJobResponse.getRedirectUrl().toString());
            System.out.println(directJobResponse.getStatusUrl().toString());
            if(directJobResponse != null){
                return true;
            } else {
                return false;
            }
        }
    }


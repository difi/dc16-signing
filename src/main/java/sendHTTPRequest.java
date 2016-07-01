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

    public void sendRequest(SignatureJob signatureJob, KeyStoreConfig keyStoreConfig) throws IOException {
            HttpClient httpClient = HttpClients.createDefault();

            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("upload_file", new File("C:\\Users\\camp-mlo\\Documents\\GitHub\\dc16-signing\\20160701121759923-asice.zip"), ContentType.create("application/octet-stream"), "C:\\Users\\camp-mlo\\Documents\\GitHub\\dc16-signing\\20160701121759923-asice.zip")
                    .build();

            HttpPost httpPost = new HttpPost(ServiceUri.DIFI_TEST.toString());
            httpPost.setEntity(entity);
            //HttpResponse response = httpClient.execute(httpPost);
            //HttpEntity result = response.getEntity();

            client = ClientConfiguration.builder(keyStoreConfig)
                    .serviceUri(ServiceUri.DIFI_TEST)
                    .globalSender(new Sender("123456789"))
                    .build();

            DirectClient directClient = new DirectClient(client);

        DirectJobResponse directJobResponse = directClient.create((DirectJob)signatureJob);
        }
    }


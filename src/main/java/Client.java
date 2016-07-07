import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJob;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.portal.PortalClient;

/**
 * Has a clientconfiguration and can make clients.
 */
public class Client {
    private ClientConfiguration clientConfiguration;
    private DirectClient directClient;
    private DirectJobResponse directJobResponse;

    Client(ClientConfiguration clientConfiguration){
        this.clientConfiguration = clientConfiguration;
    }

    public DirectClient getDirectClient(){
        if(directClient != null{
            return directClient;
        } else {
            directClient = new DirectClient(this.clientConfiguration);
            return directClient;

        }
    }

    public void updateConfiguration(ClientConfiguration clientConfiguration){
        this.clientConfiguration = clientConfiguration;
    }

    public void sendJob (SignatureJob signatureJob){
        directJobResponse = directClient.create((DirectJob)signatureJob);

    }

    public void getXades()


}

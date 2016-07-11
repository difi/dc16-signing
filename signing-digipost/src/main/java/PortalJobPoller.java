import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJobStatusChanged;

public class PortalJobPoller {
    private PortalClient client;

    PortalJobPoller(PortalClient portalClient){
        this.client = portalClient;

    }

    public String poll(){
        PortalJobStatusChanged statusChange =  client.getStatusChange();
        return statusChange.getStatus().toString();
    }



}

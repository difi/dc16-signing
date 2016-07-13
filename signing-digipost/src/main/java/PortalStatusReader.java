import no.digipost.signature.client.portal.PortalClient;

/**
 * Unused for now, portals only support polling.
 */
public class PortalStatusReader {
    private PortalClient client;

    PortalStatusReader(PortalClient portalClient){
        this.client = portalClient;
    }

    public String getStatus(){
        return "";
    }



}

import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.portal.SignatureStatus;

public class PortalJobPoller {
    private PortalClient client;
    private PortalJobStatusChanged statusChange;

    PortalJobPoller(PortalClient portalClient){
        this.client = portalClient;

    }

    public String poll(){
        this.statusChange =  client.getStatusChange();
        return statusChange.getStatus().toString();
    }

    public boolean isPadesReady(){
        if(hasPolled() ){
            if(statusChange.isPAdESAvailable()){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isXadesReady(){
        if(hasPolled()){
            if(statusChange.getSignatures() != null){
                return statusChange.getSignatures().stream().anyMatch(x -> x.is(SignatureStatus.SIGNED));
            }
        } else {
            return false;
        }
        return false;
    }

    public boolean hasPolled(){
        if(this.statusChange != null){
            return true;
        } else {
            return false;
        }
    }

    public PortalJobStatusChanged getStatusChange(){
        if(hasPolled()){
            return this.statusChange;
        } else {
            return null;
        }
    }



}

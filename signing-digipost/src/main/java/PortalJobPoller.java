import no.digipost.signature.client.core.exceptions.TooEagerPollingException;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.portal.SignatureStatus;

public class PortalJobPoller {
    private PortalClient client;
    private PortalJobStatusChanged statusChange;

    PortalJobPoller(PortalClient portalClient){
        this.client = portalClient;

    }

    //Gets the PortalJobStatusChanged object, should only be called once. Polling exception lasts for several minutes if it is called twice.
    public String poll(){
        try {
            this.statusChange =  client.getStatusChange();
            return statusChange.getStatus().toString();
        }catch (TooEagerPollingException eagerPollingException){
            String nextAvailablePollingTime = eagerPollingException.getNextPermittedPollTime().toString();
            System.out.print(nextAvailablePollingTime);
            return "Too frequent polling, please wait until " + nextAvailablePollingTime;
        }



    }

    //Checks whether a pades is available. For portals it is only available when all signers have signed.
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

    //Xades can be requested as long as at least one of the signers have signed.
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

    //Avoiding hitting the polling exception by polling only once
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

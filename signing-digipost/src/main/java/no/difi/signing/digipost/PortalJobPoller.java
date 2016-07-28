package no.difi.signing.digipost;

import no.digipost.signature.client.core.exceptions.TooEagerPollingException;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.portal.SignatureStatus;

public class PortalJobPoller {
    private PortalClient client;
    private PortalJobStatusChanged statusChange;

    PortalJobPoller(PortalClient portalClient) {
        this.client = portalClient;
    }
    /**
     * Polls the signingservice for the signingstatus of a portal signing job. Can only be polled every 10 minutes.
     */
    public String poll() {
        try {
            if(this.client != null){
                this.statusChange =  client.getStatusChange();
                return statusChange.getStatus().toString();
            }
            else {
                return "Client must be initialized";
            }
        } catch (TooEagerPollingException eagerPollingException) {
            String nextAvailablePollingTime = eagerPollingException.getNextPermittedPollTime().toString();
            System.out.print(nextAvailablePollingTime);
            return "Too frequent polling, please wait until " + nextAvailablePollingTime;
        }
    }

    /**
     * Checks whether a pades is available. For portals it is only available when all signers have signed.
     */
    public boolean isPadesReady() {
        if (hasPolled()) {
            if (statusChange.isPAdESAvailable()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Xades can be requested as long as at least one of the signers have signed.
     */
    public boolean isXadesReady() {
        if (hasPolled()) {
            if (statusChange.getSignatures() != null) {
                return statusChange.getSignatures().stream().anyMatch(x -> x.is(SignatureStatus.SIGNED));
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Avoiding hitting the polling exception by polling only once
     */
    public boolean hasPolled() {
        if (this.statusChange != null) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Returns new status if the status has changed.
     */
    public PortalJobStatusChanged getStatusChange() {
        if (hasPolled()) {
            return this.statusChange;
        } else {
            return null;
        }
    }


}

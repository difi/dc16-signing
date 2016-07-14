import no.digipost.signature.client.core.SignatureJob;

/**
 * Created by camp-mlo on 11.07.2016.
 */
public class SignatureJobModel {
    public int id;
    public String status;
    public String sender;
    public String  signer;

    public SignatureJobModel(String status, String sender, String signer){
        this.status = status;
        this.sender = sender;
        this.signer = signer;
    }

    public String getSender() {
        return sender;
    }

    public String getSigner() {
        return signer;
    }

    public String getStatus() {
        return status;
    }

    public void updateStatus(String value){
        this.status = value;

    }

}


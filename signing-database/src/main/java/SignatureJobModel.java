/**
 * Created by camp-mlo on 11.07.2016.
 */
public class SignatureJobModel {
    public int id;
    public String status;
    public String sender;
    public String signer;
    public String senderPid;

    public SignatureJobModel(String status, String sender, String signer, String senderPid) {
        this.status = status;
        this.sender = sender;
        this.signer = signer;
        this.senderPid = senderPid;
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

    public String getSenderPid() { return senderPid;}

    public void updateStatus(String value) {
        this.status = value;

    }

}


import no.digipost.signature.client.core.SignatureJob;

/**
 * Created by camp-mlo on 11.07.2016.
 */
public class SignatureJobModel {
    public int id;
    public String status;
    public String sender;
    public String  signer;

    public SignatureJobModel(SignatureDatabase db){
        this.status = "Ikke signert";
        this.sender = "123456789";
        this.signer = "17079493538";
        db.insertSignature(status, signer, sender, "doc");
    }

    public int getId() {
        return id;
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

    public void updateStatus(SignatureDatabase db, String status){
        db.updateValue(this.sender, "status", status);
    }

}


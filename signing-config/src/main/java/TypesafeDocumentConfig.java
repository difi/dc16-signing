/**
 * Created by camp-nto on 27.07.2016.
 */
import com.typesafe.config.Config;

import java.util.List;
import java.util.UUID;

public class TypesafeDocumentConfig {
    private String signer;
    private String sender;
    private List<String> signers;
    private String email;
    private String relativeDocumentPath;
    private String id;

    public String getId() {
        return id;
    }

    public TypesafeDocumentConfig(Config documentConfig){
        System.out.println(documentConfig.entrySet().toString());
        this.id = UUID.randomUUID().toString();

        this.signer = documentConfig.getString("signer");
        this.sender = documentConfig.getString("sender");
        this.signers = documentConfig.getStringList("signers");
        this.email = documentConfig.getString("e_mail");
        this.relativeDocumentPath = documentConfig.getString("relativeDocumentPath");

        System.out.println(this.signer);
        System.out.println(this.sender);
        System.out.println(this.signers.toString());
        System.out.println(this.email);
    }

    public String getSigner() {
        return signer;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getSigners() {
        return signers;
    }

    public String getEmail() {
        return email;
    }

    public String getRelativeDocumentPath() {
        return relativeDocumentPath;
    }
}
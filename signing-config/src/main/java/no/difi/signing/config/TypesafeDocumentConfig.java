package no.difi.signing.config;

import com.typesafe.config.Config;

import java.util.List;
import java.util.UUID;

public class TypesafeDocumentConfig {
    private String signer;
    private String sender;
    private List<String> signers;
    private String email;
    private String id;

    public String getId() {
        return id;
    }

    public TypesafeDocumentConfig(Config documentConfig) {
        System.out.println(documentConfig.entrySet().toString());
        this.id = UUID.randomUUID().toString();

        this.signer = documentConfig.getString("signer");
        this.sender = documentConfig.getString("sender");
        this.signers = documentConfig.getStringList("signers");
        this.email = documentConfig.getString("e_mail");

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

}
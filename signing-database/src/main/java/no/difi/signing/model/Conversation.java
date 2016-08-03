package no.difi.signing.model;

import no.difi.signing.api.ConversationStub;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Conversation implements ConversationStub {

    public static Conversation newInstance() {
        Conversation conversation = new Conversation();
        conversation.setIdentifier(UUID.randomUUID().toString());
        conversation.setTimestamp(new Date());
        return conversation;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String identifier;

    private String pid;

    private String documentToken;
    private String redirectUri;

    private long digipostSignatureJobId;
    private String digipostRedirectUrl;
    private String digipostStatusUrl;

    private Date timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDocumentToken() {
        return documentToken;
    }

    public void setDocumentToken(String documentToken) {
        this.documentToken = documentToken;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public long getDigipostSignatureJobId() {
        return digipostSignatureJobId;
    }

    @Override
    public void setDigipostSignatureJobId(long digipostSignatureJobId) {
        this.digipostSignatureJobId = digipostSignatureJobId;
    }

    @Override
    public String getDigipostRedirectUrl() {
        return digipostRedirectUrl;
    }

    @Override
    public void setDigipostRedirectUrl(String digipostRedirectUrl) {
        this.digipostRedirectUrl = digipostRedirectUrl;
    }

    @Override
    public String getDigipostStatusUrl() {
        return digipostStatusUrl;
    }

    @Override
    public void setDigipostStatusUrl(String digipostStatusUrl) {
        this.digipostStatusUrl = digipostStatusUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

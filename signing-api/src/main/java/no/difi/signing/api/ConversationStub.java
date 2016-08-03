package no.difi.signing.api;

public interface ConversationStub {

    String getIdentifier();

    long getDigipostSignatureJobId();

    void setDigipostSignatureJobId(long digipostSignatureJobId);

    String getDigipostRedirectUrl();

    void setDigipostRedirectUrl(String digipostRedirectUrl);

    String getDigipostStatusUrl();

    void setDigipostStatusUrl(String digipostStatusUrl);

}

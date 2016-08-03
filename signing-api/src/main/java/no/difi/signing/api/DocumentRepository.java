package no.difi.signing.api;

import no.difi.signing.lang.SigningException;

import java.util.List;

public interface DocumentRepository {

    Document findByToken(String token) throws SigningException;

    List<Document> allDocuments();

}

package no.difi.signing.api;

import java.util.List;

public interface DocumentRepository {

    Document findByToken(String token);

    List<Document> allDocuments();

}

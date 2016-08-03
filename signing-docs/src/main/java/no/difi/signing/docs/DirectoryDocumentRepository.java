package no.difi.signing.docs;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.lang.SigningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableAutoConfiguration
@SuppressWarnings("unused")
public class DirectoryDocumentRepository implements DocumentRepository {

    @Autowired
    @Qualifier("docs")
    private Path docsPath;

    private Map<String, Document> documentMap;

    @PostConstruct
    public void postConstruct() throws IOException {
        documentMap = Files.walk(docsPath)
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(".conf"))
                .filter(p -> Files.exists(p.getParent().resolve(p.toFile().getName().replace(".conf", ".pdf"))))
                .map(DirectoryDocument::new)
                .collect(Collectors.toMap(Document::getToken, Function.identity()));
    }

    @Override
    public Document findByToken(String token) throws SigningException {
        if (!documentMap.containsKey(token))
            throw new SigningException(String.format("Document '%s' not found.", token));

        return documentMap.get(token);
    }

    @Override
    public List<Document> allDocuments() {
        return new ArrayList<>(documentMap.values());
    }
}

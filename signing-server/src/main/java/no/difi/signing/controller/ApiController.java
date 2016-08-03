package no.difi.signing.controller;

import com.google.common.io.ByteStreams;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.lang.SigningException;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    @Qualifier("storage")
    private Path storagePath;

    @RequestMapping("/pid/{pid}")
    @ResponseBody
    public List<Signature> findByPid(@PathVariable String pid) {
        return signatureRepository.findByPid(pid);
    }

    @RequestMapping("/sign/{identifier}")
    @ResponseBody
    public Signature viewSignature(@PathVariable String identifier) throws SigningException {
        return getSignature(identifier);
    }

    @RequestMapping("/sign/{identifier}/pades")
    @ResponseBody
    public void viewSignaturePades(@PathVariable String identifier, HttpServletResponse response)
            throws IOException, SigningException {
        Signature signature = getSignature(identifier);

        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s-pades-%s.pdf\"", identifier, signature.getDocumentToken()));
        try (InputStream inputStream = Files.newInputStream(storagePath.resolve(identifier).resolve("pades.pdf"))) {
            ByteStreams.copy(inputStream, response.getOutputStream());
        }
    }

    @RequestMapping("/sign/{identifier}/xades")
    @ResponseBody
    public void viewSignatureXades(@PathVariable String identifier, HttpServletResponse response)
            throws IOException, SigningException {
        Signature signature = getSignature(identifier);

        response.setHeader("Content-Type", "application/xml");
        response.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s-xades-%s.xml\"", identifier, signature.getDocumentToken()));
        try (InputStream inputStream = Files.newInputStream(storagePath.resolve(identifier).resolve("xades.xml"))) {
            ByteStreams.copy(inputStream, response.getOutputStream());
        }
    }

    @RequestMapping("/tag/{tag}")
    @ResponseBody
    public List<Signature> findByTag(@PathVariable String tag) {
        return signatureRepository.findByTag(tag);
    }

    @RequestMapping("/doc")
    @ResponseBody
    public List<String> listDocs() {
        return documentRepository.allDocuments().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/doc/{token}", produces = "application/pdf")
    @ResponseBody
    public void getDoc(@PathVariable String token, HttpServletResponse response) throws IOException, SigningException {
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"%s.pdf\"", token));
        ByteStreams.copy(documentRepository.findByToken(token).getInputStream(), response.getOutputStream());
    }

    private Signature getSignature(String identifier) throws SigningException {
        return Optional.ofNullable(signatureRepository.findByIdentifier(identifier))
                .orElseThrow(() -> new SigningException(String.format("Signature '%s' not found.", identifier)));
    }
}

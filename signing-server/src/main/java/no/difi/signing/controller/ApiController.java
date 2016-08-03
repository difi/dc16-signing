package no.difi.signing.controller;

import com.google.common.io.ByteStreams;
import no.difi.signing.api.DocumentRepository;
import no.difi.signing.lang.SigningException;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/pid/{pid}")
    @ResponseBody
    public List<Signature> findByPid(@PathVariable String pid) {
        return signatureRepository.findByPid(pid);
    }

    @RequestMapping("/sign/{identifier}")
    @ResponseBody
    public Signature viewSignature(@PathVariable String identifier) {
        return signatureRepository.findByIdentifier(identifier);
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
}

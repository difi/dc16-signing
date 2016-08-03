package no.difi.signing.controller;

import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SignatureRepository signatureRepository;

    @RequestMapping("/pid/{pid}")
    @ResponseBody
    public List<Signature> findByPid(@PathVariable String pid) {
        return signatureRepository.findByPid(pid);
    }

    @RequestMapping("/signature/{identifier}")
    @ResponseBody
    public Signature viewSignature(@PathVariable String identifier) {
        return signatureRepository.findByIdentifier(identifier);
    }

    @RequestMapping("/tag/{tag}")
    @ResponseBody
    public List<Signature> findByTag(@PathVariable String tag) {
        return signatureRepository.findByTag(tag);
    }
}

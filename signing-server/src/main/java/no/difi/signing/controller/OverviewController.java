package no.difi.signing.controller;

import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OverviewController {

    @Autowired
    private SignatureRepository signatureRepository;

    @RequestMapping("/overview")
    @ResponseBody
    public String viewSignatures(){

       String signatures =  signatureRepository.findAll().iterator().toString();
        return signatures;

    }

}

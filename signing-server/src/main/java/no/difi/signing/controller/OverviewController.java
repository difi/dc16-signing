package no.difi.signing.controller;

import com.google.common.collect.Lists;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class OverviewController {

    @Autowired
    private SignatureRepository signatureRepository;

    @RequestMapping("/overview")
    @ResponseBody
    public ArrayList<Signature> viewSignatures(){
        return Lists.newArrayList(signatureRepository.findAll());

    }

}

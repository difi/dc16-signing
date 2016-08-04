package no.difi.signing.controller;

import no.difi.signing.lang.SigningException;
import no.difi.signing.model.Signature;
import no.difi.signing.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OverviewController {

    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping("/overview")
    public String viewSignatures(ModelMap modelMap) throws SigningException {
        modelMap.put("signatures", signatureRepository.findByPid(httpServletRequest.getHeader("X-DifiProxy-pid")));

        return "overview";
    }
}

package no.difi.signing.spring;

/**
 * Class description: Makes the html-document "index.html" appear on localhost:8080
 */
import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;


@EnableAutoConfiguration
@Controller
public class IndexController {
    public String senderPid;

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }

    @RequestMapping(value = "/docs")
    @ResponseBody
    public List<String> getDocsList() {
        return documentRepository.allDocuments().stream().map(Document::toString).collect(Collectors.toList());
    }

    @RequestMapping("/simulertLogin")
    public String goToLogin(){ return "simulertLogin";}

}

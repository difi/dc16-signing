package no.difi.signing.controller;

import no.difi.signing.api.Document;
import no.difi.signing.api.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description: Makes the html-document "index.html" appear on localhost:8080
 */
@EnableAutoConfiguration
@Controller
public class IndexController {

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/")
    public String getHomePage(ModelMap modelMap) {
        modelMap.put("name", "Test");
        return "index";
    }

    @RequestMapping(value = "/docs")
    @ResponseBody
    public List<String> getDocsList() {
        return documentRepository.allDocuments().stream().map(Document::toString).collect(Collectors.toList());
    }
}

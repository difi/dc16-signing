package no.difi.signing.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class description: Makes the html-document "index.html" appear on localhost:8080
 */
@EnableAutoConfiguration
@Controller
public class IndexController {

    @RequestMapping("/")
    public String getHomePage() {
        return "index";
    }
}

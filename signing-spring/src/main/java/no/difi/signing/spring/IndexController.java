package no.difi.signing.spring;

/**
 * Class description: Makes the html-document "index.html" appear on localhost:8080
 */
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@EnableAutoConfiguration
@Controller

public class IndexController {
    public String senderPid;

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }

    @RequestMapping("/simulertLogin")
    public String goToLogin(){ return "simulertLogin";}

}
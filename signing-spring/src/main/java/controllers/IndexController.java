package controllers;

/**
 * Controller: Making the html-document "index.html" appear on localhost:8080
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableAutoConfiguration
@Controller
public class IndexController {

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }
}

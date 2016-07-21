package controllers;

/**
 * Class description: Makes the html-document "index.html" appear on localhost:8080
 */
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@EnableAutoConfiguration
@Controller
@RestController
public class IndexController {

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }

    @RequestMapping("/simulertLogin")
    public String goToLogin(){ return "simulertLogin";}

    @RequestMapping("/pid")
    public String getRequest(HttpServletRequest request, HttpServletRequest response) {
        System.out.println("HEST: " + request.getHeader("X-DifiProxy-pid"));

        return "";
    }
}

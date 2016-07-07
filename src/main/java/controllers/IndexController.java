package controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by camp-nto on 07.07.2016.
 */

@EnableAutoConfiguration
@Controller
public class IndexController {

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IndexController.class, args);
    }
}

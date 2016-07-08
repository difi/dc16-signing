import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by camp-nto on 07.07.2016.
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String getHomePage(){
        return "index";
    }

}
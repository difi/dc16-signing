import controllers.IndexController;
import org.springframework.boot.SpringApplication;

/**
 * Created by camp-nto on 08.07.2016.
 */
public class ApplicationMain {

    public static void main(String[] args) throws Exception {
        Object[] sources = {DigipostSpringConnector.class, controllers.IndexController.class};
        SpringApplication.run(sources, args);
    }
}

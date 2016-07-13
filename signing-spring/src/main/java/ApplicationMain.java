import controllers.IndexController;
import org.springframework.boot.SpringApplication;

/**
 * Class description: Starts Spring Boot-application. Runs controllers
 */
public class ApplicationMain {

    public static void main(String[] args) throws Exception {
        Object[] sources = {DigipostSpringConnector.class, controllers.IndexController.class, PortalController.class};
        SpringApplication.run(sources, args);
    }

    //DirectSignatureJobResponse.getStatusURL
    //Bruk bibliotek, send status-url og query-token
}

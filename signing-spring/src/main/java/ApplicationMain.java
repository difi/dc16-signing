import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import no.difi.idporten.oidc.proxy.config.ConfigModule;
import no.difi.idporten.oidc.proxy.proxy.NettyHttpListener;
import no.difi.idporten.oidc.proxy.proxy.ProxyModule;
import no.difi.idporten.oidc.proxy.storage.StorageModule;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;

/**
 * Class description: Starts Spring Boot-application. Runs controllers
 */
public class ApplicationMain {


    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ArrayList<Module>() {{
            add(new ConfigModule());
            add(new StorageModule());
            add(new ProxyModule());
        }});



        Object[] sources = {DigipostSpringConnector.class, controllers.IndexController.class, PortalController.class};

        System.out.println("HEISANN: " );
        SpringApplication.run(sources, args);
        injector.getInstance(NettyHttpListener.class).run();

    }

    //DirectSignatureJobResponse.getStatusURL
    //Bruk bibliotek, send status-url og query-token
}

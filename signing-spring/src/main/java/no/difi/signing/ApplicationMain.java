package no.difi.signing;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import no.difi.idporten.oidc.proxy.config.ConfigModule;
import no.difi.idporten.oidc.proxy.proxy.NettyHttpListener;
import no.difi.idporten.oidc.proxy.proxy.ProxyModule;
import no.difi.idporten.oidc.proxy.storage.StorageModule;
import no.difi.signing.docs.DirectoryDocumentRepository;
import no.difi.signing.spring.ApplicationConfiguration;
import no.difi.signing.spring.DigipostSpringConnector;
import no.difi.signing.spring.IndexController;
import no.difi.signing.spring.PortalController;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;

/**
 * Class description: Starts Spring Boot-application. Runs no.difi.signing.spring.controllers
 */
public class ApplicationMain {


    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ArrayList<Module>() {{
            add(new ConfigModule());
            add(new StorageModule());
            add(new ProxyModule());
        }});



        Object[] sources = {DigipostSpringConnector.class, IndexController.class, PortalController.class, DirectoryDocumentRepository.class, ApplicationConfiguration.class};

        System.out.println("HEISANN: " );
        SpringApplication.run(sources, args);
        injector.getInstance(NettyHttpListener.class).run();

    }

    //DirectSignatureJobResponse.getStatusURL
    //Bruk bibliotek, send status-url og query-token
}
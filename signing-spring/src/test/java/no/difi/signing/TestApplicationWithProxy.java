package no.difi.signing;

import com.google.inject.Guice;
import com.google.inject.Module;
import no.difi.idporten.oidc.proxy.config.ConfigModule;
import no.difi.idporten.oidc.proxy.proxy.NettyHttpListener;
import no.difi.idporten.oidc.proxy.proxy.ProxyModule;
import no.difi.idporten.oidc.proxy.storage.StorageModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TestApplicationWithProxy {

    @Test(groups = "manual")
    public void run() throws Exception {
        SpringApplication.run(TestApplicationWithProxy.class);

        Guice.createInjector(new ArrayList<Module>() {{
            add(new ConfigModule());
            add(new StorageModule());
            add(new ProxyModule());
        }}).getInstance(NettyHttpListener.class).run();
    }
}
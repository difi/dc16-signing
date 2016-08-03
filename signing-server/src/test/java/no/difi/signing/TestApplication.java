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
public class TestApplication {

    @Test(groups = "manual")
    public void runStandalone() throws Exception {
        SpringApplication.run(TestApplication.class);

        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        };
        thread.start();
        thread.join();
    }

    @Test(groups = "manual")
    public void runWithProxy() throws Exception {

        SpringApplication.run(TestApplication.class);

        Guice.createInjector(new ArrayList<Module>() {{
            add(new ConfigModule());
            add(new StorageModule());
            add(new ProxyModule());
        }}).getInstance(NettyHttpListener.class).run();
    }
}
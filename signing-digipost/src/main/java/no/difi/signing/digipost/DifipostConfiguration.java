package no.difi.signing.digipost;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(name = "keystore.password", value = "password")
})
public class DifipostConfiguration {
}

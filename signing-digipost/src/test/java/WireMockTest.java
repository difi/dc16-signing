import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.digipost.signature.client.Certificates;
import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.ServiceUri;
import no.digipost.signature.client.core.Sender;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Before;
import org.junit.Rule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by camp-mlo on 14.07.2016.
 */
public class WireMockTest {
    private static HttpClient httpClient;
    private final String BASEURL = "http://localhost:8080";
    private final String portalUrl = "/%s/portal/signature-jobs";
    private final String directUrl = "/%s/direct/signature-jobs";

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.wireMockConfig().port(8082));


    @Before
    public void setUp(){
      httpClient = HttpClientBuilder.create().build();
        System.out.print("yolo");

      configureFor(8082);
      stubFor(get(urlMatching("/test"))
                .willReturn(aResponse()
                    .withStatus(12345)
                    .withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/plain")
                    .withBody("")));
        stubFor(get(urlMatching(BASEURL + portalUrl))
                .willReturn(aResponse()
                        .withStatus(12345)
                        .withHeader("yolo", "lol")
                        .withBody("HELLOOOOO - portal")));


  }

}

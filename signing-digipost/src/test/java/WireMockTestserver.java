import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.digipost.signature.client.portal.PortalClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Rule;
import org.testng.annotations.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

/**
 * Created by camp-mlo on 14.07.2016.
 */
public class WireMockTestserver {

    private static HttpClient httpClient;
    private final String BASEURL = "http://localhost:8080";
    private final String portalUrl = "/%s/portal/signature-jobs";
    private final String directUrl = "/%s/direct/signature-jobs";

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.wireMockConfig().port(8082));


    @BeforeClass
    public void setUp(){
        httpClient = HttpClientBuilder.create().build();

        configureFor(8082);
        stubFor(get(urlMatching(BASEURL + directUrl))
                .willReturn(aResponse()
                        .withStatus(12345)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/plain")
                        .withBody("")));
        stubFor(get(urlPathMatching(BASEURL + portalUrl))
                .willReturn(aResponse()
                        .withStatus(12345)
                        .withHeader("yolo", "lol")
                        .withBody("HELLOOOOO - portal")));
    }



    public static void main(String args[]){
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8082));
        wireMockServer.start();
        System.out.println("Server started");

        WireMock.configureFor(8082);
        System.out.println("Client configured");
/*
        WireMock.stubFor(WireMock.get(WireMock.anyUrl())
                .willReturn(WireMock.aResponse()
                        .withBody("DPA")));*/
        System.out.println("Server has config");
        System.out.println(wireMockServer.listAllStubMappings().getMappings());
    }
}

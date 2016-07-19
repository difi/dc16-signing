import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.ByteStreams;
import no.digipost.signature.client.direct.DirectJobResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.BeforeClass;
import org.junit.Rule;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by camp-mlo on 14.07.2016.
 */
public class MockServer {

    private static HttpClient httpClient;
    private final String BASEURL = "http://localhost:8080";
    private final String portalUrl = "/%s/portal/signature-jobs";
    private final String directUrl = "/%s/direct/signature-jobs";

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.wireMockConfig().port(8082));


    @BeforeClass
    public static void setUp() throws IOException {
        httpClient = HttpClientBuilder.create().build();
        String directUrl = ".*/direct/signature-jobs";
        String portalUrl = ".*/portal/signature-jobs";
        String statusUrl = ".*/direct/signature-jobs/1/status";
        String padesUrl = ".*/direct/signature-jobs/1/pades";
        String xadesUrl = ".*/direct/signature-jobs/1/xades/1";
        String confirmationUrl = ".*/direct/signature-jobs/1/complete";

        String filePath = new File("").getAbsolutePath();
        System.out.print(filePath);

        DirectJobResponse sampleJobResponse = getSampleSignatureJob();
        //XMLDirectSignatureJobResponse xmlSample = toJaxb();
        configureFor(8082);

        stubFor(post(urlMatching(directUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
                        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/JobResponse.xml")))));
        stubFor(post(urlPathMatching(portalUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
                        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/PortalJobResponse.xml")))));
        stubFor(get(urlPathMatching(statusUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
                        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/StatusResponse.xml")))));

        stubFor(get(urlPathMatching(padesUrl))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/pdf")
                        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/pAdES.pdf")))));

        stubFor(get(urlPathMatching(xadesUrl))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
                        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/xAdES.xml")))));

    }

    public static DirectJobResponse getSampleSignatureJob(){
        return new DirectJobResponse(5, "redirect url", "status url");
    }


    public static void main(String args[]) throws IOException {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8082));
        wireMockServer.start();
        setUp();

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
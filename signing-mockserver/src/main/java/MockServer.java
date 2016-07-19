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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

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
        String statusUrl = ".*/direct/signature-jobs";
        String padesUrl = ".*/direct/signature-jobs/1/pades";
        String xadesUrl = ".*/direct/signature-jobs/1/xades/1";
        String cancellationURL = ".*/portal/signature-jobs/1/cancel";
        String confirmationUrl = ".*/direct/signature-jobs/1/complete";
        String padesPortalURL = ".*/portal/signature-jobs/1/pades";
        String confirmationPortalURL = ".*/portal/signature-jobs/1/complete";

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
        stubFor(get(urlPathMatching(portalUrl))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
        .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/PortalPollResponse.xml")))));

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

        stubFor(get(urlPathMatching(cancellationURL))
        .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
        .withBody("cancelled")));


        stubFor(get(urlPathMatching(padesPortalURL))
                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/pdf")
                .withBody(ByteStreams.toByteArray(MockServer.class.getResourceAsStream("__files/pAdESPortal.pdf")))));

        stubFor(post(urlPathMatching(confirmationPortalURL))
                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
                .withBody("confirmed")));


        //En http-post mot ressurs. for å opprette signeringsoppdrag
        //Metadata legges i multipart-kallet med application/xml?

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

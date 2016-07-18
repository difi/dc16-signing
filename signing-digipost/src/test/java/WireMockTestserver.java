import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.digipost.signature.api.xml.XMLDirectSignatureJobResponse;
import no.digipost.signature.client.direct.DirectJobResponse;
import no.digipost.signature.client.portal.PortalClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Rule;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;

import java.io.ByteArrayInputStream;
import java.io.File;

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
    public static void setUp(){
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

        DirectJobResponse sampleJobResponse = getSampleSignatureJob();
        //XMLDirectSignatureJobResponse xmlSample = toJaxb();
        configureFor(8082);
        stubFor(post(urlPathMatching(directUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
                        .withBodyFile("JobResponse.xml")));
        stubFor(post(urlPathMatching(portalUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
                        //.withBody(String.valueOf(new ByteArrayInputStream(new byte[]{0x03, 0x04})).getBytes())
                        .withBodyFile("PortalJobResponse.xml")));
        stubFor(get(urlPathMatching(portalUrl))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
        .withBodyFile("PortalStatusChangeResponse.xml")));

        stubFor(get(urlPathMatching(statusUrl))
        .willReturn(aResponse()
        .withStatus(200)
        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
        .withBodyFile("StatusResponse.xml")));

        stubFor(get(urlPathMatching(padesUrl))
        .willReturn(aResponse()
                .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/pdf")
        .withBodyFile("pAdES.pdf")));

        stubFor(get(urlPathMatching(xadesUrl))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(),"application/xml")
                        .withBodyFile("xAdES.xml")));

        stubFor(get(urlPathMatching(cancellationURL))
        .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
        .withBody("cancelled")));


        stubFor(get(urlPathMatching(padesPortalURL))
                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/pdf")
                .withBodyFile("pAdESPortal.pdf")));

        stubFor(post(urlPathMatching(confirmationPortalURL))

                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
                .withBody("confirmed")));


        //En http-post mot ressurs. for å opprette signeringsoppdrag
        //Metadata legges i multipart-kallet med application/xml?






        ;}

    public static DirectJobResponse getSampleSignatureJob(){
        return new DirectJobResponse(5, "redirect url", "status url");
    }






    public static void main(String args[]){
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

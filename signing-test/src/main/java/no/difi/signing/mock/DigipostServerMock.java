package no.difi.signing.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.common.io.ByteStreams;
import org.eclipse.jetty.http.HttpHeader;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class DigipostServerMock {

    private WireMockServer wireMockServer;

    private int port;

    public DigipostServerMock(int port) {
        this.port = port;
    }

    public DigipostServerMock() {
        this(8082);
    }

    public void start() throws IOException {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(port));
        wireMockServer.start();

        wireMockServer.stubFor(post(urlMatching(".*/direct/signature-jobs"))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
                        .withBody(ByteStreams.toByteArray(getClass().getResourceAsStream("/mockfiles/JobResponse.xml")))));

        wireMockServer.stubFor(get(urlPathMatching(".*/direct/signature-jobs"))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
                        .withBody(ByteStreams.toByteArray(getClass().getResourceAsStream("/mockfiles/StatusResponse.xml")))));

        wireMockServer.stubFor(get(urlPathMatching(".*/direct/signature-jobs/1/pades"))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/pdf")
                        .withBody(ByteStreams.toByteArray(getClass().getResourceAsStream("/mockfiles/pAdES.pdf")))));


        wireMockServer.stubFor(get(urlPathMatching(".*/direct/signature-jobs/1/xades/1"))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE.toString(), "application/xml")
                        .withBody(ByteStreams.toByteArray(getClass().getResourceAsStream("/mockfiles/xAdES.xml")))));

        wireMockServer.stubFor(get(urlPathMatching(".*/portal/signature-jobs/1/cancel"))
                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
                        .withBody("cancelled")));

        wireMockServer.stubFor(get(urlPathMatching(".*/portal/signature-jobs/1/complete"))
                .willReturn(aResponse().withHeader(HttpHeader.CONTENT_TYPE.toString(), "text/html")
                        .withBody("completed")));

    }

    public void shutDown() {
        wireMockServer.shutdownServer();
    }
}

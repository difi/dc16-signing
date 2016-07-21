import no.digipost.signature.client.ClientConfiguration;
import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import no.digipost.signature.client.portal.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by camp-nto on 18.07.2016.
 */
public class PortalControllerTest {

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();
    @Test
    public void getPortalXades_returns_getXades() throws IOException {
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);

        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);;
        Assert.assertEquals(portalController.getPortalXades(), null);
    }

    @Test
    public void getPortalXades_returnes_getXades_if_signedDocumentFetcher_is_null() throws IOException {
        PortalController portalController = new PortalController();

        PortalJobPoller portalJobPoller = Mockito.mock(PortalJobPoller.class);
        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setPortalJobPoller(portalJobPoller);
        portalController.setSigningServiceConnector(signingServiceConnector);

        Assert.assertEquals(portalController.getPortalXades(), "no xades available");
    }

    @Test
    public void getPortalPades_returns_getPades() throws IOException{
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);

        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);
        Assert.assertEquals(portalController.getPortalPades(), null);
    }

    @Test
    public void getPortalPades_returnes_getPades_if_signedDocumentFetcher_is_null() throws IOException {
        PortalController portalController = new PortalController();

        PortalJobPoller portalJobPoller = Mockito.mock(PortalJobPoller.class);
        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setPortalJobPoller(portalJobPoller);
        portalController.setSigningServiceConnector(signingServiceConnector);

        Assert.assertEquals(portalController.getPortalPades(), "pades not ready or failed");
    }

    @Test
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_not_null() throws IOException {
        PortalController portalController = new PortalController();

        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setSigningServiceConnector(signingServiceConnector);
        portalController.startPortalJob();

    }

    @Test
    public void poll_returns_status(){
        PortalController portalController = new PortalController();
        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setSigningServiceConnector(signingServiceConnector);

        PortalJobPoller portalJobPoller = mock(PortalJobPoller.class);
        when(portalJobPoller.poll()).thenReturn("polled");

        Assert.assertEquals(portalJobPoller.poll(), "polled");
    }

}

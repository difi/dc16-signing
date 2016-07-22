import no.digipost.signature.client.portal.PortalJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;

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
        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);
        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);

        Assert.assertEquals(portalController.getPortalXades(), null);
    }

    @Test
    public void getPortalPades_returns_getPades() throws IOException{
        PortalController portalController = new PortalController();

        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);
        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);

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
        PortalSignedDocumentFetcher portalSignedDocumentFetcher = Mockito.mock(PortalSignedDocumentFetcher.class);
        portalController.setPortalSignedDocumentFetcher(portalSignedDocumentFetcher);

        Assert.assertEquals(portalController.getPortalPades(), null);
    }

    @Test
    public void startPortalJob_sendsPortalRequest_if_signingSerViceConnector_not_null() throws IOException {
        PortalController portalController = new PortalController();
        PortalJob portalJob = mock(PortalJob.class);
        KeyStoreConfig keyStoreConfig = mock(KeyStoreConfig.class);

        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setSigningServiceConnector(signingServiceConnector);
        //signingServiceConnector.sendPortalRequest(portalJob, keyStoreConfig)).thenReturn(true);

    }

/*    @Test
    public void poll_returns_status(){
        PortalController portalController = new PortalController();
        SigningServiceConnector signingServiceConnector = Mockito.mock(SigningServiceConnector.class);
        portalController.setSigningServiceConnector(signingServiceConnector);

        PortalJobPoller portalJobPoller = mock(PortalJobPoller.class);
        portalController.setPortalJobPoller(portalJobPoller);
        when(portalJobPoller.poll()).thenReturn("polled from portalJobPoller");

        String status = portalController.poll();

        Assert.assertEquals(portalController.poll(), "Client must be initialized");
    }*/

}

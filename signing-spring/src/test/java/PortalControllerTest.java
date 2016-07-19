import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

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

}

import no.digipost.signature.client.portal.PortalClient;
import no.digipost.signature.client.portal.PortalJobStatus;
import no.digipost.signature.client.portal.PortalJobStatusChanged;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortalJobPollerTest {
    @Mock
    private static PortalClient client;

    @Mock
    PortalJobStatusChanged statusChange;

    public static void setUp(){
        client = mock(PortalClient.class);
        PortalJobStatusChanged mockedStatusChanged = mock(PortalJobStatusChanged.class);
        when(client.getStatusChange().getStatus()).thenReturn(PortalJobStatus.IN_PROGRESS);
        when(mockedStatusChanged.getStatus().toString()).thenReturn("IN_PROGRESS");

    }

    public void testHasPolled(){

    }

    public void testHasNotPolled(){
        PortalJobPoller poller = new PortalJobPoller(mockedClient);
    }

    public void testPadesReady(){

    }

    public void testXadesReady(){

    }

    public void testPollStatus(){

    }


}

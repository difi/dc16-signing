package no.difi.signing.digipost;

import no.difi.signing.digipost.PortalJobPoller;
import no.digipost.signature.client.portal.*;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortalJobPollerTest {
    @Mock
    private static PortalClient client;

    @Mock
    private static PortalJobStatusChanged statusChange;

    @Mock
    private static PortalJobStatusChanged secondStatusChange;

    @Mock
    private static Signature signature;

    @Mock
    private static Signature secondSignature;


    @BeforeClass
    public static void setUp(){
        client = mock(PortalClient.class);
        statusChange = mock(PortalJobStatusChanged.class);
        secondStatusChange = mock(PortalJobStatusChanged.class);
        signature = mock(Signature.class);
        secondSignature = mock(Signature.class);
        //portalJobStatusInProgress = mock(PortalJobStatus.class);


        when(client.getStatusChange()).thenReturn(statusChange);
        when(statusChange.getStatus()).thenReturn(PortalJobStatus.IN_PROGRESS);
        when(statusChange.isPAdESAvailable()).thenReturn(false);
        when(secondStatusChange.isPAdESAvailable()).thenReturn(false);
        when(signature.is(SignatureStatus.SIGNED)).thenReturn(true);
        when(secondSignature.is(SignatureStatus.SIGNED)).thenReturn(false);

        List<Signature> signaturesList = new ArrayList<>();
        signaturesList.add(signature);
        List<Signature> secondSignaturesList = new ArrayList<>();
        signaturesList.add(secondSignature);

        when(statusChange.getSignatures()).thenReturn(signaturesList);
        when(secondStatusChange.getSignatures()).thenReturn(secondSignaturesList);

    }

    @Test
    public void testHasPolled(){
        PortalJobPoller poller = new PortalJobPoller(client);
        poller.poll();
        Assert.assertTrue(poller.hasPolled());
        PortalJobPoller portalJobPoller = Mockito.mock(PortalJobPoller.class);
    }

    @Test
    public void testHasNotPolled(){
        PortalJobPoller poller = new PortalJobPoller(client);
        Assert.assertFalse(poller.hasPolled());
    }

    @Test
    public void testPadesReady(){
        PortalJobPoller poller = new PortalJobPoller(client);
        poller.poll();
        Assert.assertFalse(poller.isPadesReady());
    }

    @Test
    public void testXadesReady(){
        PortalJobPoller poller = new PortalJobPoller(client);
        poller.poll();
        Assert.assertFalse(poller.isPadesReady());
    }

    @Test
    public void testPollStatusWhenNotPolled(){
        PortalJobPoller poller = new PortalJobPoller(client);
        Assert.assertNull(poller.getStatusChange());
    }

    @Test
    public void testPollStatusWhenPolled(){
        PortalJobPoller poller = new PortalJobPoller(client);
        poller.poll();
        Assert.assertEquals(statusChange,poller.getStatusChange());

    }


}

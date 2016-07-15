import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by camp-nto on 14.07.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DigipostSpringConnector.class)
@WebAppConfiguration

public class DigipostSpringConnectorTest {

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();;

        //@Before
        //public void setup() throws Exception{
        //    mvc = MockMvcBuilders.standaloneSetup(new DigipostSpringConnector()).build();
        //}

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello")))
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"));
    }

    @Test
    public void testMakeAsice_checksStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/asice"))
                .andExpect(status().isFound())
        ;
    }

    @Test
    public void testWhenSigningComplete_checksStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/onCompletion"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUserRejects_checksStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/onRejection"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testwhenSigningFails_checksStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/onError"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSignedDocument_checksStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getDocument"))
                .andExpect(status().isBadRequest());
    }

    //@Test
    //public void getPades_checksStatus() throws Exception {
    //    mvc.perform(MockMvcRequestBuilders.get("/getPades"))
    //            .andExpect(status().isBadRequest());
    //}



}
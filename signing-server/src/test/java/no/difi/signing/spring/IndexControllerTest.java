package no.difi.signing.spring;

import no.difi.signing.controller.IndexController;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;



public class IndexControllerTest {

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(new IndexController()).build();;

    @Test
    public void getHomePage_returns_index(){
        IndexController indexController = new IndexController();
        Assert.assertEquals(indexController.getHomePage(), "index");
    }

    @Test
    public void gotLogin_returns_simulertLogin() throws Exception {
       IndexController indexController = new IndexController();
        Assert.assertEquals(indexController.goToLogin(), "simulertLogin");
    }
}

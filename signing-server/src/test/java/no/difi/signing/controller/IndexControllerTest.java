package no.difi.signing.controller;

import org.testng.Assert;
import org.testng.annotations.Test;

public class IndexControllerTest {

    @Test
    public void getHomePage_returns_index(){
        IndexController indexController = new IndexController();
        Assert.assertEquals(indexController.getHomePage(), "index");
    }
}

import controllers.IndexController;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-nto on 21.07.2016.
 */
public class IndexControllerTest {

    @Test
    public void getHomePage_returns_index(){
        IndexController indexController = new IndexController();
        Assert.assertEquals(indexController.getHomePage(), "index");
    }
}

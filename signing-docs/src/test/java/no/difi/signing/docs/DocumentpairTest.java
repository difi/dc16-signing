package no.difi.signing.docs;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DocumentpairTest {

    @Test
    public void documentpairTest(){
        Documentpair pair = new Documentpair("document1");

        Assert.assertNotNull(pair);
    }
}

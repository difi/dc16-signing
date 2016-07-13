import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-mlo on 12.07.2016.
 */
public class SignatureDatabaseTest {
    public SignatureDatabase db = new SignatureDatabase();
    public SignatureJobModel signatureJobModel = new SignatureJobModel("Ikke signert", "987654321","11111111111");


    @Test
    public void creatingTable(){
        boolean tableCreated = db.createTable();

        Assert.assertTrue(tableCreated);
    }

    @Test
    public void insertingSignature() {
       Assert.assertNotNull(signatureJobModel);

        db.insertSignature(signatureJobModel);
        Assert.assertEquals(db.getSignatureJob(signatureJobModel) , "(1, Ikke signert, 11111111111, 987654321, document)");
    }

    @Test
    public void updatingStatus() {
        db.updateStatus(signatureJobModel, "Signert" );

        Assert.assertEquals(db.getSignatureJob(signatureJobModel), "(1, Signert, 11111111111, 987654321, document)" );
      }


}

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-mlo on 12.07.2016.
 */
public class SignatureDatabaseTest {
    public SignatureDatabase db = new SignatureDatabase();
    public SignatureJobModel signatureJobModel = new SignatureJobModel("Ikke signert", "987654321","11111111111");
    public DatabaseSignatureStorage storage = new DatabaseSignatureStorage();


    @Test
    public void simple(){
        Assert.assertNotNull(signatureJobModel);
        Assert.assertNotNull(db);
        Assert.assertNotNull(storage);
    }

    @Test
    public void creatingTable(){
        boolean tableCreated = db.createTable();
        Assert.assertTrue(tableCreated);
    }

    @Test
    public void insertingSignature() {
        db.insertSignature(signatureJobModel);
        Assert.assertEquals(db.getSignatureJob(signatureJobModel), "(1, Ikke signert, 11111111111, 987654321, document)");
    }
    @Test
    public void insertingSignatureFromStorage(){
        SignatureJobModel signatureJobModel2 = new SignatureJobModel("Ikke signert", "123456788",  "222222222222");
        storage.insertSignaturejobToDB(signatureJobModel2);
        Assert.assertEquals(db.getSignatureJob(signatureJobModel2), "(2, Ikke signert, 222222222222, 123456788, document)");
    }

    @Test
    public void updatingStatus() {
        db.updateStatus(signatureJobModel, "Signert");
        Assert.assertEquals(db.getSignatureJob(signatureJobModel), "(1, Signert, 11111111111, 987654321, document)");
    }

    @Test
    public void updatingStatusFromStorage(){
        SignatureJobModel signatureJobModel2 = new SignatureJobModel("Ikke signert", "123456788",  "222222222222");
       storage.updateStatus(signatureJobModel2, "Rejected");
        Assert.assertEquals(db.getSignatureJob(signatureJobModel2), "(2, Rejected, 222222222222, 123456788, document)");
      }




}

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by camp-mlo on 12.07.2016.
 */
public class SignatureDatabaseTest {
    public SignatureDatabase db = new SignatureDatabase();
    public SignatureJobModel signatureJobModel1 = new SignatureJobModel("Ikke signert", "987654321","11111111111");
    public SignatureJobModel signatureJobModel2 = new SignatureJobModel("Ikke signert", "123456788",  "222222222222");
    public SignatureJobModel signatureJobModel3 = new SignatureJobModel("Ikke signert", "123456777",  "33333333333");
    public DatabaseSignatureStorage storage = new DatabaseSignatureStorage();


    @Test
    public void simple(){
        Assert.assertNotNull(signatureJobModel1);
        Assert.assertNotNull(db);
        Assert.assertNotNull(storage);
    }
    @Test
    public void creatingTable(){
        boolean tableCreated = db.createTable();
        Assert.assertTrue(tableCreated);
    }

    @Test
    public void testConnection(){
        Assert.assertEquals(db.connection.toString(), "conn0: url=jdbc:h2:file:src/main/resources/signature user=SA");
    }

    @Test
    public void insertingSignature() {
        db.insertSignature(signatureJobModel1);
        Assert.assertEquals(db.getSignatureJob(signatureJobModel1).substring(4), "Ikke signert, 11111111111, 987654321, document)");
    }
    @Test
    public void insertingSignatureFromStorage(){
        storage.insertSignaturejobToDB(signatureJobModel2);
        Assert.assertEquals(db.getSignatureJob(signatureJobModel2).substring(4), "Ikke signert, 222222222222, 123456788, document)");
    }

    @Test
    public void updatingStatus() {
        db.updateStatus(signatureJobModel1, "Signert");
        Assert.assertEquals(db.getSignatureJob(signatureJobModel1).substring(4), "Signert, 11111111111, 987654321, document)");
    }

    @Test
    public void updatingStatusFromStorage(){
        storage.updateStatus(signatureJobModel2, "Rejected");
        Assert.assertEquals(db.getSignatureJob(signatureJobModel2).substring(4), "Rejected, 222222222222, 123456788, document)");
    }

    @Test
    public void gettingSigner() {
        db.insertSignature(signatureJobModel3);
        Assert.assertEquals(db.getSigner("123456777"), "33333333333");
    }


}

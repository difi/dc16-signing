import org.h2.store.Data;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseSignatureStorage {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DatabaseSignatureStorage.class);

    private  static SignatureDatabase db = new SignatureDatabase();
    private static DatabaseSignatureStorage ourInstance = new DatabaseSignatureStorage();
    public static DatabaseSignatureStorage getDbInstance(){
        return ourInstance;
    }
    private static HashMap<String, String> defaultData;

    public static void main(String[] args) {
        db.createTable();

        // Creating test-signatur 1
        SignatureJobModel signatureJob = new SignatureJobModel(SignatureDatabase db);

        signatureJob.updateStatus(db, "signert");


      /*  // Creating test-signatur 2
        String status2 = "Ikke signert";
        String sender2 = "987654321";
        String signer2 = "111111111111";
        String document2 = "Dokument til signering.pdf";

        // Inserting sender1 and sender2 data into database and printing
        db.insertSignature(status1, signer1, sender1, document1);
        db.insertSignature(status2, signer2, sender2, document2);

        // Retriving a signature job using sender as key
        String s = db.getSignatureJob(sender1);
        System.out.print("DB-data: Signaturejob: " + s + "\n\n");

        // Retriving a signer id using sender as key
        String signer = db.getSigner(sender1);
        System.out.println( "DB-data: Signer: " + signer + "\n\n");
*/
        try {
            db.printDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n \n __________________________________________________________________________________\n");
        logger.debug("TESTLOGGER \n");

    }
}

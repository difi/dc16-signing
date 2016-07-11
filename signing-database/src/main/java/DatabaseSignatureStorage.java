import org.apache.catalina.LifecycleState;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
        String status1 = "Ikke signert";
        String sender1 = "123456789";
        String signer1 = "17079493538";
        String document1 = "Dokument til signering.pdf";

        // Creating test-signatur 2
        String status2 = "Ikke signert";
        String sender2 = "987654321";
        String signer2 = "111111111111";
        String document2 = "Dokument til signering.pdf";

        // Inserting sender1 and sender2 data into database and printing
        db.insertSignature(status1, signer1, sender1, document1);
        db.insertSignature(status2, signer2, sender2, document2);
        try {
            db.printDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retriving a signature job using sender as key
        String signaturejob = db.getSignatureJob(sender1);
        System.out.print("DB-data: Signaturejob: " + signaturejob + "\n\n");

        // Retriving a signer id using sender as key
        String signer = db.getSigner(sender1);
        System.out.println( "DB-data: Signer: " + signer + "\n\n");

        // Update signature status to Signert
        db.updateValue(sender1, "status", "Signert");


        System.out.println("\n \n __________________________________________________________________________________\n");
        logger.debug("TESTLOGGER \n");

    }
}

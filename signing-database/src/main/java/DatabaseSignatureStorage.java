import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseSignatureStorage {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DatabaseSignatureStorage.class);

    private static final int MINUTE =60*1000;
    private int initialValidPeriod = 30;
    private int expandSessionPeriod = 30;
    private int maxValidPeriod = 120;
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

        db.insertSignature(status1, signer1, sender1, document1);
        db.insertSignature(status2, signer2, sender2, document2);
        try {
            db.selectQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("TESTLOGGER");

    }
}

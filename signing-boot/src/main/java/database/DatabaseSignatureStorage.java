package database;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by camp-mlo on 07.07.2016.
 */
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

    // private Map<String, >

    private static String generateDatabaseKey(String a, String b) {
        String uuid = UUID.randomUUID().toString();
        db.insertSomething("name", "id");
        return null;
    }


    public static void main(String[] args) {
        db.createTable();

        // Creating test entries
        db.insertSomething("name", "id");
        logger.debug("TESTLOGGER");

    }
}

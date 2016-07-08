package database;

import org.apache.catalina.Server;
import org.slf4j.LoggerFactory;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        try {
            db.selectQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("TESTLOGGER");

    }
}

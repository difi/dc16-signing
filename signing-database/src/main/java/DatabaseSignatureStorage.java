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

    public SignatureJobModel createDatabase(){
        db.createTable();
        SignatureJobModel signatureJobModel = new SignatureJobModel("Ikke signert", "123456789", "17079493538");
        db.insertSignature(signatureJobModel);
        return signatureJobModel;
    }

    public void updateStatus(SignatureJobModel signatureJobModel, String status){
        db.updateStatus(signatureJobModel, status);
    }
}

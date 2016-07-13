import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseSignatureStorage {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DatabaseSignatureStorage.class);

    private  static SignatureDatabase db = new SignatureDatabase();

    public DatabaseSignatureStorage(){
        db.createTable();
    }

    public void insertSignaturejobToDB(SignatureJobModel signatureJobModel){
        db.insertSignature(signatureJobModel);
    }

    public void updateStatus(SignatureJobModel signatureJobModel, String status){
        db.updateStatus(signatureJobModel, status);
    }
}

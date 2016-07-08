import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;

public class SignatureDatabase {
    private final String JDBC_DRIVER = "org.h2.Driver";
    //private final String DB_URL = "jdbc:h2:file:C:/Users/camp-eul/Documents/GitHub/dc16-signing/signing-database/src/main/resources/signature";
    private final String DB_URL = "jdbc:h2:file:src/main/resource/signature";

    private final String USER = "SA";
    private final String PASS = "";

    private static Logger logger = LoggerFactory.getLogger(SignatureDatabase.class);
    Server server;
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData metaData;

    public String uuid;

    public SignatureDatabase(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            //dokumentTilSignering = new File(classLoader.getResource(relativeDocumentPath).getFile());

            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

        } catch (SQLException | ClassNotFoundException e){
            System.err.println("Exception caught in SignatureDatabas.SignatureDatabase(): " + e);
            e.printStackTrace();
        }
    }

    public void createTable(){
        try {
            statement.execute("DROP TABLE IF EXISTS SIGNATURE ");
            statement.execute("CREATE TABLE SIGNATURE " +
                    "(id MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "status VARCHAR(30)," +
                    "signer VARCHAR(30), " +
                    "sender VARCHAR(30)," +
                    "document VARCHAR(30)," +
                    "PRIMARY KEY (`id`));" );
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.createTable()" + e);
            e.printStackTrace();
        } System.out.println("DB: Table created");
    }

    public void selectQuery() throws SQLException {
        String query = "SELECT * FROM SIGNATURE";
        resultSet= statement.executeQuery(query);
        metaData = resultSet.getMetaData();
        int columnsNumber = metaData.getColumnCount();
        System.out.println("\n -----SIGNATURE DATABASE:---- \n");

        while (resultSet.next()) {
            for(int i = 1 ; i <= columnsNumber; i++){
                System.out.print(resultSet.getString(i) + " ");
            }
            System.out.println();
        }
    }

    public void insertSignature(String status, String signer, String sender, String document){
        //uuid = UUID.randomUUID().toString();
        String query = String.format("INSERT INTO SIGNATURE (status, signer, sender, document) " +
                "VALUES ('%s','%s','%s','%s');", status, signer, sender, document);

        System.out.println("DB: Insert signature query: " + query);
        try {
            statement.executeUpdate(query);
            System.out.println("DB: Signature inserted into the database ");
        } catch (SQLException e){
            System.err.println("SQLException caught in SignatureDatabase.insertSignature(): " + e);
            e.printStackTrace();
        }
    }


}

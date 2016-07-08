package database;


import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by camp-mlo on 07.07.2016.
 */
public class SignatureDatabase {
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:file:/Users/camp-mlo/Documents/GitHub/dc16-signing/src/main/java/database/signature";
    private final String USER = "SA";
    private final String PASS = "";

    private static Logger logger = LoggerFactory.getLogger(SignatureDatabase.class);
    Server server;
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData metaData;

    public SignatureDatabase(){
        try {
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
            statement.execute("CREATE TABLE IF NOT EXISTS SIGNATURE(ID INT PRIMARY KEY, NAME VARCHAR(30), STATUS VARCHAR(30), SIGNER VARCHAR(30), SENDER VARCHAR(30), DOCUMENT VARCHAR(30))\n");
           //statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS \"cookie_uuid_uindex \" ON PUBLIC.signature (uuid);");
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

    public void insertSomething(String name, String id){
        String query = String.format("INSERT INTO SIGNATURE \n" +
                    "VALUES(9, 'jobb1', 'ikke signert', ' doc1', 'per', 'kari')");

        System.out.println("DB: Insert signature query: " + query);
        try {
            statement.executeUpdate(query);
            System.out.println("DB: Signature inserted into the database with uuid ");
        } catch (SQLException e){
            System.err.println("SQLException caught in SignatureDatabase.insertSomething(): " + e);
            e.printStackTrace();
        }
    }
}

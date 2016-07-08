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
    private final String DB_URL = "jdbc:h2:mem:signature";
    private final String USER = "SA";
    private final String PASS = "";

    private static Logger logger = LoggerFactory.getLogger(SignatureDatabase.class);
    Server server;
    Connection connection;
    Statement statement;
    ResultSet resultSet;

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
            statement.execute("CREATE TABLE SIGNATURE(ID INT PRIMARY KEY,\n" +
                    "   NAME VARCHAR(255));");
           //statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS \"cookie_uuid_uindex \" ON PUBLIC.signature (uuid);");
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.createTable()" + e);
            e.printStackTrace();
        } System.out.println("DB: Table created");
    }

    public void insertSomething(String name, String id){
        long presentTimeInMillisec = new Date().getTime(); // lastUpdated
            String query = String.format("INSERT INTO " +
                    "SIGNATURE (name, id) " +
                        "VALUES ('name1', '1')");

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

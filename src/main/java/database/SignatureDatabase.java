package database;


import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by camp-mlo on 07.07.2016.
 */
public class SignatureDatabase {
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:mem:cookie";
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
            statement.execute("CREATE TABLE IF NOT EXISTS PUBLIC.signaturejob" +
            "(" +
                "uuid VARCHAR(36) PRIMARY KEY NOT NULL, " +
                "name VARCHAR(30) NOT NULL" +
                "id VARCHAR(30) NOT NULL" +
                ");");
           //statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS \" "ON PUBLIC.")
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.createTable()" + e);
            e.printStackTrace();
        } System.out.println("DB: Table created");
    }
}

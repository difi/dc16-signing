import java.sql.*;

public class SignatureDatabase {
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:mem:signature";
    private final String USER = "SA";
    private final String PASS = "";

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData metaData;

    /**
     * Connects the database.
     */
    public SignatureDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Exception caught in SignatureDatabase.SignatureDatabase(): " + e);
            e.printStackTrace();
        }
    }

    /**
     * Creating databasefile with empty table SIGNATURE: [id, status, signer, sender, document]
     * id = autogenerated id for each signatuire job
     * status = the signingstatus of each document. Signed / Not Signed
     * signer = the pid of the person that is supposed to signe document TODO: add functinality for multiple signers
     * sender = the org.number of the person/organisation who is sending the document to signing
     * document = the document, actual file or path/id??
     */
    public boolean createTable() {
        try {
            statement.execute("DROP TABLE SIGNATURE ");
            statement.execute("CREATE TABLE SIGNATURE " +
                    "(id int AUTO_INCREMENT PRIMARY KEY," +
                    "status VARCHAR(30)," +
                    "signer VARCHAR(30), " +
                    "sender VARCHAR(30)," +
                    "document VARCHAR(30));");
            System.out.println("DB: Table created");
            return true;
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.createTable()" + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method inserts a signature job into the database.
     *
     * @param signatureJob
     */
    public void insertSignature(SignatureJobModel signatureJob) {
        String query = String.format("INSERT INTO SIGNATURE (status, signer, sender, document) " +
                "VALUES ('%s','%s','%s','%s');", signatureJob.getStatus(), signatureJob.getSigner(), signatureJob.getSender(), "document");

        try {
            statement.executeUpdate(query);
            System.out.println("DB: Signature inserted into the database ");
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.insertSignature(): " + e);
            e.printStackTrace();
        }
        System.out.println("DB: Insert signature query: " + query);
    }

    /**
     * This method prints the table in the database. Not currently in use, but is nice to have when debugging.
     * @throws SQLException
     */
  /*
   public void printDB() throws SQLException {
        String query = "SELECT * FROM SIGNATURE";
        resultSet = statement.executeQuery(query);
        metaData = resultSet.getMetaData();
        int columnsNumber = metaData.getColumnCount();
        System.out.println("\n -----SIGNATURE DATABASE:---- \n");

        while (resultSet.next()) {
            for(int i = 1 ; i <= columnsNumber; i++){
                System.out.print(resultSet.getString(i) + " ");
            }
            System.out.println();
        }
        System.out.println("\n --------------------------------- \n");
    }
    */

    /**
     * This method gets a signature job, using the sender as a key.
     *
     * @param signatureJob
     * @return String signaturejob  with the whole signature job
     */
    public String getSignatureJob(SignatureJobModel signatureJob) {
        String query = String.format("SELECT (id, status, signer, sender, document)" +
                "FROM SIGNATURE " +
                "WHERE sender LIKE '%s';", signatureJob.getSender());

        String signaturejob = "";
        System.out.println("DB: Select query: " + query);
        try {
            resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();

            while (resultSet.next())
                signaturejob = resultSet.getString(1);
            return signaturejob;

        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.getSignatureJob()" + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method retrives signer id using sender as key.
     *
     * @param sender
     * @return signer
     **/
    public String getSigner(String sender) {
        String query = String.format("SELECT (signer)" +
                "FROM SIGNATURE " +
                "WHERE sender LIKE '%s';", sender);
        System.out.println("DB: Select query: " + query);
        try {
            resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();
            String signer = null;
            while (resultSet.next())
                signer = resultSet.getString(1);
            return signer;

        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.getSigner()" + e);
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(SignatureJobModel signatureJob, String value) {
        String update = String.format("UPDATE SIGNATURE " +
                "SET status = '%s' " +
                "WHERE sender = '%s' ;", value, signatureJob.getSender());
        System.out.println("DB: Update query: Signaturejob with sender = " + signatureJob.getSender() + " has updated 'status' to " + value);
        signatureJob.updateStatus(value);
        try {
            statement.executeUpdate(update);
        } catch (SQLException e) {
            System.err.println("SQLException caught in SignatureDatabase.updateStatus()" + e);
        }

    }

}

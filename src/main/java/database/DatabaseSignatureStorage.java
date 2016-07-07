package database;

/**
 * Created by camp-mlo on 07.07.2016.
 */
public class DatabaseSignatureStorage {

    private static final int MINUTE =60*1000;
    private int initialValidPeriod = 30;
    private int expandSessionPeriod = 30;
    private int maxValidPeriod = 120;

    private static DBsetup dbInstance = new DBsetup();

    public static DBsetup getDbInstance(){
        return dbInstance;
    }

    private static HashMap<String, String> defaultData;

    // private Map<String, >

    private static String generateDatabaseKey(String a, String b){
        String uuid = UUID.randomUUID().toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(uuid.getBytes());
            md.update(a.getBytes());
            md.update(b.getBytes());
            byte[] array = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException exc) {
            exc.printStackTrace();
        }
        return null;
    }

    private DBsetup(){
        defaultData = new HashMap<String, String>();
        defaultData.put("id", "123");
    }

}

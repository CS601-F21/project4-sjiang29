package DataBase;


/**
 *
 * DBConfig: Utility class to hold database config data
 */
public class DBConfig {

    private String database;
    private String username;
    private String password;

    /**
     * Config class constructor.
     * @param database
     * @param username
     * @param password
     */
    public DBConfig(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Return the database property.
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return the username property.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password property.
     * @return
     */
    public String getPassword() {
        return password;
    }
}


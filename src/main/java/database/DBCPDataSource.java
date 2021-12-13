package database;


import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * DBCPDataSource class: class to build connection to remote db
 * Reference: CS 601 example code
 *
 */
public class DBCPDataSource {
    // Apache commons connection pool implementation
    private static BasicDataSource ds = new BasicDataSource();

    // This code inside the static block is executed only once: the first time the class is loaded into memory.
    // -- https://www.geeksforgeeks.org/static-blocks-in-java/
    static {
        DBConfig dbConfig = DBUtilities.readConfig();
        // if the config file cannot be found
        if(dbConfig == null) {
            System.exit(1);
        }
        ds.setUrl("jdbc:mysql://localhost:3306/" + dbConfig.getDatabase());
        ds.setUsername(dbConfig.getUsername());
        ds.setPassword(dbConfig.getPassword());
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
    }

    /**
     * Return a Connection from the pool.
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * A private DBCP default constructor.
     */
    private DBCPDataSource(){ }
}


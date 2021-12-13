package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class to do data modification or selection towards sessions table.
 */
public class SessionsJDBC {

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @param sessionId
     * @return
     * @throws SQLException
     */
    public static ResultSet executeSelectUserBySessionId(Connection con, String sessionId) throws SQLException {
        String selectUserBySessionIdSql = "SELECT * FROM users where email=(SELECT user_email FROM sessions where id = ?);";
        PreparedStatement selectUserBySessionIdStmt = con.prepareStatement(selectUserBySessionIdSql);
        selectUserBySessionIdStmt.setString(1,sessionId);
        ResultSet results = selectUserBySessionIdStmt.executeQuery();
        return results;
    }

    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param sessionId
     * @param email

     * @throws SQLException
     */
    public static void executeInsertSession(Connection con, String sessionId, String email) throws SQLException {
        String insertSessionSql = "INSERT INTO sessions (id, user_email) VALUES (?, ?);";
        PreparedStatement insertSessionStmt = con.prepareStatement(insertSessionSql);
        insertSessionStmt.setString(1, sessionId);
        insertSessionStmt.setString(2, email);

        insertSessionStmt.executeUpdate();
    }
}

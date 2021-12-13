package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class to do data modification or selection towards users table.
 */
public class UsersJDBC {

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @throws SQLException
     */
    public static ResultSet executeSelectUserByEmail(Connection con, String email) throws SQLException {
        String selectUserByEmailSql = "SELECT * FROM users where email=?;";
        PreparedStatement selectUserByEmailStmt = con.prepareStatement(selectUserByEmailSql);
        selectUserByEmailStmt.setString(1,email);
        ResultSet results = selectUserByEmailStmt.executeQuery();
        return results;
    }

    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param name
     * @param email
     * @throws SQLException
     */
    public static void executeInsertUser(Connection con, String name, String email) throws SQLException {
        String insertUserSql = "INSERT INTO users (name, email, zipcode) VALUES (?, ?, ?);";
        PreparedStatement insertUserStmt = con.prepareStatement(insertUserSql);
        insertUserStmt.setString(1, name);
        insertUserStmt.setString(2, email);
        insertUserStmt.setNull(3,java.sql.Types.NULL);

        insertUserStmt.executeUpdate();
    }

    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param name
     * @param zipcode
     * @param email
     * @throws SQLException
     */
    public static void executeUpdateUser(Connection con, String email, String name, int zipcode) throws SQLException {
        if(name == "" && zipcode == 0){
            return;
        }
        String updateUserSql = "UPDATE users SET name=?, zipcode=? where email=?;";
        PreparedStatement updateUserStmt = con.prepareStatement(updateUserSql);
        if(!name.equals("")){
            updateUserStmt.setString(1, name);
        }
        if(zipcode != 0){
            updateUserStmt.setInt(2, zipcode);
        }
        updateUserStmt.setString(3, email);
        updateUserStmt.executeUpdate();
    }


}

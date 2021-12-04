package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsJDBC {

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @param buyerEmail
     * @throws SQLException
     */
    public static ResultSet executeDisplayUserTransactions(Connection con, String buyerEmail) throws SQLException {
        String selectEventsByBuyerEmailSql = "SELECT * FROM events WHERE id=(SELECT event_id FROM tickets WHERE buyer_email=?);";
        PreparedStatement selectEventsByBuyerEmailStmt = con.prepareStatement(selectEventsByBuyerEmailSql);
        selectEventsByBuyerEmailStmt.setString(1,buyerEmail);
        ResultSet results = selectEventsByBuyerEmailStmt.executeQuery();
        return results;
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con

     * @throws SQLException
     */
    public static ResultSet executeSelectAllEvents(Connection con) throws SQLException {
        String selectAllEventsSql = "SELECT * FROM events;";
        PreparedStatement selectAllEventsStmt = con.prepareStatement(selectAllEventsSql);

        ResultSet results = selectAllEventsStmt.executeQuery();
        return results;
    }

    public static ResultSet executeSelectEventById(Connection con, int eventId) throws SQLException {
        String selectEventByIdSql = "SELECT * FROM events WHERE id=?;";
        PreparedStatement selectEventByIdStmt = con.prepareStatement(selectEventByIdSql);
        selectEventByIdStmt.setInt(1, eventId);

        ResultSet results = selectEventByIdStmt.executeQuery();
        return results;
    }


}

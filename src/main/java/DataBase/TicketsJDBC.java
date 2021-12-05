package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketsJDBC {

    public static ResultSet executeSelectTicketsByEventId(Connection con, int eventId) throws SQLException {
        String selectTicketsByEventIdSql = "SELECT * FROM tickets WHERE sold='no' AND event_id=? ;";
        PreparedStatement selectTicketsByEventIdStmt = con.prepareStatement(selectTicketsByEventIdSql);
        selectTicketsByEventIdStmt.setInt(1, eventId);

        ResultSet results = selectTicketsByEventIdStmt.executeQuery();
        return results;
    }



    public static void transferTicket(Connection con, String oldBuyerEmail, String newBuyerEmail, int ticketId) throws SQLException {
        String updateBuyerEmailSql = "UPDATE tickets SET buyer_email=? WHERE id=?; ";
        PreparedStatement updateBuyerEmailStmt = con.prepareStatement(updateBuyerEmailSql);

        updateBuyerEmailStmt.setString(1, newBuyerEmail);
        updateBuyerEmailStmt.setInt(2, ticketId);
        updateBuyerEmailStmt.executeUpdate();
    }


    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param price
     * @param eventId
     * @param sold

     * @throws SQLException
     */
    public static void executeInsertTicket(Connection con, int price, int eventId, String sold) throws SQLException {
        String insertTicketSql = "INSERT INTO tickets (event_id, price, sold, buyer_email) VALUES (?, ?, ?, ?);";
        PreparedStatement insertTicketStmt = con.prepareStatement(insertTicketSql);
        insertTicketStmt.setInt(1, eventId);
        insertTicketStmt.setInt(2, price);
        insertTicketStmt.setString(3, sold);
        insertTicketStmt.setNull(4,java.sql.Types.NULL);

        insertTicketStmt.executeUpdate();
    }

}

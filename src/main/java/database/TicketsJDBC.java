package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class to do data modification or selection towards tickets table.
 */
public class TicketsJDBC {

    /**
     * A method to select all available tickets of a specific event
     * @param con
     * @param eventId
     * @return
     * @throws SQLException
     */
    public static ResultSet executeSelectTicketsByEventId(Connection con, int eventId) throws SQLException {
        String selectTicketsByEventIdSql = "SELECT * FROM tickets WHERE sold='no' AND event_id=? ;";
        PreparedStatement selectTicketsByEventIdStmt = con.prepareStatement(selectTicketsByEventIdSql);
        selectTicketsByEventIdStmt.setInt(1, eventId);

        ResultSet results = selectTicketsByEventIdStmt.executeQuery();
        return results;
    }

    /**
     * A method to select a ticket according to its id
     * @param con
     * @param ticketId
     * @return
     * @throws SQLException
     */
    public static ResultSet executeSelectTicketsByTicketId(Connection con, int ticketId) throws SQLException {
        String selectTicketsByTicketIdSql = "SELECT * FROM tickets WHERE id=? ;";
        PreparedStatement selectTicketsByTicketIdStmt = con.prepareStatement(selectTicketsByTicketIdSql);
        selectTicketsByTicketIdStmt.setInt(1, ticketId);

        ResultSet results = selectTicketsByTicketIdStmt.executeQuery();
        return results;
    }

    /**
     * A method to select a ticket by its event and buyer_email
     * @param con
     * @param buyerEmail
     * @param eventId
     * @return
     * @throws SQLException
     */
    public static ResultSet selectTicketsByEventAndBuyer(Connection con, String buyerEmail, int eventId) throws SQLException {
        String selectTicketsByEventAndBuyerSql = "SELECT * FROM tickets WHERE buyer_email=? AND event_id=? ;";
        PreparedStatement selectTicketsByEventAndBuyerStmt = con.prepareStatement(selectTicketsByEventAndBuyerSql);
        selectTicketsByEventAndBuyerStmt.setString(1, buyerEmail);
        selectTicketsByEventAndBuyerStmt.setInt(2, eventId);

        ResultSet results = selectTicketsByEventAndBuyerStmt.executeQuery();
        return results;
    }


    /**
     * A method to select all tickets purchased by a specific buyer
     * @param con
     * @param buyerEmail
     * @return
     * @throws SQLException
     */
    public static ResultSet executeSelectTicketsByBuyer(Connection con, String buyerEmail) throws SQLException {
        String selectTicketsByBuyerSql = "SELECT * FROM tickets WHERE sold='yes' AND buyer_email=? ;";
        PreparedStatement selectTicketsByBuyerStmt = con.prepareStatement(selectTicketsByBuyerSql);
        selectTicketsByBuyerStmt.setString(1, buyerEmail);

        ResultSet results = selectTicketsByBuyerStmt.executeQuery();
        return results;
    }


    /**
     * A method to modify a ticket's buyer
     * @param con
     * @param newBuyerEmail
     * @param ticketId
     * @throws SQLException
     */
    public static void transferTicket(Connection con, String newBuyerEmail, int ticketId) throws SQLException {
        String updateBuyerEmailSql = "UPDATE tickets SET buyer_email=? WHERE id=?; ";
        PreparedStatement updateBuyerEmailStmt = con.prepareStatement(updateBuyerEmailSql);

        updateBuyerEmailStmt.setString(1, newBuyerEmail);
        updateBuyerEmailStmt.setInt(2, ticketId);
        updateBuyerEmailStmt.executeUpdate();
    }


    /**
     * A method to insert a new ticket to db
     * @param con
     * @param price
     * @param eventId
     * @throws SQLException
     */
    public static void executeInsertTicket(Connection con, int price, int eventId) throws SQLException {
        String insertTicketSql = "INSERT INTO tickets (event_id, price, sold, buyer_email) VALUES (?, ?, ?, ?);";
        PreparedStatement insertTicketStmt = con.prepareStatement(insertTicketSql);
        insertTicketStmt.setInt(1, eventId);
        insertTicketStmt.setInt(2, price);
        insertTicketStmt.setString(3, "no");
        insertTicketStmt.setNull(4,java.sql.Types.NULL);

        insertTicketStmt.executeUpdate();
    }

    /**
     * A method to update a ticket
     * @param con
     * @param buyerEmail
     * @param sold
     * @param ticketId
     * @throws SQLException
     */
    public static void executeUpdateTicket(Connection con, String buyerEmail, String sold, int ticketId) throws SQLException {
        String updateTicketSql = "UPDATE tickets SET buyer_email=? , sold=? WHERE id=?; ";
        PreparedStatement updateTicketStmt = con.prepareStatement(updateTicketSql);

        updateTicketStmt.setString(1, buyerEmail);
        updateTicketStmt.setString(2, sold);
        updateTicketStmt.setInt(3, ticketId);
        updateTicketStmt.executeUpdate();
    }


    /**
     * A method to delete tickets using eventId
     * @param con
     * @param eventId
     * @return
     * @throws SQLException
     */
    public static void executeDeleteTicketsByEventId(Connection con, int eventId) throws SQLException {
        String deleteEventByIdSql = "DELETE FROM tickets WHERE event_id=?;";
        PreparedStatement deleteEventByIdStmt = con.prepareStatement(deleteEventByIdSql);
        deleteEventByIdStmt.setInt(1, eventId);
        deleteEventByIdStmt.executeUpdate();

    }
}

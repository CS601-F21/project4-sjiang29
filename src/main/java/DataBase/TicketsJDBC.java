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


}

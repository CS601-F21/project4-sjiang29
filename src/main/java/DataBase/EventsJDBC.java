package DataBase;

import java.sql.*;

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

    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     * @param con
     * @param creatorEmail
     * @param eventName
     * @param date
     * @param zipcode
     * @param eventDescription

     * @throws SQLException
     */
    public static void executeInsertEvent(Connection con, String creatorEmail, String eventName, String eventDescription,
                                          int zipcode, Date date, String startTime, String endTime) throws SQLException {
        String insertEventSql = "INSERT INTO events (name, creator_email, time, zipcode, description, start_time, end_time) " +
                "VALUES (?, ?, ?, ?, ?, ? ,?);";
        PreparedStatement insertEventStmt = con.prepareStatement(insertEventSql);
        insertEventStmt.setString(1, eventName);
        insertEventStmt.setString(2, creatorEmail);
        if(date == null){
            insertEventStmt.setNull(3,java.sql.Types.NULL);
        }else{
            insertEventStmt.setDate(3, date);
        }

        if(zipcode == 0){
            insertEventStmt.setNull(3,java.sql.Types.NULL);
        }else{
            insertEventStmt.setInt(4, zipcode);
        }

        if(eventDescription.equals("")){
            insertEventStmt.setNull(5,java.sql.Types.NULL);
        }else{
            insertEventStmt.setString(5, eventDescription);
        }

        if(startTime.equals("")){
            insertEventStmt.setNull(6, java.sql.Types.NULL);
        }else{
            insertEventStmt.setString(6,startTime);
        }

        if(endTime.equals("")){
            insertEventStmt.setNull(7, java.sql.Types.NULL);
        }else{
            insertEventStmt.setString(7, endTime);
        }

        insertEventStmt.executeUpdate();
    }



}

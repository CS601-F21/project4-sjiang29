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
        String selectEventsByBuyerEmailSql = "SELECT * FROM events WHERE id= ANY (SELECT DISTINCT event_id FROM tickets WHERE buyer_email=?);";
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

    public static ResultSet executeSelectEventsByCreator(Connection con, String creator_email) throws SQLException {
        String selectEventsByCreatorSql = "SELECT * FROM events where creator_email=? ;";
        PreparedStatement selectEventsByCreatorStmt = con.prepareStatement(selectEventsByCreatorSql);
        selectEventsByCreatorStmt.setString(1, creator_email);

        ResultSet results = selectEventsByCreatorStmt.executeQuery();
        return results;
    }

    public static ResultSet executeSelectEventById(Connection con, int eventId) throws SQLException {
        String selectEventByIdSql = "SELECT * FROM events WHERE id=?;";
        PreparedStatement selectEventByIdStmt = con.prepareStatement(selectEventByIdSql);
        selectEventByIdStmt.setInt(1, eventId);

        ResultSet results = selectEventByIdStmt.executeQuery();
        return results;
    }

    public static ResultSet executeSelectEventHasTickets(Connection con) throws SQLException {
        String selectEventHasTicketsSql = "SELECT events.id, events.name, events.date FROM events WHERE events.id= ANY (SELECT DISTINCT event_id FROM tickets WHERE sold= 'no');";
        PreparedStatement selectEventHasTicketsStmt = con.prepareStatement(selectEventHasTicketsSql);

        ResultSet results = selectEventHasTicketsStmt.executeQuery();
        return results;
    }

    public static void executeDeleteEventById(Connection con, int eventId) throws SQLException {
        String deleteEventByIdSql = "DELETE FROM events WHERE id=?;";
        PreparedStatement deleteEventByIdStmt = con.prepareStatement(deleteEventByIdSql);
        deleteEventByIdStmt.setInt(1, eventId);
        deleteEventByIdStmt.executeUpdate();

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
    public static ResultSet executeInsertEvent(Connection con, String creatorEmail, String eventName, String eventDescription,
                                          int zipcode, Date date, String startTime, String endTime, String location) throws SQLException {
        String insertEventSql =
                "INSERT INTO events (name, creator_email, date, zipcode, description, start_time, end_time, location) " +
                "VALUES (?, ?, ?, ?, ?, ? ,?, ?); ";

        PreparedStatement insertEventStmt = con.prepareStatement(insertEventSql);
        insertEventStmt.setString(1, eventName);
        insertEventStmt.setString(2, creatorEmail);
        if(date == null){
            insertEventStmt.setNull(3,java.sql.Types.NULL);
        }else{
            insertEventStmt.setDate(3, date);
        }

        if(zipcode == 0){
            insertEventStmt.setNull(4,java.sql.Types.NULL);
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

        if(location.equals("")){
            insertEventStmt.setNull(8, java.sql.Types.NULL);
        }else {
            insertEventStmt.setString(8, location);
        }

        insertEventStmt.executeUpdate();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() FROM events;");

        return rs;
    }

    public static ResultSet executeNewlyInsertedEventId(Connection con) throws SQLException {
        String selectNewEventIdSql = "SELECT LAST_INSERT_ID() FROM events;";
        PreparedStatement selectNewEventIdStmt = con.prepareStatement(selectNewEventIdSql);

        ResultSet results = selectNewEventIdStmt.executeQuery();
        return results;
    }


    public static void executeUpdateEventById(Connection con, int eventId, String eventName, String eventDescription,
                                          int zipcode, Date date, String startTime, String endTime, String location) throws SQLException {
        String updateEventSql = "UPDATE events SET" +
                "name=?, creator_email=?, date=?, zipcode=?, description=?, start_time=?, end_time=?, location=?" +
                "WHERE id=?;";
        PreparedStatement updateEventStmt;



        if(!eventName.equals("")){
            updateEventSql = "UPDATE events SET" +
                    "name=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, eventName);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(date != null){
            updateEventSql = "UPDATE events SET" +
                    "date=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setDate(1, date);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(zipcode != 0){
            updateEventSql = "UPDATE events SET" +
                    "zipcode=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setInt(1, zipcode);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(!eventDescription.equals("")){
            updateEventSql = "UPDATE events SET" +
                    "description=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, eventDescription);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(!startTime.equals("")){
            updateEventSql = "UPDATE events SET" +
                    "start_time=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, startTime);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(!endTime.equals("")){
            updateEventSql = "UPDATE events SET" +
                    "end_time=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, endTime);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }

        if(!location.equals("")){
            updateEventSql = "UPDATE events SET" +
                    "location=?" +
                    "WHERE id=?;";
            updateEventStmt = con.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, location);
            updateEventStmt.setInt(2,eventId);
            updateEventStmt.executeUpdate();
        }


    }




}

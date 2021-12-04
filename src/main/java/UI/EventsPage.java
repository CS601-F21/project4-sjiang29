package UI;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Events page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static final String RETURN_TO_LANDING =
            PAGE_HEADER + "<h1>Please login</h1>\n" + "<p><a href=\"/\">Login</a></p>" + PAGE_FOOTER;

    public static String displayEvents(ResultSet events) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are all the events.</h1>\n");
        if(events.next() == false){
            builder.append("<h2>There are no available events ongoing.</h2>\n");
        }else{
            while(events.next()){
                builder.append("<li>" + "Event Id: " + events.getInt("id") + "\n" +
                        "Event name: " + events.getString("name") + "\n" +
                        "</li>\n");
            }
        }

        builder.append(EVENT_DETAIL_FORM);
        builder.append(LINKS_IN_GET);
        builder.append(PAGE_FOOTER);


        return builder.toString();
    }

    public static final String LINKS_IN_GET =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/newEvent\"> Add a New Event</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";

    public static final String EVENT_DETAIL_FORM =
            "<h2>Please input the id of event that you are interested for more detail.</h2>\n" +
            "<form style=\"text-align: center\" action=\"/events\" method=\"post\">\n" +
                    "  <label for=\"term\">Event ID</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventId\" name=\"eventId\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    public static String displaySingleEvent(ResultSet event) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are the details of your selected event.</h1>\n");
        if(event.next() == false){
            builder.append("<h2>There is no event that has the given id, please double check</h2>\n");

        }else{
            while(event.next()){
                builder.append("<li>" + "Event id: " + event.getInt("id") + "\n" +
                        "Event name: " + event.getString("name") + "\n" +
                        "Event description: " + event.getString("description") + "\n" +
                        "Event time: " + event.getDate("time") + "\n" +
                        "Event zipcode: " + event.getInt("zipcode") + "\n" +
                        "Event creator's email: " + event.getString("creator_email") +
                        "</li>\n");
            }
        }

        builder.append(LINKS_IN_POST);
        builder.append(PAGE_FOOTER);

        return builder.toString();

    }

    public static final String LINKS_IN_POST =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/newEvent\"> Add a New Event</a> | " +
                    "<a href=\"/events\"> Show All Events</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";
}

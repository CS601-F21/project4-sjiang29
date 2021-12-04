package UI;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewEventPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>New Event page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static final String RETURN_TO_LANDING =
            PAGE_HEADER + "<h1>Please login</h1>\n" + "<p><a href=\"/\">Login</a></p>" + PAGE_FOOTER;

    public static final String NEW_EVENT_FORM =
            "<h1>Please input the following table to create a new event</h1>\n" +
            "<form style=\"text-align: center\" action=\"/newEvent\" method=\"post\">\n" +
                    "  <label for=\"term\">Event Name(MUST HAVE)</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventName\" name=\"eventName\"/><br/>\n" +
                    "  <label for=\"term\">Event Zipcode</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventZipcode\" name=\"eventZipcode\"/><br/>\n" +
                    "  <label for=\"term\">Event Date(YYYY-MM-DD)</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventDate\" name=\"eventDate\"/><br/>\n" +
                    "  <label for=\"term\">Start Time(hh-mm)</label><br/>\n" +
                    "  <input type=\"text\" id=\"startTime\" name=\"startTime\"/><br/>\n" +
                    "  <label for=\"term\">End Time(hh-mm)</label><br/>\n" +
                    "  <input type=\"text\" id=\"endTime\" name=\"endTime\"/><br/>\n" +
                    "  <label for=\"term\">Event Description</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventDescription\" name=\"eventDescription\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    public static final String LINKS_IN_GET =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/events\"> Show All Events</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";

    public static final String RESPONSE_FOR_GET =
            PAGE_HEADER +
                    NEW_EVENT_FORM +
                    LINKS_IN_GET +
                    PAGE_FOOTER;

    public static final String LINKS_IN_POST =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/newEvents\"> Add New Event</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";

    public static String displayResponseForPost(ResultSet events) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are all the events after updating.</h1>\n");

        while(events.next()){
            builder.append("<li>" + "Event Id: " + events.getInt("id") + "\n" +
                    "Event name: " + events.getString("name") + "\n" +
                    "</li>\n");
        }
        builder.append(LINKS_IN_POST);
        builder.append(PAGE_FOOTER);

        return builder.toString();
    }
}

package UI;

import Util.LandingUri;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Server.HttpServer.LOGGER;

public class MyOwnEventsPage {

    public static String displayMyEvents(ResultSet myEvents) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are all the events created by you.</h1>\n");

        while(myEvents.next()){

            String eventId = Integer.toString(myEvents.getInt("id"));
            String urlToDeleteEvent = buildDeleteEventByIdUri(eventId);
            String urlToModifyEvent = buildModifyEventByIdUri(eventId);

            builder.append("<li>" + "Event Id: " + myEvents.getInt("id") + "\n" +
                    "Event name: " + myEvents.getString("name") + "\n" +
                    "Event description: " + myEvents.getString("description") + "<br>" +
                    "Event time: " + myEvents.getDate("date") + "<br>" +
                    "Event zipcode: " + myEvents.getInt("zipcode") + "<br>" +
                    "Event location: " + myEvents.getString("location") + "<br>" +
                    "<a href=" + urlToDeleteEvent + ">" + "Delete</a>" + "<br>" +
                    "<a href=" + urlToModifyEvent + ">" + "Modify</a>" + "\n" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all the events created by you.</h1>\n")){
            builder.append("<h2>You haven't created any events</h2>\n");
        }
        builder.append(LINKS_IN_MYEVENTS);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    public static String getDeleteResponse(ResultSet deletedEvent) throws SQLException {

        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are the details of the event you would like to delete.</h1>\n");

        String deletedEventId = "";
        while(deletedEvent.next()){

            deletedEventId = Integer.toString(deletedEvent.getInt("id"));


            builder.append("<li>" + "Event Id: " + deletedEvent.getInt("id") + "\n" +
                    "Event name: " + deletedEvent.getString("name") + "\n" +
                    "Event description: " + deletedEvent.getString("description") + "<br>" +
                    "Event time: " + deletedEvent.getDate("date") + "<br>" +
                    "Event zipcode: " + deletedEvent.getInt("zipcode") + "<br>" +
                    "Event location: " + deletedEvent.getString("location") + "<br>" +
                    "</li>\n");
        }
        builder.append(getDeleteEventForm(deletedEventId));
        builder.append(LINKS_IN_MYEVENTS);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }


    public static String getDeleteEventForm(String eventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h2>Please check the box for deleting confirmation.</h2>\n");
        builder.append("<form style=\"text-align: center\" action=\"/myOwnEvents\" method=\"post\">\n" +
                "  <label for=\"deletedEventId\">DELETE</label><br/>\n" +
                "  <input type=\"checkbox\" id=\"deletedEventId\" name=\"deletedEventId\" value=" + eventId + "/><br/>\n" +
                "</form>");
        return  builder.toString();
    }

    public static String getModifyEventResponse(ResultSet modifiedEvent) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are the details of the event you would like to modify.</h1>\n");

        String modifiedEventId = "";
        while(modifiedEvent.next()){

            modifiedEventId = Integer.toString(modifiedEvent.getInt("id"));


            builder.append("<li>" + "Event Id: " + modifiedEvent.getInt("id") + "\n" +
                    "Event name: " + modifiedEvent.getString("name") + "\n" +
                    "Event description: " + modifiedEvent.getString("description") + "<br>" +
                    "Event time: " + modifiedEvent.getDate("date") + "<br>" +
                    "Event zipcode: " + modifiedEvent.getInt("zipcode") + "<br>" +
                    "Event location: " + modifiedEvent.getString("location") + "<br>" +
                    "</li>\n");
        }
        builder.append(getModifyEventForm(modifiedEventId));
        builder.append(LINKS_IN_MYEVENTS);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }

    public static String getModifyEventForm(String modifiedEventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>Please use the form below to update</h1>\n");
        builder.append("<form style=\"text-align: center\" action=\"/myOwnEvents\" method=\"post\">\n" +
                "  <label for=\"term\">Modified Event Id(Please double check)</label><br/>\n" +
                "  <input type=\"text\" id=\"modifiedEventId\" name=\"modifiedEventId\" value=" + modifiedEventId + "/><br/>\n" +
                "  <label for=\"term\">Event Name(MUST HAVE)</label><br/>\n" +
                "  <input type=\"text\" id=\"eventName\" name=\"eventName\"/><br/>\n" +
                "  <label for=\"term\">Event Zipcode</label><br/>\n" +
                "  <input type=\"text\" id=\"eventZipcode\" name=\"eventZipcode\"/><br/>\n" +
                "  <label for=\"term\">Event Location</label><br/>\n" +
                "  <input type=\"text\" id=\"eventLocation\" name=\"eventLocation\"/><br/>\n" +
                "  <label for=\"term\">Event Date(YYYY-MM-DD)</label><br/>\n" +
                "  <input type=\"text\" id=\"eventDate\" name=\"eventDate\"/><br/>\n" +
                "  <label for=\"term\">Start Time(hh-mm)</label><br/>\n" +
                "  <input type=\"text\" id=\"startTime\" name=\"startTime\"/><br/>\n" +
                "  <label for=\"term\">End Time(hh-mm)</label><br/>\n" +
                "  <input type=\"text\" id=\"endTime\" name=\"endTime\"/><br/>\n" +
                "  <label for=\"term\">Event Description</label><br/>\n" +
                "  <input type=\"text\" id=\"eventDescription\" name=\"eventDescription\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>"
            );

        return builder.toString();
    }


    public static final String DELETE_SUCCESS =
            UIConstants.PAGE_HEADER +
                    "<h1>The selected event has been deleted successfully</h1>\n" +
                    "<p style=\"text-align: center\">" +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/events\"> Show All Events</a> | " +
                    "<a href=\"/myOwnEvents\"> Show All Events Created By Me</a> | " +
                    "<a href=\"/tickets\"> Buy Ticket</a> | " +
                    "<a href=\"/logout\">Logout</a></p>" +
                    UIConstants.PAGE_FOOTER;




    public static final  String MODIFY_SUCCESS =
            UIConstants.PAGE_HEADER +
                    "<h1>The selected event has been modified successfully</h1>\n" +
                    "<p style=\"text-align: center\">" +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/events\"> Show All Events</a> | " +
                    "<a href=\"/myOwnEvents\"> Show All Events Created By Me</a> | " +
                    "<a href=\"/tickets\"> Buy Ticket</a> | " +
                    "<a href=\"/logout\">Logout</a></p>" +
                    UIConstants.PAGE_FOOTER;


    public static String LINKS_IN_MYEVENTS = "<p style=\"text-align: center\">" +
            "<a href=\"/account\"> Show My Account</a> | " +
            "<a href=\"/events\"> Show All Events</a> | " +
            "<a href=\"/tickets\"> Buy Ticket</a> | " +
            "<a href=\"/logout\">Logout</a></p>";

    public static String buildDeleteEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String landingUrl = landingUri.getLandingUri() + "/myOwnEvents";
        LOGGER.info("landing url:" + landingUrl);

        HttpGet httpGet = new HttpGet(landingUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("deletedEventId", eventId)
                .build();
        return uri.toString();
    }

    public static String buildModifyEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String landingUrl = landingUri.getLandingUri() + "/myOwnEvents";
        LOGGER.info("landing url:" + landingUrl);

        HttpGet httpGet = new HttpGet(landingUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("modifiedEventId", eventId)
                .build();
        return uri.toString();
    }
}

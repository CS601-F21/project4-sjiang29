package UI;

import Server.ServerConstants;
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
import static UI.NewEventPage.buildAddTicketByEventIdUri;
import static UI.NewEventPage.getSlackEventForm;


/**
 * A class to deal all UI in /myEvents
 */
public class MyEventsPage {

    /**
     * UI method to display events created by user
     * @param myEvents
     * @return
     */
    public static String displayMyEvents(ResultSet myEvents) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h3 style=\"color:#AA336A\">Below are all the events created by you.</h3>\n");

        while(myEvents.next()){

            String eventId = Integer.toString(myEvents.getInt("id"));
            String urlToDeleteEvent = buildDeleteEventByIdUri(eventId);
            String urlToModifyEvent = buildModifyEventByIdUri(eventId);
            String urlToSlackEvent = buildSlackEventByIdUri(eventId);
            String urlToAddTicket = buildAddTicketByEventIdUri(eventId);


            builder.append("<li>" + "<b>Event Id</b>: " + myEvents.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Event name</b>: " + myEvents.getString("name") + "&ensp;&ensp;&ensp;" +
                    "<b>Event description</b>: " + myEvents.getString("description") + "&ensp;&ensp;&ensp;" +
                    "<b>Event time</b>: " + myEvents.getDate("date") + "&ensp;&ensp;&ensp;" +
                    "<b>Event zipcode</b>: " + myEvents.getInt("zipcode") + "&ensp;&ensp;&ensp;" +
                    "<b>Event location</b>: " + myEvents.getString("location") + "&ensp;&ensp;" +
                    "<a href=" + urlToAddTicket + ">" + "Add Ticket</a>" + "&ensp;&ensp;" +
                    "<a href=" + urlToSlackEvent + ">" + "Send To Slack</a>" + "&ensp;&ensp;" +
                    "<a href=" + urlToDeleteEvent + ">" + "Delete</a>" + "&ensp;&ensp;" +
                    "<a href=" + urlToModifyEvent + ">" + "Modify</a>" + "\n" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<br>" + "<br>" + UIConstants.LINKS_IN_PAGE + "<hr>" +
                "<h3 style=\"color:#AA336A\">Below are all the events created by you.</h3>\n")){
            builder.append("<h3 style=\"color:#AA336A\">You haven't created any events</h3>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    /**
     * UI method to generate page to let user send an event to slack
     * @param slackedEvent
     * @return
     */
    public static String getSlackEventResponse(ResultSet slackedEvent) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">Below are the details of the event you would like to send to slack.</h2>\n");

        String slackedEventId = "";
        while(slackedEvent.next()){

            slackedEventId = Integer.toString(slackedEvent.getInt("id"));
            LOGGER.info("modifiedEventId: " + slackedEventId);
            builder.append("<li>" + "<b>Event Id</b>: " + slackedEvent.getInt("id") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event name</b>: " + slackedEvent.getString("name") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event description</b>: " + slackedEvent.getString("description") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event time</b>: " + slackedEvent.getDate("date") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event zipcode</b>: " + slackedEvent.getInt("zipcode") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event location</b>: " + slackedEvent.getString("location") + "<br>" +
                    "</li>\n");
        }
        builder.append(getSlackEventForm(slackedEventId));
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }


    /**
     * UI method to generate page to let user delete an event
     * @param deletedEvent
     * @return
     */
    public static String getDeleteResponse(ResultSet deletedEvent) throws SQLException {

        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">Below are the details of the event you would like to delete.</h2>\n");

        String deletedEventId = "";
        while(deletedEvent.next()){

            deletedEventId = Integer.toString(deletedEvent.getInt("id"));

            builder.append("<li>" + "<b>Event Id</b>: " + deletedEvent.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Event name</b>: " + deletedEvent.getString("name") + "&ensp;&ensp;&ensp;" +
                    "<b>Event description</b>: " + deletedEvent.getString("description") + "&ensp;&ensp;&ensp;" +
                    "<b>Event time</b>: " + deletedEvent.getDate("date") + "&ensp;&ensp;&ensp;" +
                    "<b>Event zipcode</b>: " + deletedEvent.getInt("zipcode") + "&ensp;&ensp;&ensp;"+
                    "<b>Event location</b>: " + deletedEvent.getString("location") + "&ensp;&ensp;&ensp;"+
                    "</li>\n");
        }
        builder.append(getDeleteEventForm(deletedEventId));
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }


    /**
     * UI method to generate form to delete event
     * @param eventId
     */
    public static String getDeleteEventForm(String eventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h2 style=\"color:#AA336A\">Please check the box for deleting confirmation.</h2>\n");
        builder.append("<form action=\"/myEvents\" method=\"post\">\n" +
                "  <label for=\"deletedEventId\">DELETE</label><br/>\n" +
                "  <input type=\"checkbox\" id=\"deletedEventId\" name=\"deletedEventId\" value=" + eventId + "/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
        return  builder.toString();
    }


    /**
     * UI method to generate page to let user to modify an event
     * @param modifiedEvent
     * @return
     */
    public static String getModifyEventResponse(ResultSet modifiedEvent) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">Below are the details of the event you would like to modify.</h2>\n");

        String modifiedEventId = "";
        while(modifiedEvent.next()){

            modifiedEventId = Integer.toString(modifiedEvent.getInt("id"));
            LOGGER.info("modifiedEventId: " + modifiedEventId);
            builder.append("<li>" + "<b>Event Id</b>: " + modifiedEvent.getInt("id") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event name</b>: " + modifiedEvent.getString("name") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event description</b>: " + modifiedEvent.getString("description") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event time</b>: " + modifiedEvent.getDate("date") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event zipcode</b>: " + modifiedEvent.getInt("zipcode") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event location</b>: " + modifiedEvent.getString("location") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event start time</b>: " + modifiedEvent.getTime("start_time") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event end time</b>: " + modifiedEvent.getTime("end_time") + "<br>" +
                    "</li>\n");
        }
        builder.append(getModifyEventForm(modifiedEventId));
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }


    /**
     * UI method to generate form to modify event
     * @param modifiedEventId
     */
    public static String getModifyEventForm(String modifiedEventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h3 style=\"color:#AA336A\">Please use the form below to update</h3>\n");
        builder.append("<form  action=\"/myEvents\" method=\"post\">\n" +
                "  <label for=\"term\">Modified Event Id(Please double check)</label><br/>\n" +
                "  <input type=\"text\" id=\"modifiedEventId\" name=\"modifiedEventId\" value=" + modifiedEventId + "><br/>\n" +
                "  <label for=\"term\">Event Name</label><br/>\n" +
                "  <input type=\"text\" id=\"eventName\" name=\"eventName\"/><br/>\n" +
                "  <label for=\"term\">Event Zipcode</label><br/>\n" +
                "  <input type=\"text\" id=\"eventZipcode\" name=\"eventZipcode\"/><br/>\n" +
                "  <label for=\"term\">Event Location</label><br/>\n" +
                "  <input type=\"text\" id=\"eventLocation\" name=\"eventLocation\"/><br/>\n" +
                "  <label for=\"term\">Event Date(YYYY-MM-DD)</label><br/>\n" +
                "  <input type=\"text\" id=\"eventDate\" name=\"eventDate\"/><br/>\n" +
                "  <label for=\"term\">Start Time(hh:mm)</label><br/>\n" +
                "  <input type=\"text\" id=\"startTime\" name=\"startTime\"/><br/>\n" +
                "  <label for=\"term\">End Time(hh:mm)</label><br/>\n" +
                "  <input type=\"text\" id=\"endTime\" name=\"endTime\"/><br/>\n" +
                "  <label for=\"term\">Event Description</label><br/>\n" +
                "  <input type=\"text\" id=\"eventDescription\" name=\"eventDescription\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>"
            );

        return builder.toString();
    }


    // response to tell user deletion was successfully
    public static final String DELETE_SUCCESS =
            UIConstants.PAGE_HEADER +
                    "<br>"+
                    "<br>"+
                    UIConstants.LINKS_IN_PAGE +
                    "<hr>"+
                    "<h2 style=\"color:#AA336A\">The selected event has been deleted successfully</h2>\n" +
                    UIConstants.PAGE_FOOTER;


    /**
     * UI method to generate response to tell user modification was successfully
     * @param event
     */
    public static String getSuccessfulModificationResponse(ResultSet event) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">The selected event has been modified successfully. " +
                "Below is the event information after modification</h2>\n");
        while(event.next()){
            builder.append("<li>" + "<b>Event id</b>: " + event.getInt("id") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event name</b>: " + event.getString("name") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event description</b>: " + event.getString("description") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event time</b>: " + event.getDate("date") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event zipcode</b>: " + event.getInt("zipcode") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event location</b>: " + event.getString("location") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event creator's email</b>: " + event.getString("creator_email") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event start time</b>: " + event.getTime("start_time") + "<br>" +
                    "&ensp;&ensp;&ensp;<b>Event end time</b>: " + event.getTime("end_time") + "<br>" +
                    "</li>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }



    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
    public static String buildDeleteEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String myEventsUrl = landingUri.getLandingUri() + ServerConstants.PATH_TO_MYEVENTS;
        LOGGER.info("myEventsUrl url:" + myEventsUrl);

        HttpGet httpGet = new HttpGet(myEventsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("deletedEventId", eventId)
                .build();
        return uri.toString();
    }

    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
    public static String buildModifyEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String myEventsUrl = landingUri.getLandingUri() + ServerConstants.PATH_TO_MYEVENTS;
        LOGGER.info("myEventsUrl url:" + myEventsUrl);

        HttpGet httpGet = new HttpGet(myEventsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("modifiedEventId", eventId)
                .build();
        return uri.toString();
    }

    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
    public static String buildSlackEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String myEventsUrl = landingUri.getLandingUri() + ServerConstants.PATH_TO_MYEVENTS;
        LOGGER.info("myEventsUrl url:" + myEventsUrl);

        HttpGet httpGet = new HttpGet(myEventsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("slackedEventId", eventId)
                .build();
        return uri.toString();
    }
}

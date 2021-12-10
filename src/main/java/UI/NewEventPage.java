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
import static UI.MyEventsPage.buildDeleteEventByIdUri;
import static UI.MyEventsPage.buildModifyEventByIdUri;


/**
 * A class to deal all UI in /newEvent
 */
public class NewEventPage {



    // generate NEW_EVENT_FORM
    public static final String NEW_EVENT_FORM =
            "<h3 style=\"color:#AA336A\">Please input the following table to create a new event</h3>\n" +
            "<form  action=\"/newEvent\" method=\"post\">\n" +
                    "  <label for=\"term\">Event Name(MUST HAVE)</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventName\" name=\"eventName\"/ required><br/>\n" +
                    "  <label for=\"term\">Event Zipcode</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventZipcode\" name=\"eventZipcode\"/><br/>\n" +
                    "  <label for=\"term\">Event Location</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventLocation\" name=\"eventLocation\"/ required><br/>\n" +
                    "  <label for=\"term\">Event Date(YYYY-MM-DD)</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventDate\" name=\"eventDate\"/ required><br/>\n" +
                    "  <label for=\"term\">Start Time(hh:mm)</label><br/>\n" +
                    "  <input type=\"text\" id=\"startTime\" name=\"startTime\"/><br/>\n" +
                    "  <label for=\"term\">End Time(hh:mm)</label><br/>\n" +
                    "  <input type=\"text\" id=\"endTime\" name=\"endTime\"/><br/>\n" +
                    "  <label for=\"term\">Event Description</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventDescription\" name=\"eventDescription\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";


    // response for a get request
    public static final String RESPONSE_FOR_GET =
            UIConstants.PAGE_HEADER +
                    "<br>"+
                    "<br>"+
                    UIConstants.LINKS_IN_PAGE +
                    "<hr>"+
                    NEW_EVENT_FORM +
                    UIConstants.PAGE_FOOTER;



    /**
     * Method to generate a response to post request
     * @param events
     * @return
     */
    public static String displayResponseForPost(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">Below is the event you just created.</h2>\n");

        String eventId = "";
        while(events.next()){

            eventId= Integer.toString(events.getInt("id"));

            String urlToDeleteEvent = buildDeleteEventByIdUri(eventId);
            String urlToModifyEvent = buildModifyEventByIdUri(eventId);
            String urlToAddTicket = buildAddTicketByEventIdUri(eventId);

            builder.append("<li>" + "<b>Event Id</b>: " + events.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Event name</b>: " + events.getString("name") + "&ensp;&ensp;&ensp;" +
                    "<a href=" + urlToAddTicket + ">" + "Add Ticket</a>" + "&ensp;&ensp;" +
                    "<a href=" + urlToDeleteEvent + ">" + "Delete</a>" + "&ensp;&ensp;" +
                    "<a href=" + urlToModifyEvent + ">" + "Modify</a>" + "\n" +
                    "</li>\n");
        }
        builder.append(getSlackEventForm(eventId));
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }

    /**
     * Method to generate SlackEventForm
     * @param eventId
     * @return
     */
    public static String getSlackEventForm(String eventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h2 style=\"color:#AA336A\">Please check the box for sending this event to slack.</h2>\n");
        builder.append("<form  action=\"/newEvent\" method=\"post\">\n" +
                "  <label for=\"sendToSlackEventId\">SEND</label><br/>\n" +
                "  <input type=\"checkbox\" id=\"sendToSlackEventId\" name=\"sendToSlackEventId\" value=" + eventId + "/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
        return  builder.toString();
    }

    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
    public static String buildAddTicketByEventIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String newTicketUrl = landingUri.getLandingUri() + ServerConstants.PATH_TO_NEWTICKET;
        LOGGER.info("myEventsUrl url:" + newTicketUrl);

        HttpGet httpGet = new HttpGet(newTicketUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("eventId", eventId)
                .build();
        return uri.toString();
    }

    // response to tell user slack event was successfully
    public static final String SLACK_SUCCESS =
            UIConstants.PAGE_HEADER +
                    "<br>"+
                    "<br>"+
                    UIConstants.LINKS_IN_PAGE +
            "<h2 style=\"color:#AA336A\">The message including newly created event detail has been sent to slack channel successfully.</h2>\n" +
                    UIConstants.PAGE_FOOTER;

    // response to tell user slack event failed
    public static final String SLACK_FAILURE =
            UIConstants.PAGE_HEADER +
                    "<br>"+
                    "<br>"+
                    UIConstants.LINKS_IN_PAGE +
            "<h2 style=\"color:#AA336A\">Oops, something went wrong, please try again.</h2>\n" +
                    NEW_EVENT_FORM +
                    UIConstants.PAGE_FOOTER;


}

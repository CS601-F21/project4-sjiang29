package ui;

import util.LandingUri;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static server.HttpServer.LOGGER;


/**
 * A class to deal all UI in /events
 */

public class EventsPage {

    /**
     * UI method to display all events
     * @param events
     * @return
     */
    public static String displayEvents(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h3 style=\"color:#AA336A\">Below are all the events.</h3>\n");

        while(events.next()){
            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));
            LOGGER.info("url to an event:" + urlToAnEvent);

            builder.append("<li>" + "<b>Event Id</b>: " + events.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Event name</b>: " + events.getString("name") + "&ensp;&ensp;" +
                    "<b>Event Detail</b>: " + "<a href=" + urlToAnEvent + ">" + "Detail</a>" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h3 style=\"color:#AA336A\">Below are all the events.</h3>\n")){
            builder.append("<h3 style=\"color:#AA336A\">There are no available events ongoing.</h3>\n");
        }

        builder.append(EVENT_DETAIL_FORM);
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }



    // form for asking for a specific event's detail by providing event id
    public static final String EVENT_DETAIL_FORM =
            "<h3 style=\"color:#AA336A\">Please input the id of event that you are interested for more detail.</h3>\n" +
            "<form action=\"/events\" method=\"post\">\n" +
                    "  <label for=\"term\">Event ID</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventId\" name=\"eventId\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    /**
     * UI method to display single event
     * @param event
     * @return
     */
    public static String displaySingleEvent(ResultSet event) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h3 style=\"color:#AA336A\">Below are the details of your selected event.</h3>\n");

        while(event.next()){
            builder.append("<li>" + "<b>Event id</b>: " + event.getInt("id") + "<br>" +
                    "<b>Event name</b>: " + event.getString("name") + "<br>" +
                    "<b>Event description</b>: " + event.getString("description") + "<br>" +
                    "<b>Event date</b>: " + event.getDate("date") + "<br>" +
                    "<b>Event zipcode</b>: " + event.getInt("zipcode") + "<br>" +
                    "<b>Event location</b>: " + event.getString("location") + "<br>" +
                    "<b>Event start time</b>: " + event.getTime("start_time") + "<br>" +
                    "<b>Event end time</b>: " + event.getTime("end_time") + "<br>" +
                    "<b>Event creator's email</b>: " + event.getString("creator_email") +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are the details of your selected event.</h1>\n")){
            builder.append("<h3 style=\"color:#AA336A\">There is no event that has the given id, please double check</h3>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();

    }


    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
    public static String buildGetEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String landingUrl = landingUri.getLandingUri() + "/events";
        LOGGER.info("landing url:" + landingUrl);

        HttpGet httpGet = new HttpGet(landingUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("eventId", eventId)
                .build();
        return uri.toString();
    }
}

package UI;

import Util.Config;
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



public class EventsPage {

    public static String displayEvents(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h2>Below are all the events.</h2>\n");

        while(events.next()){
            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));
            LOGGER.info("url to an event:" + urlToAnEvent);

            builder.append("<li>" + "Event Id: " + events.getInt("id") + "\n" +
                    "Event name: " + events.getString("name") + "\n" +
                    "Event Detail: " + "<a href=" + urlToAnEvent + ">" + "Detail</a>" + "\n" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all the events.</h1>\n")){
            builder.append("<h2>There are no available events ongoing.</h2>\n");
        }

        builder.append(EVENT_DETAIL_FORM);
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }



    public static final String EVENT_DETAIL_FORM =
            "<h2>Please input the id of event that you are interested for more detail.</h2>\n" +
            "<form style=\"text-align: center\" action=\"/events\" method=\"post\">\n" +
                    "  <label for=\"term\">Event ID</label><br/>\n" +
                    "  <input type=\"text\" id=\"eventId\" name=\"eventId\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    public static String displaySingleEvent(ResultSet event) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h2>Below are the details of your selected event.</h2>\n");

        while(event.next()){
            builder.append("<li>" + "Event id: " + event.getInt("id") + "<br>" +
                    "Event name: " + event.getString("name") + "<br>" +
                    "Event description: " + event.getString("description") + "<br>" +
                    "Event time: " + event.getDate("date") + "<br>" +
                    "Event zipcode: " + event.getInt("zipcode") + "<br>" +
                    "Event location: " + event.getString("location") + "<br>" +
                    "Event creator's email: " + event.getString("creator_email") +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are the details of your selected event.</h1>\n")){
            builder.append("<h2>There is no event that has the given id, please double check</h2>\n");
        }

        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();

    }


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

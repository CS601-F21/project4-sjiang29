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
        builder.append("<h1>Below are all the events.</h1>\n");

        //if(rowcount == 0){
        //builder.append("<h2>There are no available events ongoing.</h2>\n");
        //}else{
        //String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));
        while(myEvents.next()){

            String eventId = Integer.toString(myEvents.getInt("id"));
            String urlToDeleteEvent = buildDeleteEventByIdUri(eventId);
            String urlToModifyEvent = buildDeleteEventByIdUri(eventId);

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
        //}
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all the events.</h1>\n")){
            builder.append("<h2>You haven't created any events</h2>\n");
        }
        builder.append(LINKS_IN_MYEVENTS);
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }


    public static final String LINKS_IN_MYEVENTS = "<p style=\"text-align: center\">" +
            "<a href=\"/account\"> Show My Account</a> | " +
            "<a href=\"/events\"> Show All Events</a> | " +
            "<a href=\"/tickets\"> Buy Ticket</a> | " +
            "<a href=\"/logout\">Logout</a></p>";

    public static String buildDeleteEventByIdUri (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String landingUrl = landingUri.getLandingUri() + "/myEvents";
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

        String landingUrl = landingUri.getLandingUri() + "/myEvents";
        LOGGER.info("landing url:" + landingUrl);

        HttpGet httpGet = new HttpGet(landingUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("modifiedEventId", eventId)
                .build();
        return uri.toString();
    }
}

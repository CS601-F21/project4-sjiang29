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

public class TicketsPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Tickets page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static final String RETURN_TO_LANDING =
            PAGE_HEADER + "<h1>Please login</h1>\n" + "<p><a href=\"/\">Login</a></p>" + PAGE_FOOTER;


    public static String displayEventsHaveTickets(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are all the events that still have available tickets.</h1>\n");

        //if(rowcount == 0){
        //builder.append("<h2>There are no available events ongoing.</h2>\n");
        //}else{
        //String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));
        while(events.next()){

            String urlToTicketDetail = buildUriToTicketDetail(Integer.toString(events.getInt("id")));
            LOGGER.info("url to show event ticket:" + urlToTicketDetail);

            builder.append("<li>" + "Event Id: " + events.getInt("id") + "\n" +
                    "Event name: " + events.getString("name") + "\n" +
                    "Tickets Detail: " + "<a href=" + urlToTicketDetail + ">" + "Show Tickets</a>" + "\n" +
                    "</li>\n");
        }
        //}
        if(builder.toString().equals(PAGE_HEADER + "<h1>Below are all the events that still have available tickets.</h1>\n")){
            builder.append("<h2>There are no available events ongoing.</h2>\n");
        }

        //builder.append(EVENT_DETAIL_FORM);
        builder.append(LINKS_IN_GET);
        builder.append(PAGE_FOOTER);

        return builder.toString();
    }

    public static String displayTickets(ResultSet tickets) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are all the tickets for the selected event.</h1>\n");

        //if(rowcount == 0){
        //builder.append("<h2>There are no available events ongoing.</h2>\n");
        //}else{
        //String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));
        while(tickets.next()){

            String urlToBuyTicket = buildUriToBuyTicket(Integer.toString(tickets.getInt("id")));
            LOGGER.info("url to buy event ticket:" + urlToBuyTicket);

            builder.append("<li>" + "Event Id: " + tickets.getInt("event_id") + "    " +
                    "Ticket Id: " + tickets.getInt("id") + "    " +
                    "Ticket Price: " + tickets.getInt("price") + "    " +
                    "Sold Or Not: " + tickets.getString("sold") + "    " +
                    "Buyer Email: " + tickets.getString("buyer_email") + "    " +
                    "Ticket Type: " + tickets.getString("type") + "    " +
                    "<a href=" + urlToBuyTicket + ">" + "Buy this Ticket</a>" + "\n" +
                    "</li>\n");
        }
        //}
        if(builder.toString().equals(PAGE_HEADER + "<h1>Below are all the tickets for the selected event.</h1>\n")){
            builder.append("<h2>There are no available tickets.</h2>\n");
        }

        //builder.append(EVENT_DETAIL_FORM);
        builder.append(LINKS_IN_GET);
        builder.append(PAGE_FOOTER);

        return builder.toString();

    }

    public static String LINKS_IN_GET =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/account\"> Show My Account</a> | " +
                    "<a href=\"/transferTicket\"> Transfer Ticket</a> | " +
                    "<a href=\"/newEvent\"> Add a New Event</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";


    public static String buildUriToTicketDetail (String eventId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String ticketsUrl = landingUri.getLandingUri() + "/tickets";
        LOGGER.info("tickets url:" + ticketsUrl);

        HttpGet httpGet = new HttpGet(ticketsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("eventId", eventId)
                .build();
        return uri.toString();
    }

    public static String buildUriToBuyTicket (String ticketId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String ticketsUrl = landingUri.getLandingUri() + "/tickets";
        LOGGER.info("tickets url:" + ticketsUrl);

        HttpGet httpGet = new HttpGet(ticketsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("ticketId", ticketId)
                .build();
        return uri.toString();
    }

    public static String updateSuccessfully = PAGE_HEADER +
            "<h2>You have purchased the ticket successfully</h2>\n" +
            LINKS_IN_GET +
            PAGE_FOOTER;

}

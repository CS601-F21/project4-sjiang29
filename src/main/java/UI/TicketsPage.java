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

    public static String displayEventsHaveTickets(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are all the events that still have available tickets.</h1>\n");

        while(events.next()){

            String urlToTicketDetail = buildUriToTicketDetail(Integer.toString(events.getInt("id")));
            LOGGER.info("url to show event ticket:" + urlToTicketDetail);

            builder.append("<li>" + "Event Id: " + events.getInt("id") + "\n" +
                    "Event name: " + events.getString("name") + "\n" +
                    "Tickets Detail: " + "<a href=" + urlToTicketDetail + ">" + "Show Tickets</a>" + "\n" +
                    "</li>\n");
        }
        //}
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all the events that still have available tickets.</h1>\n")){
            builder.append("<h2>There are no available events ongoing.</h2>\n");
        }

        //builder.append(EVENT_DETAIL_FORM);
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }

    public static String displayTickets(ResultSet tickets) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are all the tickets for the selected event.</h1>\n");

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
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all the tickets for the selected event.</h1>\n")){
            builder.append("<h2>There are no available tickets.</h2>\n");
        }

        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }




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

    public static String updateSuccessfully = UIConstants.PAGE_HEADER +
            "<h2>You have purchased the ticket successfully</h2>\n" +
            UIConstants.LINKS_IN_PAGE +
            UIConstants.PAGE_FOOTER;

}

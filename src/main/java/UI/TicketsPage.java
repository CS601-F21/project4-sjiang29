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
import static UI.EventsPage.buildGetEventByIdUri;


/**
 * A class to deal all UI in /tickets
 */
public class TicketsPage {

    /**
     * UI method to display events which still have tickets
     * @param events
     * @return
     */
    public static String displayEventsHaveTickets(ResultSet events) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:orange\">Below are all the events that still have available tickets.</h2>\n");

        while(events.next()){
            String urlToTicketDetail = buildUriToTicketDetail(Integer.toString(events.getInt("id")));
            LOGGER.info("url to show event ticket:" + urlToTicketDetail);

            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(events.getInt("id")));

            builder.append("<li>" + "<b>Event Id</b>: " + "<a href=" + urlToAnEvent + ">" + events.getInt("id") +"</a>" + "&ensp;&ensp;&ensp;" +
                    "<b>Event name</b>: " + events.getString("name") + "&ensp;&ensp;&ensp;" +
                    "<b>Tickets Detail</b>: " + "<a href=" + urlToTicketDetail + ">" + "Show Tickets</a>" + "\n" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<br>" + "<br>"+ UIConstants.LINKS_IN_PAGE + "<hr>" +
                "<h2 style=\"color:orange\">Below are all the events that still have available tickets.</h2>\n")){
            builder.append("<h2 style=\"color:orange\">There are no available events ongoing.</h2>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }


    /**
     * UI method to display tickets for an event
     * @param tickets
     * @return
     */
    public static String displayTickets(ResultSet tickets) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<h2 style=\"color:orange\">Below are all the tickets for the selected event.</h2>\n");

        while(tickets.next()){

            String urlToBuyTicket = buildUriToBuyTicket(Integer.toString(tickets.getInt("id")));
            LOGGER.info("url to buy event ticket:" + urlToBuyTicket);

            builder.append("<li>" + "<b>Event Id</b>: " + tickets.getInt("event_id") + "&ensp;&ensp;&ensp;" +
                    "<b>Ticket Id</b>: " + tickets.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Ticket Price</b>: " + tickets.getInt("price") + "&ensp;&ensp;&ensp;" +
                    "<b>Sold Or Not</b>: " + tickets.getString("sold") + "&ensp;&ensp;&ensp;" +
                    "<b>Buyer Email</b>: " + tickets.getString("buyer_email") + "&ensp;&ensp;&ensp;" +
                    "<a href=" + urlToBuyTicket + ">" + "Buy This Ticket</a>" + "\n" +
                    "</li>\n");
        }
        //}
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h2>Below are all the tickets for the selected event.</h2>\n")){
            builder.append("<h2 style=\"color:orange\">There are no available tickets.</h2>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }


    /**
     * UI helper method to generate a redirect link
     * @param eventId
     * @return
     */
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

    /**
     * UI helper method to generate a redirect link
     * @param ticketId
     * @return
     */
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

    // response to tell user a ticket was purchased successfully
    public static String updateSuccessfully = UIConstants.PAGE_HEADER +
            "<br>"+
            "<br>"+
            UIConstants.LINKS_IN_PAGE +
            "<hr>" +
            "<h2 style=\"color:orange\">You have purchased the ticket successfully</h2>\n" +
            UIConstants.PAGE_FOOTER;

}

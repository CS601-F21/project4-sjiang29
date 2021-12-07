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

public class MyTicketsPage {

    public static String displayMyTickets(ResultSet tickets) throws SQLException, FileNotFoundException, URISyntaxException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are all your purchased tickets.</h1>\n");

        while(tickets.next()){

            String urlToTransferTicket = buildUriToTransferTicket(Integer.toString(tickets.getInt("id")));
            LOGGER.info("url to transfer ticket:" + urlToTransferTicket);
            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(tickets.getInt("event_id")));

            builder.append("<li>" + "Event Id: " + "<a href=" + urlToAnEvent + ">" + tickets.getInt("event_id") +"</a>" + "    " +
                    "Ticket Id: " + tickets.getInt("id") + "    " +
                    "Ticket Price: " + tickets.getInt("price") + "    " +
                    "Sold Or Not: " + tickets.getString("sold") + "    " +
                    "Buyer Email: " + tickets.getString("buyer_email") + "    " +
                    "Ticket Type: " + tickets.getString("type") + "    " +
                    "<a href=" + urlToTransferTicket + ">" + "Transfer this Ticket</a>" + "\n" +
                    "</li>\n");
        }
        //}
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<h1>Below are all your purchased tickets.</h1>\n")){
            builder.append("<h2>You haven't purchased any tickets yet</h2>\n");
        }

        builder.append(LINKS_IN_MYTICKETS);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    public static String transferTicket(ResultSet ticketToBeTransfer) throws SQLException, FileNotFoundException, URISyntaxException {

        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are the details of the ticket you would like to transfer, please double check</h1>\n");
        String transferredTicketId = "";

        while(ticketToBeTransfer.next()){

            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(ticketToBeTransfer.getInt("event_id")));

            transferredTicketId = Integer.toString(ticketToBeTransfer.getInt("id"));
            builder.append("<li>" + "Event Id: " + "<a href=" + urlToAnEvent + ">" + ticketToBeTransfer.getInt("event_id") +"</a>" + "    " +
                    "Ticket Id: " + ticketToBeTransfer.getInt("id") + "    " +
                    "Ticket Price: " + ticketToBeTransfer.getInt("price") + "    " +
                    "Sold Or Not: " + ticketToBeTransfer.getString("sold") + "    " +
                    "Buyer Email: " + ticketToBeTransfer.getString("buyer_email") + "    " +
                    "Ticket Type: " + ticketToBeTransfer.getString("type") + "    " +

                    "</li>\n");
        }
        //}
        builder.append(TRANSFER_TICKET_FORM);
        builder.append(LINKS_IN_MYTICKETS);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    public static String TRANSFER_TICKET_FORM =
            "<form style=\"text-align: center\" action=\"/myTickets\" method=\"post\">\n" +
                    "  <label for=\"term\">Transferred Ticket Id</label><br/>\n" +
                    "  <input type=\"text\" id=\"ticketId\" name=\"ticketId\"/><br/>\n" +
                    "  <label for=\"term\">Email of whom you would transfer ticket to</label><br/>\n" +
                    "  <input type=\"text\" id=\"newOwnerEmail\" name=\"newOwnerEmail\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";


    public static final String LINKS_IN_MYTICKETS = "<p style=\"text-align: center\">" +
            "<a href=\"/account\"> Show My Account</a> | " +
            "<a href=\"/events\"> Show All Events</a> | " +
            "<a href=\"/tickets\"> Buy Ticket</a> | " +
            "<a href=\"/logout\">Logout</a></p>";


    public static final String TRANSFER_SUCCESSFUL = UIConstants.PAGE_HEADER +
            "<h1>The ticket has been transferred successfully</h1>\n" +
            LINKS_IN_MYTICKETS +
            UIConstants.PAGE_FOOTER;

    public static String buildUriToTransferTicket (String ticketId) throws URISyntaxException, FileNotFoundException {

        String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
        Gson gson = new Gson();
        LandingUri landingUri = gson.fromJson(new FileReader(file), LandingUri.class);

        String myTicketsUrl = landingUri.getLandingUri() + "/myTickets";
        LOGGER.info("tickets url:" + myTicketsUrl);

        HttpGet httpGet = new HttpGet(myTicketsUrl);
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("ticketId", ticketId)
                .build();
        return uri.toString();
    }
}

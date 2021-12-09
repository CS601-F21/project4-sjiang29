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
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:orange\">Below are all your purchased tickets.</h2>\n");

        while(tickets.next()){
            String urlToTransferTicket = buildUriToTransferTicket(Integer.toString(tickets.getInt("id")));
            LOGGER.info("url to transfer ticket:" + urlToTransferTicket);
            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(tickets.getInt("event_id")));

            builder.append("<li>" + "<b>Event Id</b>: " + "<a href=" + urlToAnEvent + ">" + tickets.getInt("event_id") +"</a>" + "&ensp;&ensp;&ensp;" +
                    "<b>Ticket Id</b>: " + tickets.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "<b>Ticket Price</b>: " + tickets.getInt("price") + "&ensp;&ensp;&ensp;" +
                    "<b>Sold Or Not</b>: " + tickets.getString("sold") + "&ensp;&ensp;&ensp;" +
                    "<b>Buyer Email</b>: " + tickets.getString("buyer_email") + "&ensp;&ensp;&ensp;" +
                    "<a href=" + urlToTransferTicket + ">" + "Transfer this Ticket</a>" + "\n" +
                    "</li>\n");
        }

        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<br>" + "<br>" + UIConstants.LINKS_IN_PAGE + "<hr>" +
                "<h2 style=\"color:orange\">Below are all your purchased tickets.</h2>\n")){
            builder.append("<h2 style=\"color:orange\">You haven't purchased any tickets yet</h2>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    public static String transferTicket(ResultSet ticketToBeTransfer) throws SQLException, FileNotFoundException, URISyntaxException {

        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:orange\">Below are the details of the ticket you would like to transfer, please double check</h2>\n");
        String transferredTicketId = "";

        while(ticketToBeTransfer.next()){

            String urlToAnEvent = buildGetEventByIdUri(Integer.toString(ticketToBeTransfer.getInt("event_id")));

            transferredTicketId = Integer.toString(ticketToBeTransfer.getInt("id"));
            builder.append("<li>" + "Event Id: " + "<a href=" + urlToAnEvent + ">" + ticketToBeTransfer.getInt("event_id") +"</a>" + "    " +
                    "Ticket Id: " + ticketToBeTransfer.getInt("id") + "&ensp;&ensp;&ensp;" +
                    "Ticket Price: " + ticketToBeTransfer.getInt("price") + "&ensp;&ensp;&ensp;" +
                    "Sold Or Not: " + ticketToBeTransfer.getString("sold") + "&ensp;&ensp;&ensp;" +
                    "Buyer Email: " + ticketToBeTransfer.getString("buyer_email") + "&ensp;&ensp;&ensp;" +
                    "</li>\n");
        }

        builder.append(getTransferTicketForm(transferredTicketId));
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();
    }

    public static String getTransferTicketForm(String transferredTicketId){
        StringBuilder builder = new StringBuilder();
        builder.append("<form action=\"/myTickets\" method=\"post\">\n" +
                "  <label for=\"term\">Transferred Ticket Id</label><br/>\n" +
                "  <input type=\"text\" id=\"ticketId\" name=\"ticketId\" value=" + transferredTicketId +"><br/>\n" +
                "  <label for=\"term\">Email of whom you would transfer ticket to</label><br/>\n" +
                "  <input type=\"text\" id=\"newOwnerEmail\" name=\"newOwnerEmail\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
        return builder.toString();

    }



    public static final String TRANSFER_SUCCESSFUL = UIConstants.PAGE_HEADER +
            "<br>"+
            "<br>"+
            UIConstants.LINKS_IN_PAGE +
            "<hr>"+
            "<h2 style=\"color:orange\">The ticket has been transferred successfully</h2>\n" +
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

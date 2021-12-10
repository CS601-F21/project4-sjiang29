package UI;

import java.sql.ResultSet;
import java.sql.SQLException;
import static Server.HttpServer.LOGGER;


/**
 * A class to deal all UI in /transactions
 */
public class TransactionPage {

    public static String displayTransactions(ResultSet transactions) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");
        builder.append("<h2 style=\"color:#AA336A\">Below are the details for all events for which you have purchased tickets..</h2>\n");

        while(transactions.next()){
            builder.append("<li>" + "<b>Event name</b>: " + transactions.getString("name") + "&ensp;&ensp;&ensp;" +
                    "<b>Event description</b>: " + transactions.getString("description") + "&ensp;&ensp;&ensp;" +
                    "<b>Event time</b>: " + transactions.getDate("date") + "&ensp;&ensp;&ensp;" +
                    "<b>Event zipcode</b>: " + transactions.getInt("zipcode") + "&ensp;&ensp;&ensp;" +
                    "<b>Event creator's email</b>: " + transactions.getString("creator_email") +
                    "</li>\n");
        }

        LOGGER.info("stringBuilder for transactions: " + builder.toString());
        if(builder.toString().equals(UIConstants.PAGE_HEADER + "<br>" + "<br>" + "<hr>"+ UIConstants.LINKS_IN_PAGE +
                "<h2 style=\"color:#AA336A\">Below are the details for all events for which you have purchased tickets..</h2>\n")){
            builder.append("<h2 style=\"color:#AA336A\">You don't have any transactions yet.</h2>\n");
        }

        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }

}

package UI;

import java.sql.ResultSet;
import java.sql.SQLException;
import static Server.HttpServer.LOGGER;



public class TransactionPage {

    public static String displayTransactions(ResultSet transactions) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Below are the details for all events for which you have purchased tickets..</h1>\n");

        while(transactions.next()){
            builder.append("<li>" + "Event name: " + transactions.getString("name") + "\n" +
                    "Event description: " + transactions.getString("description") + "\n" +
                    "Event time: " + transactions.getDate("date") + "\n" +
                    "Event zipcode: " + transactions.getInt("zipcode") + "\n" +
                    "Event creator's email: " + transactions.getString("creator_email") +
                    "</li>\n");
        }

        LOGGER.info("stringBuilder for transactions: " + builder.toString());
        if(builder.toString().equals(UIConstants.PAGE_HEADER +
                "<h1>Below are the details for all events for which you have purchased tickets..</h1>\n")){
            builder.append("<h2>You don't have any transactions yet.</h2>\n");
        }

        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append(UIConstants.PAGE_FOOTER);
        return builder.toString();

    }

}

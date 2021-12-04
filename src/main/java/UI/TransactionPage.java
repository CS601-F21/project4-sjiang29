package UI;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Transaction page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static final String RETURN_TO_LANDING =
            PAGE_HEADER + "<h1>Please login</h1>\n" + "<p><a href=\"/\">Login</a></p>" + PAGE_FOOTER;

    public static String displayTransactions(ResultSet transactions) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(PAGE_HEADER);
        builder.append("<h1>Below are the details of all your transactions.</h1>\n");
        if(transactions.next() == false){
            builder.append("<h2>You don't have any transactions yet.</h2>\n");
        }else{
            while(transactions.next()){
                builder.append("<li>" + "Event name: " + transactions.getString("name") + "\n" +
                        "Event description: " + transactions.getString("description") + "\n" +
                        "Event time: " + transactions.getDate("time") + "\n" +
                        "Event zipcode: " + transactions.getInt("zipcode") + "\n" +
                        "Event creator's email: " + transactions.getString("creater_email") +
                        "</li>\n");
            }
        }
        builder.append(PAGE_FOOTER);

        return builder.toString();

    }
}

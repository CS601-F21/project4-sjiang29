package ui;


/**
 * A class to deal all UI in /newTicket
 */
public class NewTicketPage {

    /**
     * Method to generate CreateTicketForm
     * @param eventId
     * @return
     */
    public static String getCreateTicketForm(String eventId){
        StringBuilder builder = new StringBuilder();
        builder.append("<h3 >Please input the following table to create a new ticket</h3>\n");
        builder.append("<form  action=\"/newTicket\" method=\"post\">\n" +
                "  <label for=\"term\">Event Id(MUST HAVE)</label><br/>\n" +
                "  <input type=\"text\" id=\"eventId\" name=\"eventId\"/ value=" + eventId + " required><br/>\n" +
                "  <label for=\"term\">Price</label><br/>\n" +
                "  <input type=\"text\" id=\"price\" name=\"price\"/ required><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");

        return builder.toString();
    }

    /**
     * Method to generate response for get request
     * @param eventId
     * @return
     */
    public static String getCreateTicketResponse(String eventId){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<br>");
        builder.append(UIConstants.LINKS_IN_PAGE);
        builder.append("<hr>");

        builder.append(getCreateTicketForm(eventId));
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }

    // response to tell user a new ticket was added successfully
    public static final String ADD_TICKET_SUCCESS = UIConstants.PAGE_HEADER +
            "<br>"+
            "<br>"+
            UIConstants.LINKS_IN_PAGE +
            "<hr>" +
            "<h2 style=\"color:orange\">You have added a new ticket successfully</h2>\n" +
            UIConstants.PAGE_FOOTER;

}

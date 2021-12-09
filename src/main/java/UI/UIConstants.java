package UI;

import Server.ServerConstants;

public class UIConstants {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Project4_Event Handler</title>\n" +
            "</head>\n" +

            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static final String RETURN_TO_LANDING =
            PAGE_HEADER +
                    "<h1 style=\"text-align: center\">Please login</h1>\n" +
                    "<p style=\"text-align: center\"><a href=\"/\">Login</a></p>" +
                    PAGE_FOOTER;

    public static String LINKS_IN_PAGE = "<p style=\"text-align: center\">" +
            "<a href=" + ServerConstants.PATH_TO_ACCOUNT + "> Show My Account</a> | " +
            "<a href=" + ServerConstants.PATH_TO_EVENTS + "> Show All Events</a> | " +
            "<a href=" + ServerConstants.PATH_TO_TRANSACTIONS + "> Show My Transactions</a> | " +
            "<a href=" + ServerConstants.PATH_TO_MYEVENTS + "> Show Events Created By Me</a> | " +
            "<a href=" + ServerConstants.PATH_TO_TICKETS + "> Buy Ticket</a> | " +
            "<a href=" + ServerConstants.PATH_TO_MYTICKETS + "> Show My Tickets</a> | " +
            "<a href=" + ServerConstants.PATH_TO_NEWEVENT + "> Add New Event</a> | " +
            "<a href=" + ServerConstants.PATH_TO_MYTICKETS + "> Transfer My Ticket</a> | " +
            "<a href=" + ServerConstants.PATH_TO_LOGOUT + "> Logout</a>";
}

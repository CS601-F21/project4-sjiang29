package UI;

import Server.LoginServletConstants;
import Util.ClientInfo;

public class LoginPage {

    public static final String LOGIN_ALREADY = UIConstants.PAGE_HEADER +
            "<h2 style=\"text-align: center\">You have already been authenticated</h2>" +
            LoginPage.LINKS_IN_LOGIN +
            UIConstants.PAGE_FOOTER;


    public static String LINKS_IN_LOGIN =
            "<p style=\"text-align: center\">" +
            "<a href=\"/account\"> Show My Account</a> | " +
            "<a href=\"/events\"> Show All Events</a> | " +
            "<a href=\"/tickets\"> Buy Ticket</a> | " +
            "<a href=\"/myTickets\"> My Ticket</a> | " +
            "<a href=\"/logout\">Logout</a></p>";

    public static String failedLogin(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<hr>");
        builder.append("<h2 style=\"text-align: center\">Oops, login unsuccessful, please try to login again</h2>");
        builder.append("<a style=\"text-align: center\" href=\""+url+"\"><img src=\"" + LoginServletConstants.BUTTON_URL +"\"/></a>");
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }


    public static String successfulLogin(ClientInfo clientInfo){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<hr>");
        builder.append("<h1 style=\"text-align: center\">Hello, " + clientInfo.getName() + "</h1>");
        builder.append(LINKS_IN_LOGIN);
       builder.append(UIConstants.PAGE_FOOTER);

       return builder.toString();
    }
}

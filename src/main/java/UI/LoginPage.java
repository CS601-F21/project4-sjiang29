package UI;

import Server.LoginServletConstants;
import Util.ClientInfo;

public class LoginPage {

    public static final String LOGIN_ALREADY = UIConstants.PAGE_HEADER +
            "<h1>You have already been authenticated</h1>" +
            UIConstants.PAGE_FOOTER;

    public static String failedLogin(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<h1>Oops, login unsuccessful, please try to login again</h1>");
        builder.append("<a href=\""+url+"\"><img src=\"" + LoginServletConstants.BUTTON_URL +"\"/></a>");
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }


    public static String successfulLogin(ClientInfo clientInfo){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br></br>");
        builder.append("<h1 style=\"text-align: center\">Hello, " + clientInfo.getName() + "</h1>");
        builder.append("<p style=\"text-align: center\">" +
                "<a href=\"/account\"> Show My Account</a> | " +
                "<a href=\"/events\"> Show All Events</a> | " +
                "<a href=\"/tickets\"> Buy Ticket</a> | " +
                "<a href=\"/myTickets\"> My Ticket</a> | " +
                "<a href=\"/logout\">Logout</a></p>");
       builder.append(UIConstants.PAGE_FOOTER);

       return builder.toString();
    }
}

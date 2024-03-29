package ui;

import server.LoginServletConstants;
import server.ServerConstants;
import util.ClientInfo;


/**
 * A class to deal all UI in /login
 */
public class LoginPage {

    // response to tell user he/she has already authenticated
    public static final String LOGIN_ALREADY = UIConstants.PAGE_HEADER +
            "<h2 style=\"text-align: center\">You have already been authenticated</h2>" +
            UIConstants.LINKS_IN_PAGE +
            UIConstants.PAGE_FOOTER;



    // redirect links in login page
    public static String LINKS_IN_LOGIN = "<p style=\"text-align: center\">" +
            "<h2 style=\"text-align: center; color:green\">For Account Information</h2>" +
            "<p style=\"text-align: center\">" +
            "<a style=\"color:green\" href=" + ServerConstants.PATH_TO_ACCOUNT + "> Show My Account</a></p> \n" +
                    "<br>" +

            "<h2 style=\"text-align: center; color:#AA336A\">For Events Related  Information</h2>" +
            "<p style=\"text-align: center\">" +
                    "<a style=\"color:#AA336A\" href=" + ServerConstants.PATH_TO_EVENTS + "> Show All Events</a> | " +
                    "<a style=\"color:#AA336A\" href=" + ServerConstants.PATH_TO_MYEVENTS + "> Show Events Created By Me</a> | " +
                    "<a style=\"color:#AA336A\" href=" + ServerConstants.PATH_TO_NEWEVENT + "> Add New Event</a> | " +
                    "<a style=\"color:#AA336A\" href=" + ServerConstants.PATH_TO_TRANSACTIONS + "> Show My Transactions</a></p>" +
                    "<br>" +

                    "<h2 style=\"text-align: center; color:orange\">For Tickets Related  Information</h2>" +
            "<p style=\"text-align: center\">" +
                    "<a style=\"color:orange\" href=" + ServerConstants.PATH_TO_TICKETS + "> Buy Ticket</a> | " +
                    "<a style=\"color:orange\" href=" + ServerConstants.PATH_TO_MYTICKETS + "> Show My Tickets</a> | " +
                    "<a style=\"color:orange\" href=" + ServerConstants.PATH_TO_MYTICKETS + "> Transfer My Ticket</a></p>" +
                    "<br>" +

            "<p style=\"text-align: center\">" +
                    "<a href=" + ServerConstants.PATH_TO_LOGOUT + "> Logout</a> </p>";


    /**
     * UI method to notify user login is failed
     * @param url
     */
    public static String failedLogin(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<hr>");
        builder.append("<h2 >Oops, login unsuccessful, please try to login again</h2>");
        builder.append("<br><br><br>");
        builder.append("<a href=\""+url+"\"><img src=\"" + LoginServletConstants.BUTTON_URL +"\"/></a>");
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }

    /**
     * UI method to notify user login is successful
     * @param clientInfo
     */
    public static String successfulLogin(ClientInfo clientInfo){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<hr>");
        builder.append("<h1 style=\"text-align: center\">Hello, " + clientInfo.getName() + "</h1>");
        builder.append("<br>");
        builder.append(LINKS_IN_LOGIN);
       builder.append(UIConstants.PAGE_FOOTER);

       return builder.toString();
    }
}

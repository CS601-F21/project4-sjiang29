package UI;

import Server.LoginServletConstants;
import Util.ClientInfo;

public class LoginPage {

    public static final String LOGIN_ALREADY = UIConstants.PAGE_HEADER +
            "<h2 style=\"text-align: center\">You have already been authenticated</h2>" +
            UIConstants.LINKS_IN_PAGE +
            UIConstants.PAGE_FOOTER;




    public static String failedLogin(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<hr>");
        builder.append("<h2 >Oops, login unsuccessful, please try to login again</h2>");
        builder.append("<a href=\""+url+"\"><img src=\"" + LoginServletConstants.BUTTON_URL +"\"/></a>");
        builder.append(UIConstants.PAGE_FOOTER);

        return builder.toString();
    }


    public static String successfulLogin(ClientInfo clientInfo){
        StringBuilder builder = new StringBuilder();
        builder.append(UIConstants.PAGE_HEADER);
        builder.append("<br>");
        builder.append("<hr>");
        builder.append("<h1 style=\"text-align: center\">Hello, " + clientInfo.getName() + "</h1>");
        builder.append(UIConstants.LINKS_IN_PAGE);
       builder.append(UIConstants.PAGE_FOOTER);

       return builder.toString();
    }
}

package UI;

import Server.LoginServletConstants;
import Server.ServerConstants;

public class LandingPage {


    public static String getLandingPage(String url){
        return UIConstants.PAGE_HEADER +
                "<br>" +
                "<br>" +
                "<hr>" +
                "<h1>Welcome to your event handler, please login using slack</h1>\n"+
                "<br>" +
                "<a href=\""+url+"\"><img src=\"" + LoginServletConstants.BUTTON_URL +"\"/></a>" +
                UIConstants.PAGE_FOOTER;
    }

    public static final String ALREADY_AUTHENTICATED =
            UIConstants.PAGE_HEADER +
                    "<br>"+
                    "<br>"+
                    UIConstants.LINKS_IN_PAGE +
            "<h1>You have already been authenticated</h1>" +
            UIConstants.PAGE_FOOTER;

}

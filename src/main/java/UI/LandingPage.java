package UI;

import Server.LoginServerConstants;

public class LandingPage {
    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Get landing page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";


    public static String getLandingPage(String url){
        return PAGE_HEADER +
                "<h1>Welcome to your event handle, please login using slack</h1>\n"+
                "<a href=\""+url+"\"><img src=\"" + LoginServerConstants.BUTTON_URL +"\"/></a>" +
                PAGE_FOOTER;
    }

}

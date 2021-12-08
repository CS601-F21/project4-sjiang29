package UI;

public class AccountPage {


    public static String getUserInfo(String userName, String email, int zipcode){
        return "<li style=\"text-align: center\">" + "UserName: " + userName + "</li>\n" +
                "<li style=\"text-align: center\">" + "UserEmail: " + email + "</li>\n" +
                "<li style=\"text-align: center\">" + "Zipcode: " + zipcode + "</li>\n" ;
    }

    public static final String ACCOUNT_FORM =
            "<form style=\"text-align: center\" action=\"/account\" method=\"post\">\n" +
                    "  <label for=\"term\">New UserName</label><br/>\n" +
                    "  <input type=\"text\" id=\"userName\" name=\"userName\"/><br/>\n" +
                    "  <label for=\"term\">New Zipcode</label><br/>\n" +
                    "  <input type=\"text\" id=\"zipcode\" name=\"zipcode\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    public static String getAccountPage(String userName, String email, int zipcode){
        return  UIConstants.PAGE_HEADER +
                "<h1 style=\"text-align: center\">Below are your user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                "<h2 style=\"text-align: center\">You can use the form below to modify your account information(name and/or zipcode)</h2>\n" +
                ACCOUNT_FORM +
                LINKS_GET_PAGE +
                UIConstants.PAGE_FOOTER;
    }

    public static String postAccountPage(String userName, String email, int zipcode){
        return  UIConstants.PAGE_HEADER +
                "<br>"+
                "<br>"+
                "<hr>"+
                "<h1 style=\"text-align: center\">Below are your updated user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                "<br>"+
                "<br>"+
                LINKS_POST_PAGE +
                UIConstants.PAGE_FOOTER;
    }


    public static final String LINKS_POST_PAGE =
                    "<p style=\"text-align: center\"><a href=\"/account\"> Show My Account</a> | " +
                            "<a href=\"/transaction\"> Show My Transactions</a> | " +
                            "<a href=\"/events\"> Show All Events</a> | " +
                            "<a href=\"/logout\">Logout</a></p>\n";

    public static final String LINKS_GET_PAGE =
            "<p style=\"text-align: center\"><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/events\"> Show All Events</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";
}

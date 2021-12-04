package UI;

public class AccountPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Account page</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static String getUserInfo(String userName, String email, int zipcode){
        return "<li>" + "UserName: " + userName + "</li>\n" +
                "<li>" + "UserEmail: " + email + "</li>\n" +
                "<li>" + "Zipcode: " + zipcode + "</li>\n" ;


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
        return  PAGE_HEADER +
                "<h1>Below are your user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                "<h2>You can use the form below to modify your account information(name and/or zipcode)</h2>\n" +
                ACCOUNT_FORM +
                LINKS_GET_PAGE +
                PAGE_FOOTER;
    }

    public static String postAccountPage(String userName, String email, int zipcode){
        return  PAGE_HEADER +
                "<h1>Below are your updated user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                LINKS_POST_PAGE +
                PAGE_FOOTER;
    }

    public static final String RETURN_TO_LANDING =
                    PAGE_HEADER +
                    "<h1>Please login</h1>\n" +
                    "<p><a href=\"/\">Login</a></p>" +
                    PAGE_FOOTER;


    public static final String LINKS_POST_PAGE =
                    "<p><a href=\"/account\"> Show My Account</a> | " +
                            "<a href=\"/transaction\"> Show My Transactions</a> | " +
                            "<a href=\"/allEvents\"> Show All Events</a> | " +
                            "<a href=\"/logout\">Logout</a></p>\n";

    public static final String LINKS_GET_PAGE =
            "<p><a href=\"/transaction\"> Show My Transactions</a> | " +
                    "<a href=\"/allEvents\"> Show All Events</a> | " +
                    "<a href=\"/logout\">Logout</a></p>\n";
}

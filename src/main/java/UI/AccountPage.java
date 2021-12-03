package UI;

public class AccountPage {

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <title>Show user account</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n";

    public static final String PAGE_FOOTER = "\n" +
            "</body>\n" +
            "</html>";

    public static String getUserInfo(String userName, String email, int zipcode){
        return  PAGE_HEADER +
                "<h1>Below are your user account information</h1>\n" +
                "<li>" + "UserName: " + userName + "</li>\n" +
                "<li>" + "UserEmail: " + email + "</li>\n" +
                "<li>" + "Zipcode: " + zipcode + "</li>\n" +
                "<h2>If needed, please use the from to modify your user account</h2>\n" +
                ACCOUNT_FORM +
                PAGE_FOOTER;

    }

    public static final String ACCOUNT_FORM =
            "<form style=\"text-align: center\" action=\"/account\" method=\"post\">\n" +
                    "  <label for=\"term\">New UserName</label><br/>\n" +
                    "  <input type=\"text\" id=\"userName\" name=\"userName\"/><br/>\n" +
                    "  <label for=\"term\">New Email</label><br/>\n" +
                    "  <input type=\"text\" id=\"email\" name=\"email\"/><br/>\n" +
                    "  <label for=\"term\">New Zipcode</label><br/>\n" +
                    "  <input type=\"text\" id=\"zipcode\" name=\"zipcode\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";
}

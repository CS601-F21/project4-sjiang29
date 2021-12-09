package UI;

public class AccountPage {


    public static String getUserInfo(String userName, String email, int zipcode){
        return "<li >" + "UserName: " + userName + "</li>\n" +
                "<li >" + "UserEmail: " + email + "</li>\n" +
                "<li >" + "Zipcode: " + zipcode + "</li>\n" ;
    }

    public static final String ACCOUNT_FORM =
            "<form  action=\"/account\" method=\"post\">\n" +
                    "  <label for=\"term\">New UserName</label><br/>\n" +
                    "  <input type=\"text\" id=\"userName\" name=\"userName\"/><br/>\n" +
                    "  <label for=\"term\">New Zipcode</label><br/>\n" +
                    "  <input type=\"text\" id=\"zipcode\" name=\"zipcode\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>";

    public static String getAccountPage(String userName, String email, int zipcode){
        return  UIConstants.PAGE_HEADER +
                "<h1 >Below are your user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                "<h2 >You can use the form below to modify your account information(name and/or zipcode)</h2>\n" +
                ACCOUNT_FORM +
                UIConstants.LINKS_IN_PAGE +
                UIConstants.PAGE_FOOTER;
    }

    public static String postAccountPage(String userName, String email, int zipcode){
        return  UIConstants.PAGE_HEADER +
                "<br>"+
                "<br>"+
                "<hr>"+
                "<h1>Below are your updated user account information</h1>\n" +
                getUserInfo(userName, email, zipcode) +
                "<br>"+
                "<br>"+
                UIConstants.LINKS_IN_PAGE +
                UIConstants.PAGE_FOOTER;
    }




}

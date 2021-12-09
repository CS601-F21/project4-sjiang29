import Server.ServerConstants;
import Util.LandingUri;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SystemTest {

    String file = "/Users/sj/Desktop/601_sw development/assignments/p4/landingUri.json";
    Gson gson = new Gson();
    LandingUri landingUri;
    {
        try {
            landingUri = gson.fromJson(new FileReader(file), LandingUri.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    String landingUrl = landingUri.getLandingUri();

    /**
     *
     * Test get request sent to server by checking response status code
     *
     */
    @Test
    public void testDoGet1(){
        String url = landingUrl + ServerConstants.PATH_TO_MYEVENTS;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet2(){
        String url = landingUrl + ServerConstants.PATH_TO_MYTICKETS;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet3(){
        String url = landingUrl + ServerConstants.PATH_TO_LOGIN;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet4(){
        String url = landingUrl + ServerConstants.PATH_TO_LOGOUT;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet5(){
        String url = landingUrl + ServerConstants.PATH_TO_NEWTICKET;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet6(){
        String url = landingUrl + ServerConstants.PATH_TO_NEWEVENT;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet7(){
        String url = landingUrl + ServerConstants.PATH_TO_TRANSACTIONS;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet8(){
        String url = landingUrl + ServerConstants.PATH_TO_EVENTS;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet9(){
        String url = landingUrl + ServerConstants.PATH_TO_TICKETS;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(401, statusCode);
    }

    @Test
    public void testDoGet10(){
        String url = landingUrl;
        int statusCode = Client.doGet(url, null);
        Assertions.assertEquals(200, statusCode);
    }


    /**
     *
     * Test post request sent to chat server by checking response status code
     *
     */
    @Test
    public void testDoPost1(){
        String url = landingUrl + ServerConstants.PATH_TO_MYEVENTS;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost2(){
        String url = landingUrl + ServerConstants.PATH_TO_MYTICKETS;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost3(){
        String url = landingUrl + ServerConstants.PATH_TO_EVENTS;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost4(){
        String url = landingUrl + ServerConstants.PATH_TO_TICKETS;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost5(){
        String url = landingUrl + ServerConstants.PATH_TO_NEWEVENT;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost6(){
        String url = landingUrl + ServerConstants.PATH_TO_NEWTICKET;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 401);
    }

    @Test
    public void testDoPost7(){
        String url = landingUrl + ServerConstants.PATH_TO_LOGIN;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 405);
    }

    @Test
    public void testDoPost8(){
        String url = landingUrl + ServerConstants.PATH_TO_LOGOUT;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 405);
    }

    @Test
    public void testDoPost9(){
        String url = landingUrl;
        int statusCode = Client.doPost(url, null, "test");
        Assertions.assertEquals(statusCode, 405);
    }

}



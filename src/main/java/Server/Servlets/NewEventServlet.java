package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import DataBase.UsersJDBC;
import Server.LoginServerConstants;
import UI.AccountPage;
import UI.EventsPage;
import UI.MyOwnEventsPage;
import UI.NewEventPage;
import Util.SlackRequestBody;
import Util.Token;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static Server.HttpServer.LOGGER;
import static Util.ServeletUtil.getBodyParameter;

public class NewEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
           resp.getWriter().println(NewEventPage.RESPONSE_FOR_GET);
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(NewEventPage.RETURN_TO_LANDING);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);

        if(clientInfoObj != null) {
            resp.setStatus(HttpStatus.OK_200);
            try(BufferedReader reader = req.getReader()) {
                String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
                LOGGER.info("body: " + body);

                if(body.contains("sendToSlackEventId")){
                    String[] bodyParts = body.split("=");
                    String sendToSlackEventId = bodyParts[1];

                    String message = "hello slack";
                    Gson gson = new Gson();
                    Token tokenObject = null;
                    try {
                        tokenObject = gson.fromJson(new FileReader("slackToken.json"), Token.class);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                    String token = tokenObject.getToken();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    headers.put("Content-Type", "application/json; utf-8");
                    headers.put("as_user", "true");
                    String url = "https://slack.com/api/chat.postMessage";
                    String channel = "C02KZE22RU0";
                    SlackRequestBody requestBody = new SlackRequestBody(channel, message);
                    String jsonRequestBody = gson.toJson(requestBody);

                    String res = Util.HttpFetcher.doPost(url, headers, jsonRequestBody);
                    LOGGER.info(res);
                    resp.getWriter().println(res);



                }else{
                    String[] bodyParts = body.split("&");

                    String eventNamePart = bodyParts[0];
                    String eventName = getBodyParameter(eventNamePart);
                    LOGGER.info("new event name: " + eventName);

                    String zipcodePart = bodyParts[1];
                    String zipcode = getBodyParameter(zipcodePart);

                    int zipCode = 0;
                    if(!zipcode.equals("")){
                        zipCode = Integer.parseInt(zipcode);
                    }
                    LOGGER.info("new event zipcode: " + zipcode);
                    String locationPart = bodyParts[2];
                    String location = getBodyParameter(locationPart);
                    LOGGER.info("new event location:" + location);

                    String eventDatePart = bodyParts[3];
                    Date date = null;
                    if(getBodyParameter(eventDatePart) != ""){
                        date  = Date.valueOf(LocalDate.parse(getBodyParameter(eventDatePart)));
                    }

                    LOGGER.info("new event date:" + date);

                    String startTimePart = bodyParts[4];
                    String startTime = getBodyParameter(startTimePart);
                    LOGGER.info("new event start time:" + startTime);

                    String endTimePart = bodyParts[5];
                    String endTime = getBodyParameter(endTimePart);
                    LOGGER.info("new event end time:" + endTime);

                    String eventDescriptionPart = bodyParts[6];
                    String eventDescription = getBodyParameter(eventDescriptionPart);
                    LOGGER.info("new event des:" + eventDescription);


                    String userEmail = "";

                    try (Connection connection = DBCPDataSource.getConnection()){
                        ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                        if(user.next()){
                            userEmail = user.getString("email");
                        }

                        ResultSet newEventId = EventsJDBC.executeInsertEvent(connection, userEmail,eventName,eventDescription,zipCode,date,startTime,endTime, location);

                        LOGGER.info(newEventId);
                        int lastInsertId = 0;
                        if(newEventId.next()){
                            lastInsertId = newEventId.getInt("LAST_INSERT_ID()");
                            LOGGER.info("LAST_INSERT_ID():" + lastInsertId);
                        }
                        //LOGGER.info("rowCountAfterInsertion events:" + rowCountAfterInsertion);
                        //ResultSet newlyCreatedEvent = EventsJDBC.executeSelectEventById(connection, rowCountAfterInsertion);
                        //resp.getWriter().println(NewEventPage.displayResponseForPost(newlyCreatedEvent));

                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }

            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(NewEventPage.RETURN_TO_LANDING);
        }
    }



}

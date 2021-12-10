package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import Server.LoginServletConstants;
import UI.NewEventPage;
import UI.UIConstants;
import Util.ResponseFromSlack;
import Util.SlackRequestBody;
import Util.Token;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.*;
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
import static Util.ServletUtil.getBodyParameter;
import static Util.ServletUtil.getId;


/**
 * Implements logic for the /newEvent path
 *
 */
public class NewEventServlet extends HttpServlet {


    /**
     * Implement logic to deal get request sent to /newEvent
     * @param req
     * @param resp
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.setStatus(HttpStatus.OK_200);
           resp.getWriter().println(NewEventPage.RESPONSE_FOR_GET);
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    /**
     * Implement logic to deal post request sent to /newEvent
     * @param req
     * @param resp
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);

        if(clientInfoObj != null) {
            resp.setStatus(HttpStatus.OK_200);
            try(BufferedReader reader = req.getReader()) {
                String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
                LOGGER.info("body: " + body);

                if(body.contains("sendToSlackEventId")){
                    sendToSlackRequest(body, resp.getWriter());

                }else{
                    createNewEvent(body, resp.getWriter(),sessionId);
                }

            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    /**
     * Private helper method to deal post request relating to send message to slack
     * @param body post request's body
     * @param writer
     *
     */
    private void sendToSlackRequest(String body, PrintWriter writer){
        String[] bodyParts = body.split("=");
        String sendToSlackEventId = getId(bodyParts[1]);
        LOGGER.info("send to slack event id:" + sendToSlackEventId);
        StringBuilder messageBuilder = new StringBuilder();
        try (Connection connection = DBCPDataSource.getConnection()){
            ResultSet sendToSlackEvent = EventsJDBC.executeSelectEventById(connection, Integer.parseInt(sendToSlackEventId));
            if(sendToSlackEvent.next()){
                messageBuilder.append("A new event was just created by me. Welcome to come.\n" );
                messageBuilder.append("Event name: " + sendToSlackEvent.getString("name") + "\n");
                messageBuilder.append("Event location: " + sendToSlackEvent.getString("location") + "\n");
                messageBuilder.append("Event date: " + sendToSlackEvent.getString("date") + "\n");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        String message = messageBuilder.toString();
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
        String channel = "C02KAM114UT";
        SlackRequestBody requestBody = new SlackRequestBody(channel, message);
        String jsonRequestBody = gson.toJson(requestBody);

        String res = Util.HttpFetcher.doPost(url, headers, jsonRequestBody);
        ResponseFromSlack responseFromSlack = gson.fromJson(res, ResponseFromSlack.class);
        if(responseFromSlack.getOk().equals("true")){
            writer.println(NewEventPage.SLACK_SUCCESS);
        }else{
            writer.println(NewEventPage.SLACK_FAILURE);
        }
    }


    /**
     * Private helper method to deal post request relating to create event
     * @param body post request's body
     * @param writer
     * @param sessionId
     *
     */
    private void createNewEvent(String body, PrintWriter writer, String sessionId){
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
            ResultSet newlyCreatedEvent = EventsJDBC.executeSelectEventById(connection, lastInsertId);
            writer.println(NewEventPage.displayResponseForPost(newlyCreatedEvent));

        }catch (SQLException | URISyntaxException | FileNotFoundException e){
            e.printStackTrace();
        }


    }

}

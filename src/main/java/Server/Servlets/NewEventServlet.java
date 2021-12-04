package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import DataBase.UsersJDBC;
import Server.LoginServerConstants;
import UI.AccountPage;
import UI.EventsPage;
import UI.NewEventPage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static Server.HttpServer.LOGGER;

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
            resp.getWriter().println(NewEventPage.RETURN_TO_LANDING);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {

            try(BufferedReader reader = req.getReader()) {
                String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
                //TODO: verify the body exists and it contains a =
                LOGGER.info("body: " + body);
                String[] bodyParts = body.split("&");

                String eventNamePart = bodyParts[0];
                String eventName = getBodyParameter(eventNamePart);
                LOGGER.info("new event name: " + eventName);

                String zipcodePart = bodyParts[1];
                String zipcode = getBodyParameter(zipcodePart);
                LOGGER.info("new event zipcode: " + zipcode);
                int zipCode = 0;
                if(!zipcode.equals("")){
                    zipCode = Integer.parseInt(zipcode);
                }

                String eventDatePart = bodyParts[2];
                Date date = Date.valueOf(LocalDate.parse(getBodyParameter(eventDatePart)));

                String startTimePart = bodyParts[3];
                String startTime = getBodyParameter(startTimePart);

                String endTimePart = bodyParts[4];
                String endTime = getBodyParameter(endTimePart);

                String eventDescriptionPart = bodyParts[5];
                String eventDescription = getBodyParameter(eventDescriptionPart);


                String userEmail = "";

                try (Connection connection = DBCPDataSource.getConnection()){
                    ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                    if(user.next()){
                        userEmail = user.getString("email");
                    }

                    EventsJDBC.executeInsertEvent(connection, userEmail,eventName,eventDescription,zipCode,date,startTime,endTime);

                    ResultSet allEvents = EventsJDBC.executeSelectAllEvents(connection);
                    resp.getWriter().println(NewEventPage.displayResponseForPost(allEvents));

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }else{
            // ask the user to login
            resp.getWriter().println(NewEventPage.RETURN_TO_LANDING);
        }
    }


    public static String getBodyParameter(String bodyPart){
        String[] parsedBodyPart = bodyPart.split("=");
        return parsedBodyPart[1];
    }

}

package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import Server.LoginServletConstants;
import UI.MyEventsPage;
import UI.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static Server.HttpServer.LOGGER;
import static Util.ServletUtil.getBodyParameter;
import static Util.ServletUtil.getId;

public class MyEventsServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);
        //resp.setStatus(HttpStatus.OK_200);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.setStatus(HttpStatus.OK_200);
            String userName ;
            String userEmail = "";
            int zipcode = 0;
            String deletedEventId = req.getParameter("deletedEventId");
            String modifiedEventId = req.getParameter("modifiedEventId");

            try (Connection connection = DBCPDataSource.getConnection()){
                if(deletedEventId != null){
                    int deletedEventID = Integer.parseInt(deletedEventId);
                    ResultSet deletedEvent = EventsJDBC.executeSelectEventById(connection, deletedEventID);
                    resp.getWriter().println(MyEventsPage.getDeleteResponse(deletedEvent));
                }else if(modifiedEventId != null){
                    int modifiedEventID = Integer.parseInt(modifiedEventId);
                    ResultSet modifiedEvent = EventsJDBC.executeSelectEventById(connection, modifiedEventID);
                    resp.getWriter().println(MyEventsPage.getModifyEventResponse(modifiedEvent));
                }else{
                    ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                    if(user.next()){
                        userEmail = user.getString("email");
                    }
                    ResultSet myOwnEvents = EventsJDBC.executeSelectEventsByCreator(connection, userEmail);
                    resp.getWriter().println(MyEventsPage.displayMyEvents(myOwnEvents));
                }
            }catch (SQLException | URISyntaxException e){
                e.printStackTrace();
            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }

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
                if(body.contains("deletedEventId")){
                    deleteEvent(body, resp.getWriter());
                }else{
                    modifyEvent(body,resp.getWriter());
                }
            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    private static void deleteEvent(String body, PrintWriter writer){
        String[] bodyParts = body.split("=");

        String deletedEventId = getId(bodyParts[1]);
        try (Connection connection = DBCPDataSource.getConnection()){
            EventsJDBC.executeDeleteEventById(connection, Integer.parseInt(deletedEventId));
            writer.println(MyEventsPage.DELETE_SUCCESS);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private static void modifyEvent(String body, PrintWriter writer){
        LOGGER.info("body: " + body);
        String[] bodyParts = body.split("&");

        String modifiedEventIdPart = bodyParts[0];
        int modifiedEventId = Integer.parseInt(getBodyParameter(modifiedEventIdPart));

        String eventNamePart = bodyParts[1];
        String eventName = getBodyParameter(eventNamePart);
        LOGGER.info("new event name: " + eventName);

        String zipcodePart = bodyParts[2];
        String zipcode = getBodyParameter(zipcodePart);
        LOGGER.info("new event zipcode: " + zipcode);
        int zipCode = 0;
        if(!zipcode.equals("")){
            zipCode = Integer.parseInt(zipcode);
        }

        String locationPart = bodyParts[3];
        String location = getBodyParameter(locationPart);

        String eventDatePart = bodyParts[4];
        Date date = null;
        if(getBodyParameter(eventDatePart) != ""){
            date  = Date.valueOf(LocalDate.parse(getBodyParameter(eventDatePart)));
        }

        String startTimePart = bodyParts[5];
        String startTime = getBodyParameter(startTimePart);

        String endTimePart = bodyParts[6];
        String endTime = getBodyParameter(endTimePart);

        String eventDescriptionPart = bodyParts[7];
        String eventDescription = getBodyParameter(eventDescriptionPart);
        String userEmail = "";

        try (Connection connection = DBCPDataSource.getConnection()){
            EventsJDBC.executeUpdateEventById(connection, modifiedEventId, eventName, eventDescription,
                    zipCode, date, startTime, endTime, location);
            writer.println(MyEventsPage.MODIFY_SUCCESS);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}

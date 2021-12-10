package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import Server.LoginServletConstants;
import UI.EventsPage;
import UI.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Server.HttpServer.LOGGER;


/**
 * Implements logic for the /events path
 *
 */
public class EventsServlet extends HttpServlet {


    /**
     * Implement logic to deal get request sent to /events
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
            String userName = "";
            String userEmail = "";
            int zipcode = 0;
            String eventId = req.getParameter("eventId");
            LOGGER.info("parameter -- eventId:" + eventId);
            try (Connection connection = DBCPDataSource.getConnection()){
                if(eventId != null){
                    int eventID = Integer.parseInt(eventId);
                    ResultSet event = EventsJDBC.executeSelectEventById(connection, eventID);
                    LOGGER.info("single event result:" + event);
                    resp.getWriter().println(EventsPage.displaySingleEvent(event));
                }else{
                    ResultSet allEvents = EventsJDBC.executeSelectAllEvents(connection);
                    LOGGER.info("all events: " + allEvents);
                    resp.getWriter().println(EventsPage.displayEvents(allEvents));
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

    /**
     * Implement logic to deal post request sent to /events
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
                String[] bodyParts = body.split("=");

                String eventId = bodyParts[1];
                int eventID = Integer.parseInt(eventId);

                try (Connection connection = DBCPDataSource.getConnection()){
                    ResultSet event = EventsJDBC.executeSelectEventById(connection, eventID);
                    resp.getWriter().println(EventsPage.displaySingleEvent(event));

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }




}

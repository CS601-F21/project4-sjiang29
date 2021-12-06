package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import DataBase.TicketsJDBC;
import Server.LoginServerConstants;
import UI.EventsPage;
import UI.TicketsPage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Server.HttpServer.LOGGER;

public class TicketsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            String userName = "";
            String userEmail = "";
            int zipcode = 0;
            String eventId = req.getParameter("eventId");
            String ticketId = req.getParameter("ticketId");
            LOGGER.info("parameter -- eventId:" + eventId);
            try (Connection connection = DBCPDataSource.getConnection()){
                if(eventId != null){
                    int eventID = Integer.parseInt(eventId);
                    ResultSet tickets = TicketsJDBC.executeSelectTicketsByEventId(connection, eventID);

                    resp.getWriter().println(TicketsPage.displayTickets(tickets));
                }else if(ticketId != null){
                    int ticketID = Integer.parseInt(ticketId);

                    ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                    if(user.next()){
                        userEmail = user.getString("email");
                    }

                    TicketsJDBC.executeUpdateTicket(connection, userEmail, "yes", ticketID);
                    resp.getWriter().println(TicketsPage.updateSuccessfully);

                }else{
                    ResultSet eventsHaveTickets = EventsJDBC.executeSelectEventHasTickets(connection);
                    LOGGER.info("events have tickets: " + eventsHaveTickets);
                    resp.getWriter().println(TicketsPage.displayEventsHaveTickets(eventsHaveTickets));
                }


            }catch (SQLException | URISyntaxException e){
                e.printStackTrace();
            }


        }else{
            // ask the user to login
            resp.getWriter().println(EventsPage.RETURN_TO_LANDING);
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
            resp.getWriter().println(EventsPage.RETURN_TO_LANDING);
        }
    }



}

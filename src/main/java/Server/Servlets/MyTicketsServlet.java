package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.SessionsJDBC;
import DataBase.TicketsJDBC;
import Server.LoginServletConstants;
import UI.EventsPage;
import UI.MyTicketsPage;
import UI.UIConstants;
import Util.ServletUtil;
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

public class MyTicketsServlet extends HttpServlet {

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
            String userName = "";
            String userEmail = "";
            int zipcode = 0;
            //String eventId = req.getParameter("eventId");
            String ticketId = req.getParameter("ticketId");
            LOGGER.info("parameter_mytickets_ticketId:" + ticketId);
            try (Connection connection = DBCPDataSource.getConnection()){
                if(ticketId != null){
                    int ticketID = Integer.parseInt(ticketId);

                    ResultSet ticket = TicketsJDBC.executeSelectTicketsByTicketId(connection, ticketID);

                    resp.getWriter().println(MyTicketsPage.transferTicket(ticket));

                }else{
                    ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                    if(user.next()){
                        userEmail = user.getString("email");
                    }
                    ResultSet myTickets = TicketsJDBC.executeSelectTicketsByBuyer(connection, userEmail);

                    resp.getWriter().println(MyTicketsPage.displayMyTickets(myTickets));
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
                //TODO: verify the body exists and it contains a =
                LOGGER.info("body: " + body);
                String[] bodyParts = body.split("&");


                String ticketIdPart = bodyParts[0];
                String ticketId = ServletUtil.getBodyParameter(ticketIdPart);
                int ticketID = Integer.parseInt(ticketId);

                String newOwnerEmailPart = bodyParts[1];
                String newOwnerEmail = ServletUtil.getBodyParameter(newOwnerEmailPart);

                try (Connection connection = DBCPDataSource.getConnection()){
                    TicketsJDBC.transferTicket(connection, newOwnerEmail, ticketID);

                    resp.getWriter().println(MyTicketsPage.TRANSFER_SUCCESSFUL);

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

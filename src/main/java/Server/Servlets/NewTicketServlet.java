package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import DataBase.TicketsJDBC;
import Server.LoginServletConstants;
import UI.NewEventPage;
import UI.NewTicketPage;
import UI.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Server.HttpServer.LOGGER;
import static Util.ServletUtil.getBodyParameter;


/**
 * Implements logic for the /newTicket path
 *
 */
public class NewTicketServlet extends HttpServlet {

    /**
     * Implement logic to deal get request sent to /newTicket
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
            String eventId = req.getParameter("eventId");

            resp.getWriter().println(NewTicketPage.getCreateTicketResponse(eventId));
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    /**
     * Implement logic to deal post request sent to /newTicket
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
                String[] bodyParts = body.split("&");

                String eventIdPart = bodyParts[0];
                String eventId = getBodyParameter(eventIdPart);
                LOGGER.info("event id for new ticket: " + eventId);

                String pricePart = bodyParts[1];
                String price = getBodyParameter(pricePart);
                LOGGER.info("price for new ticket: " + price);

                try (Connection connection = DBCPDataSource.getConnection()){
                    TicketsJDBC.executeInsertTicket(connection, Integer.parseInt(price), Integer.parseInt(eventId));
                    resp.getWriter().println(NewTicketPage.ADD_TICKET_SUCCESS);

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

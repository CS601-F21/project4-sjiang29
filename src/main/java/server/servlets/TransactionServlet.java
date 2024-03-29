package server.servlets;

import database.DBCPDataSource;
import database.EventsJDBC;
import database.SessionsJDBC;
import server.LoginServletConstants;
import ui.TransactionPage;
import ui.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Implements logic for the /transactions path
 *
 */
public class TransactionServlet extends HttpServlet {

    /**
     * Implement logic to deal get request sent to /transactions
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
            try (Connection connection = DBCPDataSource.getConnection()){
                ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                if(user.next()){
                    userName = user.getString("name");
                    userEmail = user.getString("email");
                    zipcode = user.getInt("zipcode");
                }

                ResultSet transactions = EventsJDBC.executeDisplayUserTransactions(connection, userEmail);
                resp.getWriter().println(TransactionPage.displayTransactions(transactions));

            }catch (SQLException e){
                e.printStackTrace();
            }

        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }
}

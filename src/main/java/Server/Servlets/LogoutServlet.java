package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.SessionsJDBC;
import Server.LoginServletConstants;
import UI.AccountPage;
import UI.UIConstants;
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
 * Handles a request to sign out /logout
 */
public class LogoutServlet extends HttpServlet {


    /**
     * Reference code from cs 601 example code
     * Implement logic to deal get request sent to /logout
     * @param req
     * @param resp
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // log out by invalidating the session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);

        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.setStatus(HttpStatus.OK_200);
            req.getSession().invalidate();
            resp.getWriter().println(UIConstants.PAGE_HEADER);
            resp.getWriter().println("<h1 style=\"text-align: center\">Thank you.</h1>");
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
            resp.getWriter().println(UIConstants.PAGE_FOOTER);

        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println("<h1 style=\"text-align: center\">You've already logged out.</h1>");
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }


    }
}

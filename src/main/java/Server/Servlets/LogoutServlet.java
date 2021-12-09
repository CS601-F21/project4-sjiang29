package Server.Servlets;

import Server.LoginServletConstants;
import UI.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles a request to sign out
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // log out by invalidating the session
        req.getSession().invalidate();
        resp.getWriter().println(UIConstants.PAGE_HEADER);
        resp.getWriter().println("<h1 style=\"text-align: center\">Thank you.</h1>");
        resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        resp.getWriter().println(UIConstants.PAGE_FOOTER);

    }
}

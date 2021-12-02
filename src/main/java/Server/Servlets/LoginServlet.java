package Server.Servlets;


import DataBase.DBCPDataSource;
import DataBase.UsersJDBC;
import Server.LoginServerConstants;
import Util.ClientInfo;
import Util.Config;
import Util.HttpFetcher;
import Util.LoginUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import static Server.HttpServer.LOGGER;

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 */
public class LoginServlet extends HttpServlet {

    //private Connection connection;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>You have already been authenticated</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            return;
        }

        // retrieve the config info from the context
        Config config = (Config) req.getServletContext().getAttribute(LoginServerConstants.CONFIG_KEY);

        // retrieve the code provided by Slack
        String code = req.getParameter(LoginServerConstants.CODE_KEY);

        // generate the url to use to exchange the code for a token:
        // After the user successfully grants your app permission to access their Slack profile,
        // they'll be redirected back to your service along with the typical code that signifies
        // a temporary access code. Exchange that code for a real access token using the
        // /openid.connect.token method.
        String url = LoginUtilities.generateSlackTokenURL(config.getClient_id(), config.getClient_secret(), code, config.getRedirect_url());

        // Make the request to the token API
        String responseString = HttpFetcher.doGet(url, null);
        Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);

        ClientInfo clientInfo = LoginUtilities.verifyTokenResponse(response, sessionId);

        if(clientInfo == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Oops, login unsuccessful</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } else {
            String email = clientInfo.getEmail();
            String userName = clientInfo.getName();
            try (Connection connection = DBCPDataSource.getConnection()){
                LOGGER.info(connection);
                ResultSet result = UsersJDBC.executeSelectUserByEmail(connection, email);
                LOGGER.info(result);
                if(result.next() == false){
                    UsersJDBC.executeInsertUser(connection, userName, email);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }finally {
                req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Hello, " + clientInfo.getName() + "</h1>");
                resp.getWriter().println("<p><a href=\"/logout\">Signout</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            }


        }
    }
}
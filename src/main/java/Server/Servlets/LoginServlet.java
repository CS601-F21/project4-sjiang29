package Server.Servlets;


import DataBase.DBCPDataSource;
import DataBase.SessionsJDBC;
import DataBase.UsersJDBC;
import Server.LoginServletConstants;
import UI.LoginPage;
import Util.ClientInfo;
import Util.Config;
import Util.HttpFetcher;
import Util.LoginUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static Server.HttpServer.LOGGER;

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 */
public class LoginServlet extends HttpServlet {


    /**
     * Reference code from cs 601 example code
     * Implement logic to deal get request sent to /login
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
            resp.getWriter().println(LoginPage.LOGIN_ALREADY);
            return;
        }

        // retrieve the config info from the context
        Config config = (Config) req.getServletContext().getAttribute(LoginServletConstants.CONFIG_KEY);

        // retrieve the code provided by Slack
        String code = req.getParameter(LoginServletConstants.CODE_KEY);

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
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(LoginPage.failedLogin(url));

        } else {
            String email = clientInfo.getEmail();
            String userName = clientInfo.getName();
            try (Connection connection = DBCPDataSource.getConnection()){
                LOGGER.info(connection);
                ResultSet result = UsersJDBC.executeSelectUserByEmail(connection, email);
                LOGGER.info(result);
                if(result.next() == false){
                    // update users table
                    UsersJDBC.executeInsertUser(connection, userName, email);
                }
                // update sessions table
                SessionsJDBC.executeInsertSession(connection, sessionId, email);

            } catch(SQLException e) {
                e.printStackTrace();
            }finally {
                req.getSession().setAttribute(LoginServletConstants.CLIENT_INFO_KEY, clientInfo);
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(LoginPage.successfulLogin(clientInfo));
            }


        }
    }
}